package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    private Integer orderId;
    private UserDto userDto;
    private CertificateDto certificateDto;
    private LocalDateTime creationTime;
    private Integer finalPrice;
    private String comment;
}
