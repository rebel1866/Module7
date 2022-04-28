package com.epam.esm.dao;


import com.epam.esm.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


/**
 * This interface contains logic for data access for certificates
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CertificateDao extends JpaRepository<Certificate, Integer>, QuerydslPredicateExecutor<Certificate> {

}
