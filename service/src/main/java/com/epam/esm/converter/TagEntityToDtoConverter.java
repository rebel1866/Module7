package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting entity objects to dto
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class TagEntityToDtoConverter {
    public static TagDto convert(Tag tag) {
        return TagDto.builder().tagId(tag.getTagId()).tagName(tag.getTagName()).build();
    }

    public static List<TagDto> convertList(List<Tag> tags) {
        return tags.stream().map(TagEntityToDtoConverter::convert).collect(Collectors.toList());
    }
}
