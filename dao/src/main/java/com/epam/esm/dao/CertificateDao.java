package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.entity.Certificate;

import java.util.List;
import java.util.Map;

/**
 * This interface contains logic for data access for certificates
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CertificateDao {
    /**
     * Search for certificates by params
     * @param params
     * @return list of certificates
     * @throws DaoException if nothing is found
     */
    List<Certificate> findCertificates(Map<String, String> params) throws DaoException;

    /**
     * Add certificate in database
     * @param certificate
     * @return certificate that have been found
     * @throws DaoException if certificate have not been found
     */
    Certificate addCertificate(Certificate certificate) throws DaoException;

    /**
     * Delete certificate by id
     * @param id of certificate to delete
     * @throws DaoException if id validation is failed
     */
    void deleteCertificate(int id) throws DaoException;

    /**
     * Update certificate by id
     * @param id of certificate that will be updated
     * @param certificate - info that will be updated
     * @return updated certificate
     * @throws DaoException if id validation is failed
     */
    Certificate updateCertificate(int id, Certificate certificate) throws DaoException;

    /**
     * Search for certificate by id
     * @param id if certificate for searching
     * @return certificate object
     * @throws DaoException if id validation is failed
     */
    Certificate findCertificateById(int id) throws DaoException;
}
