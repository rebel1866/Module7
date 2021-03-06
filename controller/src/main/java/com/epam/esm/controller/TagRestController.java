package com.epam.esm.controller;

import com.epam.esm.dto.SearchTagRequest;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RestControllerException;
import com.epam.esm.logic.TagLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * This class is used as controller for requests related to tag objects
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
@RestController
@RequestMapping("/tags")
public class TagRestController {
    private TagLogic tagLogic;

    @Autowired
    public void setTagLogic(TagLogic tagLogic) {
        this.tagLogic = tagLogic;
    }

    @GetMapping(consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('read_tags')")
    public CollectionModel<TagDto> getTags(@ModelAttribute @Valid SearchTagRequest request, BindingResult bindingResult,
                                           @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                           @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize) {
        if (bindingResult.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", bindingResult);
        }
        List<TagDto> tagDtoList = tagLogic.findTags(request, page, pageSize);
        tagDtoList.forEach(this::createLinkFoTagsCertificates);
        Link self = linkTo(TagRestController.class).withSelfRel();
        return CollectionModel.of(tagDtoList, self);
    }

    @GetMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('read_tag_by_id')")
    public TagDto getTagById(@PathVariable("id") int id) {
        TagDto tagDto = tagLogic.findTagById(id);
        createLinkFoTagsCertificates(tagDto);
        return tagDto;
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('add_tag')")
    public EntityModel<TagDto> addTag(@RequestBody Map<String, String> param) {
        TagDto tagDto = tagLogic.addTag(param.get("tagName"));
        Link self = linkTo(TagRestController.class).withSelfRel();
        Link selfTag = linkTo(TagRestController.class).slash(tagDto.getTagId()).withRel("added tag");
        tagDto.add(selfTag);
        return EntityModel.of(tagDto, self);
    }

    @DeleteMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('delete_tag')")
    public void deleteTag(@PathVariable("id") int id) {
        tagLogic.deleteTag(id);
    }

    @GetMapping(value = "/mostPopular", consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('most_popular_tag')")
    public EntityModel<TagDto> getMostPopularTagOfUserHighestCostAllOrders() {
        TagDto tagDto = tagLogic.getMostPopularTag();
        Link self = linkTo(methodOn(TagRestController.class).
                getMostPopularTagOfUserHighestCostAllOrders()).withSelfRel();
        createLinkFoTagsCertificates(tagDto);
        return EntityModel.of(tagDto, self);
    }

    private void createLinkFoTagsCertificates(TagDto tagDto) {
        Link link = linkTo(CertificateRestController.class).withSelfRel();
        String linkWithParam = link.getHref() + "?tagName=" + tagDto.getTagName();
        link = Link.of(linkWithParam, "certificates with this tag");
        Link self = linkTo(TagRestController.class).slash(tagDto.getTagId()).withSelfRel();
        tagDto.add(self, link);
    }
}
