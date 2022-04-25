package com.epam.esm.logic.impl;

import com.epam.esm.dto.SearchTagRequest;
import com.epam.esm.converter.TagEntityToDtoConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.TagLogic;
import com.epam.esm.converter.ObjectToMapConverter;
import com.epam.esm.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TagLogicImpl implements TagLogic {
    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDto> findTags(SearchTagRequest request) {
        Map<String, String> params = ObjectToMapConverter.convertToMap(request);
        List<Tag> tags;
        tags = tagDao.findTags(params);
        return TagEntityToDtoConverter.convertList(tags);
    }

    @Override
    public TagDto findTagById(int id) {
        Validation.validateId(id);
        Tag tag;
        tag = tagDao.findTagById(id);
        return TagEntityToDtoConverter.convert(tag);
    }

    @Override
    @Transactional
    public TagDto addTag(String tagName) {
        if (tagName == null || tagName.equals("")) {
            throw new LogicException("rCode17", "errorCode=3");
        }
        Tag tag = Tag.builder().tagName(tagName).build();
        Tag addedTag;
        addedTag = tagDao.addTag(tag);
        return TagEntityToDtoConverter.convert(addedTag);
    }

    @Override
    public void deleteTag(int id) {
        Validation.validateId(id);
        tagDao.deleteTag(id);
    }

    @Override
    public TagDto getMostPopularTag() {
        Tag tag = tagDao.getMostPopularTag();
        return TagEntityToDtoConverter.convert(tag);
    }
}
