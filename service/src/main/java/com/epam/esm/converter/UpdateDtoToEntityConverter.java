package com.epam.esm.converter;

import com.epam.esm.dto.UpdateCertificateRequest;
import com.epam.esm.entity.Certificate;

import java.util.HashMap;
import java.util.Map;
/**
 * This class is used for converting dto objects to entity
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class UpdateDtoToEntityConverter {
    public static Certificate convert(UpdateCertificateRequest request) {
        Certificate certificate = new Certificate();
        String price = request.getPrice();
        String duration = request.getDuration();
        certificate.setCertificateName(request.getCertificateName());
        certificate.setDescription(request.getDescription());
        if (price != null) {
            certificate.setPrice(Integer.parseInt(price));
        }
        if (duration != null) {
            certificate.setDuration(Integer.parseInt(duration));
        }
        return certificate;
    }
}
