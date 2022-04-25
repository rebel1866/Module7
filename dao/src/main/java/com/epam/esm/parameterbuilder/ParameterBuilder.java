package com.epam.esm.parameterbuilder;


import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used for creating queries , generated dynamically according to user request
 * @author Stanislav Melnikov
 * @version 1.0
 */
@Component
public class ParameterBuilder<T> {

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * This map contains key-values, where key is name of param for search, value - action that will be executed
     * for this param name
     */
    private Map<String, Action> actions = new HashMap<>();
    private static final String DEFAULT_PAGE_SIZE = "20";
    private static final String DEFAULT_PAGE = "1";
    private final Action PRICE_FROM = (criteriaQuery, criteriaBuilder, root, key, value) -> criteriaBuilder.
            greaterThanOrEqualTo(root.get("price"), Integer.parseInt(value));
    private final Action PRICE_TO = (criteriaQuery, criteriaBuilder, root, key, value) -> criteriaBuilder.
            lessThanOrEqualTo(root.get("price"), Integer.parseInt(value));
    private final Action TAG = (criteriaQuery, criteriaBuilder, root, key, value) -> criteriaBuilder.
            like(root.get("tagName"), wrapPercent(value));
    private final Action CONCAT_LIKE = (criteriaQuery, criteriaBuilder, root, key, value) -> criteriaBuilder.
            like(root.get(key), wrapPercent(value));
    private final Action TAG_NAME_CERTIFICATE = (criteriaQuery, criteriaBuilder, root, key, value) -> {
        List<String> values = Arrays.stream(value.split("and")).map(String::trim).collect(Collectors.toList());
        Predicate predicate;
        if (values.size() == 1) {
            predicate = criteriaBuilder.like(root.join("tags").get("tagName"), wrapPercent(value));
        } else {
            List<Predicate> predicateListEq = new ArrayList<>();
            Join<Certificate, Tag> join = root.join("tags");
            for (String val : values) {
                predicateListEq.add(criteriaBuilder.equal(join.get("tagName"), val));
            }
            Predicate[] predicatesArray = new Predicate[predicateListEq.size()];
            predicate = criteriaBuilder.or(predicateListEq.toArray(predicatesArray));
            criteriaQuery.groupBy(root.get("giftCertificateId"), root.get("certificateName"), root.get("price"),
                    root.get("creationDate")).having(criteriaBuilder.
                    equal(criteriaBuilder.count(root.get("giftCertificateId")), values.size()));
        }
        return predicate;
    };

    {
        actions.put("priceFrom", PRICE_FROM);
        actions.put("priceTo", PRICE_TO);
        actions.put("tagName", TAG_NAME_CERTIFICATE);
        actions.put("tag", TAG);
        actions.put("certificateName", CONCAT_LIKE);
        actions.put("userName", CONCAT_LIKE);
        actions.put("userSurname", CONCAT_LIKE);
        actions.put("login", CONCAT_LIKE);
        actions.put("email", CONCAT_LIKE);
    }

    /**
     * Main method of class, that returns generated query
     * @param params for query generating
     * @param clazz - class of object, that query will be parametrized with
     * @return query ready for executing
     */
    public Query generateQuery(Map<String, String> params, Class clazz) {
        String sorting = params.remove("sorting");
        String sortingOrder = params.remove("sortingOrder");
        String page = params.remove("page");
        String pageSize = params.remove("pageSize");
        String distinct = params.remove("distinct");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        if (distinct != null) {
            setDistinct(distinct, criteriaQuery);
        }
        addWhereBlock(criteriaQuery, criteriaBuilder, root, params);
        if (sorting != null & sortingOrder != null) {
            addOrderBlock(criteriaQuery, criteriaBuilder, root, sorting, sortingOrder);
        }
        Query query = entityManager.createQuery(criteriaQuery);
        if (page != null) {
            addLimitBlock(query, page, pageSize);
        } else {
            addLimitBlock(query, DEFAULT_PAGE, pageSize);
        }
        return query;
    }

    /**
     * Adding where clause to query according to given params
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param root
     * @param params - query where block will be created according to this params
     */
    private void addWhereBlock(CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<T> root,
                               Map<String, String> params) {
        var iterator = params.entrySet().iterator();
        List<Predicate> predicateList = new ArrayList<>();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            Action action = actions.get(key);
            Predicate predicate = action.doAction(criteriaQuery, criteriaBuilder, root, key, value);
            predicateList.add(predicate);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        criteriaQuery.where(predicateList.toArray(predicates));
    }

    /**
     * generate order block for query
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param root
     * @param sorting - mandatory param, used for generating order clause
     * @param sortingOrder - sorting order - desc or asc
     */
    private void addOrderBlock(CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<T> root,
                               String sorting, String sortingOrder) {
        String[] array = sorting.split(",");
        List<String> sortElements = Arrays.stream(array).map(String::trim).collect(Collectors.toList());
        List<Order> orderList = new ArrayList<>();
        for (String sortElement : sortElements) {
            Order order;
            if (sortingOrder.equals("asc")) {
                order = criteriaBuilder.asc(root.get(sortElement));
            } else {
                order = criteriaBuilder.desc(root.get(sortElement));
            }
            orderList.add(order);
        }
        Order[] ordersArray = new Order[orderList.size()];
        criteriaQuery.orderBy(orderList.toArray(ordersArray));
    }

    /**
     * Method is used for generating limit block
     * @param query
     * @param page
     * @param pageSize
     */
    private void addLimitBlock(Query query, String page, String pageSize) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        int startPoint = pageInt * pageSizeInt - pageSizeInt;
        query.setFirstResult(startPoint).setMaxResults(pageSizeInt);
    }

    private void setDistinct(String distinct, CriteriaQuery<T> criteriaQuery) {
        if (distinct.equals("true")) {
            criteriaQuery.distinct(true);
        }
    }

    private String wrapPercent(String value) {
        return new StringBuilder().append("%").append(value).append("%").toString();
    }
}
