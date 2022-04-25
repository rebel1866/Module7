package com.epam.esm.converter;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting entity objects to dto
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class OrderEntityToDtoConverter {
    public static OrderDto convert(Order order) {
        return OrderDto.builder().orderId(order.getOrderId()).certificateDto(CertificateEntityToDtoConverter.
                        convert(order.getCertificate())).userDto(UserEntityToDtoConverter.convert(order.getUser())).
                creationTime(order.getCreationTime()).finalPrice(order.getFinalPrice()).comment(order.getComment()).build();
    }

    public static List<OrderDto> convertList(List<Order> orders) {
        return orders.stream().map(OrderEntityToDtoConverter::convert).collect(Collectors.toList());
    }
}
