package com.epam.esm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class SearchCertificateRequest {
    @Size(min = 1,message = "rCode13")
    private String certificateName;
    @Size(min = 1,message = "rCode14")
    private String tagName;
    @Pattern(regexp = "^[1-9]+[0-9]*$",message = "rCode2")
    private String priceFrom;
    @Pattern(regexp = "^[1-9]+[0-9]*$",message = "rCode3")
    private String priceTo;
    @Pattern(regexp = "price|certificateName|creationDate|certificateName, creationDate",
            message = "rCode4")
    private String sorting;
    @Pattern(regexp = "asc|desc", message = "rCode5")
    private String sortingOrder;
    @Pattern(regexp = "^[1-9]+[0-9]*$",message = "rCode21")
    private String page;
    @Pattern(regexp = "^[1-9]+[0-9]*$",message = "rCode22")
    private String pageSize;
}
