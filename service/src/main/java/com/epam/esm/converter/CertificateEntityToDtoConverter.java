package com.epam.esm.converter;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting entity objects to dto
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class CertificateEntityToDtoConverter {
    public static CertificateDto convert(Certificate certificate) {
        return CertificateDto.builder().certificateName(certificate.getCertificateName()).price(certificate.getPrice()).
                description(certificate.getDescription()).duration(certificate.getDuration()).giftCertificateId
                        (certificate.getGiftCertificateId()).tags(convertTags(certificate.getTags())).
                creationDate(certificate.getCreationDate()).lastUpdateTime(certificate.getLastUpdateTime()).build();
    }

    public static List<CertificateDto> convertList(List<Certificate> certificates) {
        return certificates.stream().map(CertificateEntityToDtoConverter::convert).collect(Collectors.toList());
    }

    private static List<TagDto> convertTags(List<Tag> sourceTags) {
        return sourceTags.stream().map(tagEntity -> TagDto.builder().tagName(tagEntity.getTagName()).
                tagId(tagEntity.getTagId()).build()).collect(Collectors.toList());
    }
}
