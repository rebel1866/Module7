package com.epam.esm;


import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.dto.SearchCertificateRequest;
import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.exception.LogicException;
import com.epam.esm.logic.CertificateLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceLayerTest.class)
class CertificateLogicImplTest {

    private CertificateLogic certificateLogic;

    @Autowired
    public void setCertificateLogic(CertificateLogic certificateLogic) {
        this.certificateLogic = certificateLogic;
    }

    @MockBean
    private CertificateDao certificateDao;
    @Captor
    private ArgumentCaptor<Certificate> captor;
    @Captor
    private ArgumentCaptor<Map<String, String>> captorMap;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void addCertificateTest() {
        Certificate c = Certificate.builder().certificateName("test").tags(new ArrayList<>()).giftCertificateId(1).build();
        Mockito.when(certificateDao.findCertificateById(1)).thenReturn(c);
        Mockito.when(certificateDao.addCertificate(Mockito.any(Certificate.class))).thenReturn(c);
        Mockito.verify(certificateDao).addCertificate(captor.capture());
        Assertions.assertNotNull(captor.getValue().getLastUpdateTime());
    }

    @Test
    public void getCertificatesTest() {
        SearchCertificateRequest request = SearchCertificateRequest.builder().certificateName("test").build();
        certificateLogic.findCertificates(request);
        Mockito.verify(certificateDao).findCertificates(captorMap.capture());
        Assertions.assertNotNull(captorMap.getValue().get("certificateName"));
    }

    @Test
    public void getCertificatesByIdTest() {
        Certificate certificate = Certificate.builder().certificateName("test").tags(new ArrayList<>()).build();
        Mockito.when(certificateDao.findCertificateById(5)).thenReturn(certificate);
        LogicException logicException = Assertions.assertThrows(LogicException.class, () -> certificateLogic.findCertificateById(-5));
        Assertions.assertEquals("messageCode10", logicException.getMessage());
    }

    @Test
    public void deleteCertificateTest() {
        LogicException logicException = Assertions.assertThrows(LogicException.class, () -> certificateLogic.deleteCertificate(-5));
        Assertions.assertEquals("messageCode10", logicException.getMessage());
    }

    @Test
    public void updateCertificateTest() {
        UpdateCertificateRequest request = UpdateCertificateRequest.builder().certificateName("test").build();
        Certificate certificate = Certificate.builder().certificateName("test").tags(new ArrayList<>()).build();
        Map<String, String> map = new HashMap<>();
        map.put("certificate_name", "test");
        Mockito.when(certificateDao.findCertificateById(2)).thenReturn(certificate);
        Mockito.when(certificateDao.updateCertificate(2, new Certificate())).thenReturn(certificate);
        LogicException logicException = Assertions.assertThrows(LogicException.class, () -> certificateLogic.updateCertificate(request, -5));
        Assertions.assertEquals("messageCode10", logicException.getMessage());
    }
}