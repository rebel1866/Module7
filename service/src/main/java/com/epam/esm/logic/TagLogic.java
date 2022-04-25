package com.epam.esm.logic;

import com.epam.esm.dto.SearchTagRequest;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.LogicException;

import java.util.List;

/**
 * This interface encapsulates application logic related to tags
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface TagLogic {
    /**
     * Method is used for tag searching
     * @param request with params for search
     * @return list of tags have been found
     */
    List<TagDto> findTags(SearchTagRequest request) throws LogicException;

    /**
     * Method is used for adding tag in database
     *
     * @param tagName - name of new tag
     * @return new tag object that is saved in database
     */
    TagDto addTag(String tagName) throws LogicException;

    /**
     * Method is used to delete tag
     *
     * @param id of tag to delete
     * @throws LogicException if validation is failed
     */
    void deleteTag(int id) throws LogicException;

    /**
     * Method is used for searching tags by id
     * @param id - tag id for search
     * @return tag object that is found
     * @throws LogicException if id is negative
     */
    TagDto findTagById(int id) throws LogicException;

    /**
     * Method returns most widely tag of user that have the highest cost of all orders
     * @return tag object
     */
    TagDto getMostPopularTag();
}
