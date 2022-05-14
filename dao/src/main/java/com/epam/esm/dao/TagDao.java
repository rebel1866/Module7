package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * This interface contains logic for data access for tags
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface TagDao extends PagingAndSortingRepository<Tag, Integer>, QuerydslPredicateExecutor<Tag> {

    boolean existsByTagName(String tagName);

    Tag findByTagName(String tagName);

    @Query(value = "select t.tag_id, t.tag_name, t.operation, t.operation_time from gift_certificates " +
            "inner join cert_tags ct on gift_certificates.gift_certificate_id = ct.gift_certificate_id inner join tags " +
            "t on ct.tag_id = t.tag_id inner join orders o on gift_certificates.gift_certificate_id = o.gift_certificate_id " +
            "where user_id = (select  o.user_id from users inner join orders o on users.user_id = o.user_id " +
            "group by user_name order by sum(final_price) desc limit 1) group by tag_name order by count(tag_name) desc " +
            "limit 1", nativeQuery = true)
    Tag findMostPopularTag();
}
