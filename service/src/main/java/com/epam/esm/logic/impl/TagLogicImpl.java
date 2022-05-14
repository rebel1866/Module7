package com.epam.esm.logic.impl;

import com.epam.esm.dto.SearchTagRequest;
import com.epam.esm.converter.TagEntityToDtoConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.QTag;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.TagLogic;
import com.epam.esm.utils.SortBuilder;
import com.epam.esm.validation.Validation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagLogicImpl implements TagLogic {
    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDto> findTags(SearchTagRequest request, int page, int pageSize) {
        List<Tag> tags;
        Predicate predicate;
        if (request.getTag() != null) {
            predicate = QTag.tag.tagName.containsIgnoreCase(request.getTag());
        } else {
            predicate = new BooleanBuilder();
        }
        Validation.validatePage(page);
        Validation.validatePageSize(pageSize);
        Sort sort = SortBuilder.buildSort(request.getSorting(), request.getSortingOrder());
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        tags = tagDao.findAll(predicate, pageable).getContent();
        if (tags.size() == 0) {
            throw new LogicException("messageCode6", "errorCode=1");
        }
        return TagEntityToDtoConverter.convertList(tags);
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
