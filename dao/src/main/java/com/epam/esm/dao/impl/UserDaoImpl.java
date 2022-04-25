package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.parameterbuilder.ParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;
    private ParameterBuilder<User> parameterBuilder;
    private static final String ALL_ORDERS = "select e from Order e inner join e.user u inner join e.certificate c where " +
            "u.userId=:id";
    private static final String ORDER_BY_USER_AND_ORDER_ID = "select e from Order e inner join e.user u inner join " +
            "e.certificate c where u.userId=:userId and e.orderId=:orderId";

    @Autowired
    public void setParameterBuilder(ParameterBuilder<User> parameterBuilder) {
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Query query = parameterBuilder.generateQuery(params, User.class);
        List<User> users = query.getResultList();
        if (users.size() == 0) {
            throw new DaoException("messageCode27", "errorCode=1");
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new DaoException("WmessageCode28:" + id, "errorCode=1");
        }
        return user;
    }

    @Override
    public Order addOrderToUser(Order order) {
        entityManager.persist(order);
        entityManager.detach(order);
        return getOrderOfUser(order.getOrderId(), order.getUser().getUserId());
    }

    @Override
    public Order getOrderOfUser(int orderId, int userId) {
        TypedQuery<Order> query = entityManager.createQuery(ORDER_BY_USER_AND_ORDER_ID, Order.class).
                setParameter("userId", userId).setParameter("orderId", orderId);
        Order order;
        try {
            order = query.getSingleResult();
        } catch (NoResultException e) {
            String message = "orderId = " + orderId + " and userId = " + userId;
            throw new DaoException("WmessageCode30:" + message, "errorCode=1");
        }
        return order;
    }

    @Override
    public List<Order> getAllOrdersOfUser(int userId, int page, int pageSize) {
        TypedQuery<Order> query = entityManager.createQuery(ALL_ORDERS, Order.class).setParameter("id", userId);
        int startPoint = page * pageSize - pageSize;
        query.setFirstResult(startPoint).setMaxResults(pageSize);
        List<Order> orders = query.getResultList();
        if (orders.size() == 0) {
            throw new DaoException("WmessageCode32:" + userId, "errorCode=1");
        }
        return orders;
    }
}
