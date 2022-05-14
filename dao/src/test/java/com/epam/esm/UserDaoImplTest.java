//package com.epam.esm;
//
//import com.epam.esm.dao.UserDao;
//import com.epam.esm.entity.Certificate;
//import com.epam.esm.entity.Order;
//import com.epam.esm.entity.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.init.ScriptUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = DaoLayerTest.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class UserDaoImplTest {
//
//    private JdbcTemplate jdbcTemplate;
//    private UserDao userDao;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Autowired
//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
//    @BeforeEach
//    public void before() throws SQLException {
//        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
//                new ClassPathResource("scripts.create/create_db.sql"));
//        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
//                new ClassPathResource("scripts.create/orders_and_users.sql"));
//    }
//
//    @Test
//    @Transactional
//    void getUserByIdTest() {
//        User userToAdd = new User();
//        userToAdd.setUserName("test");
//        entityManager.persist(userToAdd);
//        int lastId = jdbcTemplate.queryForObject("select max(user_id) from users", int.class);
//        User user = userDao.getUserById(lastId);
//        assertEquals("test", user.getUserName());
//    }
//
//    @Test
//    @Transactional
//    void addOrderToUserTest() {
//        User user = User.builder().userName("test1").build();
//        entityManager.persist(user);
//        Certificate certificate = new Certificate();
//        certificate.setCertificateName("testname");
//        entityManager.persist(certificate);
//        Certificate certificate1 = Certificate.builder().giftCertificateId(1).build();
//        Order order = Order.builder().user(user).comment("test").certificate(certificate1).build();
//        userDao.addOrderToUser(order);
//        int lastIdOrder = jdbcTemplate.queryForObject("select max(order_id) from orders", int.class);
//        int lastIdUser = jdbcTemplate.queryForObject("select max(user_id) from users", int.class);
//        Order order1 = userDao.getOrderOfUser(lastIdOrder, lastIdUser);
//        assertEquals("test", order1.getComment());
//    }
//
//    @Test
//    @Transactional
//    void getOrderOfUserTest() {
//        User user = User.builder().userName("test2").build();
//        entityManager.persist(user);
//        Certificate certificate = new Certificate();
//        certificate.setCertificateName("testname2");
//        entityManager.persist(certificate);
//        Certificate certificate1 = Certificate.builder().giftCertificateId(1).build();
//        Order order = Order.builder().user(user).comment("comment").certificate(certificate1).build();
//        userDao.addOrderToUser(order);
//        Order order1 = userDao.getOrderOfUser(1, 1);
//        assertEquals("comment", order1.getComment());
//    }
//
//    @Test
//    @Transactional
//    void getAllOrdersOfUserTest() {
//        User user = User.builder().userName("test2").build();
//        entityManager.persist(user);
//        Certificate certificate = new Certificate();
//        certificate.setCertificateName("testname2");
//        entityManager.persist(certificate);
//        Certificate certificate1 = Certificate.builder().giftCertificateId(1).build();
//        Order order = Order.builder().user(user).comment("comment").certificate(certificate1).build();
//        userDao.addOrderToUser(order);
//        List<Order> orders = userDao.getAllOrdersOfUser(1, 1, 20);
//        assertEquals(1, orders.size());
//
//    }
//
//    @AfterEach
//    public void after() throws SQLException {
//        entityManager.flush();
//        entityManager.clear();
//        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
//                new ClassPathResource("scripts.create/drop.sql"));
//    }
//}