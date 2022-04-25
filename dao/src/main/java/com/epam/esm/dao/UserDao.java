package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * This interface contains logic for data access for users
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface UserDao {
    /**
     * Get users by multiple params
     * @param params for search
     * @return list of users
     */
    List<User> getUsers(Map<String, String> params);

    /**
     * Get user by id
     * @param id of user for search
     * @return user (if found)
     * @throws DaoException if no user is found
     */
    User getUserById(int id);

    /**
     * Add order to user
     * @param order
     * @return new order
     */
    Order addOrderToUser(Order order);

    /**
     *  method is used for getting particular order od particular user
     * @param orderId
     * @param userId
     * @return new order
     */
    Order getOrderOfUser(int orderId, int userId);

    /**
     * Get all orders of particular user
     * @param userId
     * @param page default = 1
     * @param pageSize default = 20
     * @return list of orders
     */
    List<Order> getAllOrdersOfUser(int userId, int page, int pageSize);
}
