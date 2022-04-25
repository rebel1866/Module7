package com.epam.esm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class OrderDto extends RepresentationModel<OrderDto> {
    private Integer orderId;
    private UserDto userDto;
    private CertificateDto certificateDto;
    private LocalDateTime creationTime;
    private Integer finalPrice;
    private String comment;
}
