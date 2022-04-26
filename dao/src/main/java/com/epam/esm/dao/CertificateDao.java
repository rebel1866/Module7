package com.epam.esm.dao;


import com.epam.esm.entity.Certificate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

/**
 * This interface contains logic for data access for certificates
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CertificateDao extends CrudRepository<Certificate,Integer>, QuerydslPredicateExecutor<Certificate> {

}
