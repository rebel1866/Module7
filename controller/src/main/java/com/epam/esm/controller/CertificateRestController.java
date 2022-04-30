package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RestControllerException;
import com.epam.esm.dto.SearchCertificateRequest;
import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.logic.CertificateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * This class is used as controller for requests related to certificate objects
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/certificates")
public class CertificateRestController {
    private CertificateLogic certificateLogic;

    @Autowired
    public void setCertificateLogic(CertificateLogic certificateLogic) {
        this.certificateLogic = certificateLogic;
    }

    @GetMapping(consumes = {"application/json"}, produces = {"application/json"})
    public List<CertificateDto> getCertificates
            (@ModelAttribute @Valid SearchCertificateRequest searchRequest, BindingResult bindingResult,
             @RequestParam(value = "page", defaultValue = "1", required = false)  int page, @RequestParam(value = "pageSize",
                    defaultValue = "20", required = false) int pageSize) {
        if (bindingResult.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", bindingResult);
        }
        List<CertificateDto> certificateDtoList = certificateLogic.findCertificates(searchRequest, page, pageSize);
        certificateDtoList.forEach(l -> createLinkFoTagsCertificates(l.getTags()));
        return certificateDtoList;
    }

    @GetMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public CertificateDto getCertificateById(@PathVariable("id") int id) {
        CertificateDto certificateDto = certificateLogic.findCertificateById(id);
        createLinkFoTagsCertificates(certificateDto.getTags());
        return certificateDto;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('add_certificate')")
    public CertificateDto addCertificate(@RequestBody @Valid CertificateDto request,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", bindingResult);
        }
        CertificateDto certificateDto = certificateLogic.addCertificate(request);
        createLinkFoTagsCertificates(certificateDto.getTags());
        return certificateDto;
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('delete_certificate')")
    public void deleteCertificate(@PathVariable("id") int id) {
        certificateLogic.deleteCertificate(id);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('update_certificate')")
    public CertificateDto updateCertificate(@PathVariable("id") int id, @RequestBody @Valid UpdateCertificateRequest
            request, BindingResult result) {
        if (result.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", result);
        }
        CertificateDto certificateDto = certificateLogic.updateCertificate(request, id);
        createLinkFoTagsCertificates(certificateDto.getTags());
        return certificateDto;
    }

    private void createLinkFoTagsCertificates(List<TagDto> tagDtoList) {
        tagDtoList.forEach((tagDto) -> {
            Link link = linkTo(CertificateRestController.class).withSelfRel();
            String linkWithParam = link.getHref() + "?tagName=" + tagDto.getTagName();
            link = Link.of(linkWithParam, "certificates with this tag");
            Link self = linkTo(TagRestController.class).slash(tagDto.getTagId()).withSelfRel();
            tagDto.add(self, link);
        });
    }
}
