//package com.epam.esm;
//
//import com.epam.esm.dao.UserDao;
//import com.epam.esm.dto.CertificateDto;
//import com.epam.esm.dto.OrderDto;
//import com.epam.esm.dto.UserDto;
//import com.epam.esm.entity.Certificate;
//import com.epam.esm.entity.Order;
//import com.epam.esm.entity.User;
//import com.epam.esm.exception.LogicException;
//import com.epam.esm.logic.UserLogic;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ServiceLayerTest.class)
//class UserLogicImplTest {
//    private UserLogic userLogic;
//    @MockBean
//    private UserDao userDao;
//    @Captor
//    private ArgumentCaptor<Order> captor;
//
//    @Autowired
//    public void setUserLogic(UserLogic userLogic) {
//        this.userLogic = userLogic;
//    }
//
//    @BeforeEach
//    public void before() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    @Test
//    void getUserByIdTest() {
//        User user = new User();
//        user.setUserName("name");
//        Mockito.when(userDao.getUserById(1)).thenReturn(user);
//        UserDto userDto = userLogic.getUserById(1);
//        assertEquals("name", userDto.getUserName());
//    }
//
//    @Test
//    void addOrderToUserTest() {
//        Order order = new Order();
//        order.setOrderId(1);
//        order.setUser(new User());
//        Certificate certificate = Certificate.builder().tags(new ArrayList<>()).build();
//        order.setCertificate(certificate);
//        Mockito.when(userDao.addOrderToUser(Mockito.any())).thenReturn(order);
//        Map<String, String> map = new HashMap<>();
//        map.put("comment", "comment");
//        CertificateDto certificateDto = CertificateDto.builder().giftCertificateId(1).tags(new ArrayList<>()).build();
//        userLogic.addOrderToUser(1, map);
//        Mockito.verify(userDao).addOrderToUser(captor.capture());
//        assertEquals("comment", captor.getValue().getComment());
//    }
//
//    @Test
//    void getAllOrdersOfUserTest() {
//        assertThrows(LogicException.class, () -> userLogic.getAllOrdersOfUser(-5, 1, 20));
//    }
//
//    @Test
//    void getOrderOfUserTest() {
//        Order order = new Order();
//        order.setOrderId(1);
//        order.setComment("test");
//        order.setUser(new User());
//        Certificate certificate = Certificate.builder().tags(new ArrayList<>()).build();
//        order.setCertificate(certificate);
//        Mockito.when(userDao.getOrderOfUser(1, 1)).thenReturn(order);
//        OrderDto orderDto = userLogic.getOrderOfUser(1, 1);
//        assertEquals("test", orderDto.getComment());
//    }
//}