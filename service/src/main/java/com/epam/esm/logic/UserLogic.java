package com.epam.esm.logic;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SearchUserRequest;
import com.epam.esm.dto.UserDto;

import java.util.List;
/**
 * This interface encapsulates application logic related to user
 * @author Stanislav Melnikov
 * @version 1.0
 */
import java.util.Map;
public interface UserLogic {
    /**
     * Method is used for searching users by multiple params
     * @param request - contains params for search
     * @return list of users that are found
     */
    List<UserDto> getUsers(SearchUserRequest request);

    /**
     * Search for user by id
     * @param id of user for searching
     * @return user object, if found
     */
    UserDto getUserById(int id);


    /**
     * Method adds new order to user (user must already exist)
     * @param userId - user id that exist in database
     * @param params - info for new order
     * @return new order
     */
    OrderDto addOrderToUser(int userId, Map<String, String> params);

    /**
     * Method is used for getting all orders of user
     * @param userId
     * @param page
     * @param pageSize
     * @return list of orders that belong to user
     */
    List<OrderDto> getAllOrdersOfUser(int userId, int page, int pageSize);

    /**
     * Get particular order of user
     * @param orderId
     * @param userId
     * @return order of user that match given params
     */
    OrderDto getOrderOfUser(int orderId, int userId);
}
