package com.epam.esm.logic.impl;

import com.epam.esm.converter.*;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.SearchCertificateRequest;
import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.entity.QCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.CertificateLogic;
import com.epam.esm.validation.Validation;
import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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
        QCertificate certificate = QCertificate.certificate;
        Predicate customerHasBirthday = certificate.certificateName.eq("GALLON");

        List<Certificate> certificates = (List<Certificate>) certificateDao.findAll(customerHasBirthday);
        return CertificateEntityToDtoConverter.convertList(certificates);
    }

    @Override
    @Transactional
    public CertificateDto findCertificateById(int id) {
        Validation.validateId(id);
        Optional<Certificate> optionalCertificate = certificateDao.findById(id);
        if (optionalCertificate.isEmpty()) {
            throw new LogicException("WmessageCode2:" + id, "errorCode=1");
        }
        return CertificateEntityToDtoConverter.convert(optionalCertificate.get());
    }

    @Override
    @Transactional
    public CertificateDto addCertificate(CertificateDto request) {
        Certificate certificate = CertificateDtoToEntityConverter.convert(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        LocalDateTime now = LocalDateTime.parse(formatter.format(LocalDateTime.now()));
        certificate.setCreationDate(now);   // cascade persist
        certificate.setLastUpdateTime(now);
        List<Tag> newTags = getNewTagsToAdd(certificate.getTags());
        List<Tag> oldWithId = certificate.getTags().stream().map(tag -> tagDao.findByTagName(tag.getTagName())).
                collect(Collectors.toList());
        certificate.setTags(oldWithId);
        List<Tag> newTagsWithId = newTags.stream().map(l -> tagDao.save(l)).collect(Collectors.toList());
        certificate.getTags().addAll(newTagsWithId);
        Certificate addedCertificate = certificateDao.save(certificate);
        return CertificateEntityToDtoConverter.convert(addedCertificate);
    }

    @Override
    public void deleteCertificate(int id) {
        Validation.validateId(id);
        boolean isExists = certificateDao.existsById(id);
        if (!isExists) {
            throw new LogicException("WmessageCode2:" + id, "errorCode=1");
        }
        certificateDao.deleteById(id);
    }

    @Override
    @Transactional
    public CertificateDto updateCertificate(UpdateCertificateRequest request, int id) {
        Validation.validateId(id);
        List<Tag> tags = TagDtoToEntityConverter.convertList(request.getTags());
        Certificate certificate = UpdateDtoToEntityConverter.convert(request);
        Optional<Certificate> optionalCertificate = certificateDao.findById(id);
        if (optionalCertificate.isEmpty()) {
            throw new LogicException("WmessageCode2:" + id, "errorCode=1");
        }
        Certificate certificateToUpdate = optionalCertificate.get();
        updateFields(certificate, certificateToUpdate);
        if (tags.size() != 0) {
            updateTagsOfCertificate(tags, certificateToUpdate);
        }
        return CertificateEntityToDtoConverter.convert(certificateToUpdate);
    }

    private void updateTagsOfCertificate(List<Tag> tags, Certificate certificateToUpdate) {
        List<Tag> newTags = getNewTagsToAdd(tags);
        List<Tag> oldWithId = tags.stream().map(tag -> tagDao.findByTagName(tag.getTagName())).collect(Collectors.toList());
        List<Tag> newTagsWithId = newTags.stream().map(l -> tagDao.save(l)).collect(Collectors.toList());
        newTagsWithId.addAll(oldWithId);
        List<Integer> allIds = certificateToUpdate.getTags().stream().map(Tag::getTagId).collect(Collectors.toList());
        for (Tag tag : newTagsWithId) {
            if (allIds.contains(tag.getTagId())) {
                throw new LogicException("WmessageCode12:" + tag.getTagName(), "errorCode=3");
            }
        }
        certificateToUpdate.getTags().addAll(newTagsWithId);
    }

    private void updateFields(Certificate certificate, Certificate certificateToUpdate) {
        String certificateName = certificate.getCertificateName();
        String description = certificate.getDescription();
        Integer price = certificate.getPrice();
        Integer duration = certificate.getDuration();
        if (certificateName != null) {
            certificateToUpdate.setCertificateName(certificateName);
        }
        if (description != null) {
            certificateToUpdate.setDescription(description);
        }
        if (price != null) {
            certificateToUpdate.setPrice(price);
        }
        if (duration != null) {
            certificateToUpdate.setDuration(duration);
        }
    }

    private List<Tag> getNewTagsToAdd(List<Tag> allTags) {
        List<Tag> newTags = new ArrayList<>();
        ListIterator<Tag> iterator = allTags.listIterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if (tag.getTagName() == null) {
                throw new LogicException("messageCode13", "errorCode=3");
            }
            boolean isTagExist = tagDao.existsByTagName(tag.getTagName());
            if (!isTagExist) {
                newTags.add(tag);
                iterator.remove();
            }
        }
        return newTags;
    }
}
