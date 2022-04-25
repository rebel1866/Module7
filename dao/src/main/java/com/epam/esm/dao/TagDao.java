package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;

/**
 * This interface contains logic for data access for tags
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface TagDao {
    /**
     * Search for tags by multiple params
     * @param params - map with params for search
     * @return list of tags
     */
    List<Tag> findTags(Map<String, String> params) throws DaoException;

    /**
     * Add tag in database
     * @param tag to add
     * @return new tag
     * @throws DaoException if validation is failed
     */
    Tag addTag(Tag tag) throws DaoException;

    /**
     * Delete tag by id
     * @param id of tag to delete
     * @throws DaoException if id validation is failed
     */
    void deleteTag(int id) throws DaoException;

    /**
     * Find tag by id
     * @param id
     * @return tag that is found
     * @throws DaoException if id validation is failed
     */
    Tag findTagById(int id) throws DaoException;

    /**
     * Add tag to particular certificate
     * @param tag that will be added
     * @param certificateId - id of certificate that tag will be added to
     */
    void addTagToCertificate(Tag tag, int certificateId);

    /**
     * Get most popular tag of user with the highest cost of all orders
     * @return tag object
     */
    Tag getMostPopularTag();

    /**
     * method checks whether tag is exist
     * @param tag
     * @return true if tag is exist
     */
    boolean isTagExist(Tag tag);
}
