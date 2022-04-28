package com.epam.esm.logic;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.SearchCertificateRequest;
import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.exception.LogicException;

import java.util.List;

/**
 * This interface encapsulates application logic related to certificates
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CertificateLogic {
    /**
     * Method is used for searching certificates by multiple params
     * @param request with parameters to search
     * @param page
     * @param pageSize
     * @return list of certificates, that've been found as a result of request
     * @throws LogicException, if validation is not passed
     */
    List<CertificateDto> findCertificates(SearchCertificateRequest request, int page, int pageSize) throws LogicException;

    /**
     * Method is used to save certificate in database
     * @param request - contains info that will be persisted
     * @return certificate, that have been persisted
     * @throws LogicException, if validation is failed
     */
    CertificateDto addCertificate(CertificateDto request) throws LogicException;

    /**
     * Method is used for removing certificates
     * @param id - identifier of certificate to delete
     * @throws LogicException, if id is negative or 0
     */
    void deleteCertificate(int id) throws LogicException;

    /**
     * Method is used for updating certificates
     * @param request - fields that will be updated
     * @param id of certificate to update
     * @return certificate object with updated fields
     * @throws LogicException if id is negative
     */
    CertificateDto updateCertificate(UpdateCertificateRequest request, int id) throws LogicException;

    /**
     * Search by id
     * @param id of certificate
     * @return certificate with id that corresponds input id
     * @throws LogicException, if validation of id is failed
     */
    CertificateDto findCertificateById(int id) throws LogicException;
}
