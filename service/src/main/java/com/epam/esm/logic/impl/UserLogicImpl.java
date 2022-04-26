package com.epam.esm.logic.impl;

import com.epam.esm.converter.OrderEntityToDtoConverter;
import com.epam.esm.converter.UserEntityToDtoConverter;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SearchUserRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.UserLogic;
import com.epam.esm.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserLogicImpl implements UserLogic {

    private UserDao userDao;
    private CertificateDao certificateDao;
    private OrderDao orderDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setCertificateDao(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<UserDto> getUsers(SearchUserRequest request) {
//        Map<String, String> params = ObjectToMapConverter.convertToMap(request);
//        List<User> users = userDao.getUsers(params);
//        return UserEntityToDtoConverter.convertList(users);
        return new ArrayList<>();
    }

    @Override
    public UserDto getUserById(int id) {
        Validation.validateId(id);
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            throw new LogicException("WmessageCode28:" + id, "errorCode=1");
        }
        User user = userOptional.get();
        return UserEntityToDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public OrderDto addOrderToUser(int userId, Map<String, String> params) {
        String certificateId = params.get("certificateId");
        String comment = params.get("comment");
        validateIds(certificateId, userId);
        LocalDateTime creationTime = LocalDateTime.now();
        Optional<Certificate> optionalCertificate = certificateDao.findById(Integer.parseInt(certificateId));
        Optional<User> userOptional = userDao.findById(userId);
        checkOptionals(userOptional, optionalCertificate, userId, certificateId);
        User user = userOptional.get();
        Certificate certificate = optionalCertificate.get();
        Order orderToAdd = Order.builder().user(user).certificate(certificate).creationTime(creationTime).
                finalPrice(certificate.getPrice()).comment(comment).build();
        Order order = orderDao.save(orderToAdd);
        return OrderEntityToDtoConverter.convert(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(int userId, int page, int pageSize) {
        Validation.validateId(userId);
        Validation.validatePage(page);//try to do via validator
        Validation.validatePageSize(pageSize);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<Order> orders = orderDao.findOrdersByUserUserId(userId, pageable).getContent();
        if (orders.size() == 0) {
            throw new LogicException("WmessageCode32:" + userId, "errorCode=1");
        }
        return OrderEntityToDtoConverter.convertList(orders);
    }

    @Override
    public OrderDto getOrderOfUser(int orderId, int userId) {
        Validation.validateId(orderId);
        Validation.validateId(userId);
        Order order = orderDao.findByOrderIdAndUserUserId(orderId, userId);
        if (order == null) {
            String message = "orderId = " + orderId + " and userId = " + userId;
            throw new LogicException("WmessageCode30:" + message, "errorCode=1");
        }
        return OrderEntityToDtoConverter.convert(order);
    }

    private void checkOptionals(Optional<User> userOptional, Optional<Certificate> optionalCertificate,
                                int userId, String certificateId) {
        if (userOptional.isEmpty()) {
            throw new LogicException("WmessageCode28:" + userId, "errorCode=1");
        }
        if (optionalCertificate.isEmpty()) {
            throw new LogicException("WmessageCode2:" + certificateId, "errorCode=1");
        }
    }

    private void validateIds(String certificateIdStr, int userId) {
        if (certificateIdStr == null) {
            throw new LogicException("messageCode31", "errorCode=3");
        }
        Validation.validateId(userId);
        Validation.validateId(certificateIdStr);
    }
}
