//package com.epam.esm;
//
//
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//import com.epam.esm.exception.DaoException;
//import com.epam.esm.dao.CertificateDao;
//import com.epam.esm.entity.Certificate;
//import com.epam.esm.entity.Tag;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.init.ScriptUtils;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = DaoLayerTest.class)
//@Transactional
//public class CertificateDaoImplTest {
//
//
//    private CertificateDao certificateDao;
//
//    private JdbcTemplate jdbcTemplate;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    public void setCertificateDao(CertificateDao certificateDao) {
//        this.certificateDao = certificateDao;
//    }
//
//    @Autowired
//    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @BeforeEach
//    public void before() throws SQLException {
//        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
//                new ClassPathResource("scripts.create/create_db.sql"));
//        Tag tag = new Tag();
//        tag.setTagName("test");
//        entityManager.persist(tag);
//    }
//
//
//    @Test
//    public void findCertificateTest() throws DaoException {
//        Certificate certificate = getCertificate();
//        certificateDao.addCertificate(certificate);
//        Map<String, String> params = new HashMap<>();
//        params.put("certificateName", "TEST");
//        List<Certificate> certificates = certificateDao.findCertificates(params);
//        Assertions.assertEquals(certificate.getCertificateName(), certificates.get(0).getCertificateName());
//    }
//
//    @Test
//    public void deleteCertificateTest() throws DaoException {
//        Certificate certificate = getCertificate();
//        certificateDao.addCertificate(certificate);
//        Map<String, String> params = new HashMap<>();
//        params.put("certificateName", "TEST");
//        List<Certificate> certificates = certificateDao.findCertificates(params);
//        int id = certificates.get(0).getGiftCertificateId();
//        certificateDao.deleteCertificate(id);
//        DaoException thrown = Assertions.assertThrows(DaoException.class, () -> certificateDao.findCertificates(params));
//        Assertions.assertEquals("messageCode1", thrown.getMessage());
//    }
//
//    @Test
//    public void updateCertificateTest() throws DaoException {
//        Certificate certificate = getCertificate();
//        certificateDao.addCertificate(certificate);
//        Map<String, String> params = new HashMap<>();
//        params.put("certificateName", "TEST");
//        List<Certificate> certificates = certificateDao.findCertificates(params);
//        Assertions.assertEquals("TEST", certificates.get(0).getCertificateName());
//    }
//
//    @AfterEach
//    public void after() {
//        JdbcTestUtils.dropTables(jdbcTemplate, "gifts.cert_tags", "gifts.tags", "gifts.gift_certificates");
//    }
//
//    public static Certificate getCertificate() {
//        Certificate certificate = Certificate.builder().certificateName("TEST").lastUpdateTime(LocalDateTime.now()).
//                creationDate(LocalDateTime.now()).build();
//        Tag tag = Tag.builder().tagId(1).build();
//        List<Tag> tags = new ArrayList<>();
//        tags.add(tag);
//        certificate.setTags(tags);
//        return certificate;
//    }
//}