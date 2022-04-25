package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting dto objects to entity
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class TagDtoToEntityConverter {
    public static Tag convert(TagDto tagDto) {
       return Tag.builder().tagName(tagDto.getTagName()).tagId(tagDto.getTagId()).build();
    }
    public static List<Tag> convertList(List<TagDto> tags){
        if (tags != null) {
            return tags.stream().map(TagDtoToEntityConverter::convert).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
