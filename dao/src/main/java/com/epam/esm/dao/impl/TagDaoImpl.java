package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
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
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private EntityManager entityManager;
    private ParameterBuilder<Tag> parameterBuilder;
    private static final String COUNT_BY_NAME = "select count(o) from Tag o where o.tagName=:name";
    private static final String MOST_USED_TAG_OF_USER_MAX_ORDERS_COST = "select t.tag_id, t.tag_name, t.operation, t.operation_time from gift_certificates " +
            "inner join cert_tags ct on gift_certificates.gift_certificate_id = ct.gift_certificate_id inner join tags " +
            "t on ct.tag_id = t.tag_id inner join orders o on gift_certificates.gift_certificate_id = o.gift_certificate_id " +
            "where user_id = (select  o.user_id from users inner join orders o on users.user_id = o.user_id " +
            "group by user_name order by sum(final_price) desc limit 1) group by tag_name order by count(tag_name) desc " +
            "limit 1";

    @Autowired
    public void setParameterBuilder(ParameterBuilder<Tag> parameterBuilder) {
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    public List<Tag> findTags(Map<String, String> params) {
        Query query = parameterBuilder.generateQuery(params, Tag.class);
        List<Tag> tags = query.getResultList();
        if (tags.size() == 0) {
            throw new DaoException("messageCode6", "errorCode=1");
        }
        return tags;
    }

    @Override
    public Tag findTagById(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            throw new DaoException("WmessageCode7:" + id, "errorCode=1");
        }
        return tag;
    }

    @Override
    public Tag addTag(Tag tag) {
        if (isTagExist(tag)) {
            throw new DaoException("WmessageCode12:" + tag.getTagName(), "errorCode=3");
        }
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void deleteTag(int id) {
        Tag tag = findTagById(id);
        entityManager.remove(tag);
    }

    @Override
    public void addTagToCertificate(Tag tag, int certificateId) {
        Certificate certificate = entityManager.find(Certificate.class, certificateId);
        for (Tag element : certificate.getTags()) {
            if (element.getTagId().equals(tag.getTagId())) {
                throw new DaoException("WmessageCode12:" + tag.getTagName(), "errorCode=3");
            }
        }
        certificate.getTags().add(tag);
    }

    @Override
    public boolean isTagExist(Tag tag) {
        long amount = entityManager.createQuery(COUNT_BY_NAME, Long.class).
                setParameter("name", tag.getTagName()).getSingleResult();
        return amount != 0;
    }

    @Override
    public Tag getMostPopularTag() {
        return (Tag) entityManager.createNativeQuery(MOST_USED_TAG_OF_USER_MAX_ORDERS_COST, Tag.class).getSingleResult();
    }
}
