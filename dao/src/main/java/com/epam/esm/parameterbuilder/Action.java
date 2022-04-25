package com.epam.esm.parameterbuilder;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Action {
    Predicate doAction(CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder, Root root, String key, String value);
}
