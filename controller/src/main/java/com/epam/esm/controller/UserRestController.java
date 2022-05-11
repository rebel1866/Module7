package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SearchUserRequest;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RestControllerException;
import com.epam.esm.logic.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is used as controller for requests related to user objects
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/users")
public class UserRestController {
    private UserLogic userLogic;

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @GetMapping(consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('read_users')")
    public CollectionModel<UserDto> getUsers
            (@ModelAttribute @Valid SearchUserRequest request, BindingResult result, @RequestParam(value = "page",
                    defaultValue = "1", required = false) int page, @RequestParam(value = "pageSize", defaultValue = "20",
                    required = false) int pageSize) {
        if (result.hasErrors()) {
            throw new RestControllerException("messageCode11", "errorCode=3", result);
        }
        List<UserDto> userDtoList = userLogic.getUsers(request, page, pageSize);
        userDtoList.forEach(this::createLinksForUser);
        Link self = linkTo(UserRestController.class).withSelfRel();
        return CollectionModel.of(userDtoList, self);
    }

    @GetMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('read_user_by_id')")
    public UserDto getUserById(@PathVariable("id") int id) {
        UserDto userDto = userLogic.getUserById(id);
        createLinksForUser(userDto);
        return userDto;
    }

    @PostMapping(value = "/{userId}/orders", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#userId == principal.username && hasAuthority('add_order')")
    public EntityModel<OrderDto> addOrderForUser(@PathVariable("userId") int userId, @RequestBody Map<String, String> params) {
        OrderDto orderDto = userLogic.addOrderToUser(userId, params);
        createLinksForOrder(orderDto);
        Link selfLink = linkTo(methodOn(UserRestController.class).
                addOrderForUser(userId, new HashMap<>())).withSelfRel();
        return EntityModel.of(orderDto, selfLink);
    }

    @GetMapping(value = "/{userId}/orders", consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('get_all_user_orders')")
    public CollectionModel<OrderDto> getAllOrdersOfUser(@PathVariable("userId") int userId, @RequestParam(value = "page",
            defaultValue = "1", required = false) int page, @RequestParam(value = "pageSize", defaultValue = "20",
            required = false) int pageSize) {
        List<OrderDto> orderDtoList = userLogic.getAllOrdersOfUser(userId, page, pageSize);
        orderDtoList.forEach(this::createLinksForOrder);
        Link selfLink = linkTo(methodOn(UserRestController.class).
                getAllOrdersOfUser(userId, page, pageSize)).withSelfRel();
        return CollectionModel.of(orderDtoList, selfLink);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}", consumes = {"application/json"}, produces = {"application/json"})
    @PreAuthorize("hasAuthority('get_order_by_id')")
    public OrderDto getOrderOfUser(@PathVariable("orderId") int orderId, @PathVariable("userId") int userId) {
        OrderDto orderDto = userLogic.getOrderOfUser(orderId, userId);
        createLinksForOrder(orderDto);
        return orderDto;
    }

    private void createLinksForOrder(OrderDto orderDto) {
        Link self = linkTo(methodOn(UserRestController.class).
                getOrderOfUser(orderDto.getOrderId(), orderDto.getUserDto().getUserId())).withSelfRel();
        Link certificateLink = linkTo(CertificateRestController.class).slash(orderDto.getCertificateDto().getGiftCertificateId()).
                withRel("certificate");
        Link userLink = linkTo(UserRestController.class).slash(orderDto.getUserDto().getUserId()).withRel("user");
        orderDto.add(self, certificateLink, userLink);
    }

    private void createLinksForUser(UserDto userDto) {
        Link self = linkTo(UserRestController.class).slash(userDto.getUserId()).withSelfRel();
        Link allOrdersLink = linkTo(methodOn(UserRestController.class).
                getAllOrdersOfUser(userDto.getUserId(), 1, 20)).withRel("all orders of user");
        userDto.add(self, allOrdersLink);
    }
}
