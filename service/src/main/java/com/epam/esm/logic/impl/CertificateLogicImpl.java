package com.epam.esm.logic.impl;

import com.epam.esm.converter.*;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.SearchCertificateRequest;
import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.entity.Tag;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.CertificateLogic;
import com.epam.esm.validation.Validation;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateLogicImpl implements CertificateLogic {

    private CertificateDao certificateDao;
    private TagDao tagDao;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Autowired
    public void setCertificateDao(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    @Transactional
    public List<CertificateDto> findCertificates(SearchCertificateRequest request) {
        Map<String, String> params = ObjectToMapConverter.convertToMap(request);
        List<Certificate> certificates;
        params.put("distinct", "true");
        certificates = certificateDao.findCertificates(params);
        return CertificateEntityToDtoConverter.convertList(certificates);
    }

    @Override
    @Transactional
    public CertificateDto findCertificateById(int id) {
        Validation.validateId(id);
        Certificate certificate = certificateDao.findCertificateById(id);
        return CertificateEntityToDtoConverter.convert(certificate);
    }

    @Override
    @Transactional
    public CertificateDto addCertificate(CertificateDto request) {
        Certificate certificate = CertificateDtoToEntityConverter.convert(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        LocalDateTime now = LocalDateTime.parse(formatter.format(LocalDateTime.now()));
        certificate.setCreationDate(now);
        certificate.setLastUpdateTime(now);
        List<Tag> newTags = getNewTagsToAdd(certificate.getTags());
        List<Tag> oldWithId = certificate.getTags().stream().map(tag -> {
            Map<String, String> map = new HashMap<>();
            map.put("tag", tag.getTagName());
            return tagDao.findTags(map).get(0);
        }).collect(Collectors.toList());
        certificate.setTags(oldWithId);
        List<Tag> newTagsWithId = newTags.stream().map(l -> tagDao.addTag(l)).collect(Collectors.toList());
        certificate.getTags().addAll(newTagsWithId);
        Certificate addedCertificate = certificateDao.addCertificate(certificate);
        return CertificateEntityToDtoConverter.convert(addedCertificate);
    }

    @Override
    public void deleteCertificate(int id) {
        Validation.validateId(id);
        certificateDao.deleteCertificate(id);
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(UpdateCertificateRequest request, int id) {
        Validation.validateId(id);
        List<Tag> tags = TagDtoToEntityConverter.convertList(request.getTags());
        Certificate certificate = UpdateDtoToEntityConverter.convert(request);
        Certificate updatedCertificate = certificateDao.updateCertificate(id, certificate);
        if (tags.size() != 0) {
            List<Tag> newTags = getNewTagsToAdd(tags);
            List<Tag> oldWithId = tags.stream().map(tag -> {
                Map<String, String> map = new HashMap<>();
                map.put("tag", tag.getTagName());
                return tagDao.findTags(map).get(0);
            }).collect(Collectors.toList());
            List<Tag> newTagsWithId = newTags.stream().map(l -> tagDao.addTag(l)).collect(Collectors.toList());
            newTagsWithId.addAll(oldWithId);
            newTagsWithId.forEach(tag -> tagDao.addTagToCertificate(tag, id));
        }
        return CertificateEntityToDtoConverter.convert(updatedCertificate);
    }

    public List<Tag> getNewTagsToAdd(List<Tag> allTags) {
        List<Tag> newTags = new ArrayList<>();
        ListIterator<Tag> iterator = allTags.listIterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if (tag.getTagName() == null) {
                throw new LogicException("messageCode13", "errorCode=3");
            }
            boolean isTagExist = tagDao.isTagExist(tag);
            if (!isTagExist) {
                newTags.add(tag);
                iterator.remove();
            }
        }
        return newTags;
    }
}
