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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagLogicImpl implements TagLogic {
    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDto> findTags(SearchTagRequest request) {
//        Map<String, String> params = ObjectToMapConverter.convertToMap(request);
//        List<Tag> tags;
//        tags = tagDao.findTags(params);
//        return TagEntityToDtoConverter.convertList(tags);
        return new ArrayList<>();
    }

    @Override
    public TagDto findTagById(int id) {
        Validation.validateId(id);
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isEmpty()) {
            throw new LogicException("WmessageCode7:" + id, "errorCode=1");
        }
        return TagEntityToDtoConverter.convert(optionalTag.get());
    }

    @Override
    @Transactional
    public TagDto addTag(String tagName) {
        if (tagName == null || tagName.equals("")) {
            throw new LogicException("rCode17", "errorCode=3");
        }
        Tag tag = Tag.builder().tagName(tagName).build();
        Tag addedTag = tagDao.save(tag);
        return TagEntityToDtoConverter.convert(addedTag);
    }

    @Override
    public void deleteTag(int id) {
        Validation.validateId(id);
        boolean isExists = tagDao.existsById(id);
        if (!isExists) {
            throw new LogicException("WmessageCode7:" + id, "errorCode=1");
        }
        tagDao.deleteById(id);
    }

    @Override
    public TagDto getMostPopularTag() {
        Tag tag = tagDao.findMostPopularTag();
        return TagEntityToDtoConverter.convert(tag);
    }
}
