package com.epam.esm.logic.impl;

import com.epam.esm.converter.OrderEntityToDtoConverter;
import com.epam.esm.converter.UserDtoToEntityConverter;
import com.epam.esm.converter.UserEntityToDtoConverter;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SearchUserRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.UserLogic;
import com.epam.esm.utils.QPredicate;
import com.epam.esm.utils.SortBuilder;
import com.epam.esm.validation.Validation;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.epam.esm.entity.QUser.*;

@Service
public class UserLogicImpl implements UserLogic {

    private UserDao userDao;
    private CertificateDao certificateDao;
    private OrderDao orderDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

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
    public List<UserDto> getUsers(SearchUserRequest request, int page, int pageSize) {
        Predicate predicate = buildPredicate(request);
        Validation.validatePageSize(pageSize);
        Validation.validatePage(page);
        Sort sort = SortBuilder.buildSort(request.getSorting(), request.getSortingOrder());
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        List<User> users = userDao.findAll(predicate, pageable).getContent();
        return UserEntityToDtoConverter.convertList(users);
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
        Validation.validatePage(page);
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

    @Override
    public User findUserByLogin(String login) {
        return userDao.findUserByLogin(login);
    }

    @Override
    public UserDto signUp(UserDto userDto) {
        User user = UserDtoToEntityConverter.convert(userDto);
        boolean isLoginExists = userDao.existsByLogin(userDto.getLogin());
        if (isLoginExists) {
            throw new LogicException("messageCode35", "errorCode=3");
        }
        String password = bCryptPasswordEncoder.encode(userDto.getPassword());
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setPassword(password);
        User added = userDao.save(user);
        return UserEntityToDtoConverter.convert(added);
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

    private Predicate buildPredicate(SearchUserRequest request) {
        return QPredicate.builder().add(request.getUserName(), user.userName::containsIgnoreCase).
                add(request.getUserSurname(), user.userSurname::containsIgnoreCase).add(request.getLogin(),
                        user.login::containsIgnoreCase).add(request.getEmail(), user.email::containsIgnoreCase).buildAnd();
    }
}
