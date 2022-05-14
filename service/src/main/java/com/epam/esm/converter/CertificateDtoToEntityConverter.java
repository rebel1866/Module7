package com.epam.esm.converter;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting dto objects to entity
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class CertificateDtoToEntityConverter {
    public static Certificate convert(CertificateDto request) {
        return Certificate.builder().certificateName(request.getCertificateName()).price(request.getPrice()).duration
                (request.getDuration()).tags(convertTags(request.getTags())).description(request.getDescription()).build();
    }

    private static List<Tag> convertTags(List<TagDto> sourceTags) {
        return sourceTags.stream().map(l -> Tag.builder().tagId(l.getTagId()).tagName(l.getTagName()).build()).
                collect(Collectors.toList());
    }
}
