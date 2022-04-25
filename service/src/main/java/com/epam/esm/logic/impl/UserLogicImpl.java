package com.epam.esm.logic.impl;

import com.epam.esm.converter.ObjectToMapConverter;
import com.epam.esm.converter.OrderEntityToDtoConverter;
import com.epam.esm.converter.UserEntityToDtoConverter;
import com.epam.esm.dao.CertificateDao;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserLogicImpl implements UserLogic {

    private UserDao userDao;
    private CertificateDao certificateDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setCertificateDao(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Override
    public List<UserDto> getUsers(SearchUserRequest request) {
        Map<String, String> params = ObjectToMapConverter.convertToMap(request);
        List<User> users = userDao.getUsers(params);
        return UserEntityToDtoConverter.convertList(users);
    }

    @Override
    public UserDto getUserById(int id) {
        Validation.validateId(id);
        User user = userDao.getUserById(id);
        return UserEntityToDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public OrderDto addOrderToUser(int userId, Map<String, String> params) {
        String certificateIdStr = params.get("certificateId");
        String comment = params.get("comment");
        if (certificateIdStr == null) {
            throw new LogicException("messageCode31", "errorCode=3");
        }
        Validation.validateId(userId);
        Validation.validateId(certificateIdStr);
        int certificateId = Integer.parseInt(certificateIdStr);
        LocalDateTime creationTime = LocalDateTime.now();
        Certificate certificate = certificateDao.findCertificateById(certificateId);
        int finalPrice = certificate.getPrice();
        User user = new User();
        user.setUserId(userId);
        Certificate certificateToAdd = new Certificate();
        certificateToAdd.setGiftCertificateId(certificateId);
        Order orderToAdd = Order.builder().user(user).certificate(certificateToAdd).creationTime(creationTime).
                finalPrice(finalPrice).comment(comment).build();
        Order order = userDao.addOrderToUser(orderToAdd);
        return OrderEntityToDtoConverter.convert(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(int userId, int page, int pageSize) {
        Validation.validateId(userId);
        Validation.validatePage(page);
        Validation.validatePageSize(pageSize);
        List<Order> orders = userDao.getAllOrdersOfUser(userId, page, pageSize);
        return OrderEntityToDtoConverter.convertList(orders);
    }

    @Override
    public OrderDto getOrderOfUser(int orderId, int userId) {
        Validation.validateId(orderId);
        Order order = userDao.getOrderOfUser(orderId, userId);
        return OrderEntityToDtoConverter.convert(order);
    }
}
