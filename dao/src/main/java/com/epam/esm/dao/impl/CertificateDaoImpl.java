package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.parameterbuilder.ParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private EntityManager entityManager;

    private ParameterBuilder<Certificate> parameterBuilder;

    @Autowired
    public void setParameterBuilder(ParameterBuilder<Certificate> parameterBuilder) {
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    public List<Certificate> findCertificates(Map<String, String> params) {
        Query query = parameterBuilder.generateQuery(params, Certificate.class);
        List<Certificate> certificates = query.getResultList();
        if (certificates.size() == 0) {
            throw new DaoException("messageCode1", "errorCode=1");
        }
        return certificates;
    }


    @Override
    public Certificate findCertificateById(int id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if (certificate == null) {
            throw new DaoException("WmessageCode2:" + id, "errorCode=1");
        }
        return certificate;
    }

    @Override
    public Certificate addCertificate(Certificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteCertificate(int id) {
        Certificate certificate = findCertificateById(id);
        entityManager.remove(certificate);
    }

    @Override
    public Certificate updateCertificate(int id, Certificate certificate) {
        Certificate certificateToUpdate = findCertificateById(id);
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
        return certificateToUpdate;
    }
}
