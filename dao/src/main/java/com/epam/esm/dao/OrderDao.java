package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDao extends PagingAndSortingRepository<Order, Long> {
    Order findByOrderIdAndUserUserId(Integer orderId, Integer userId);
    Page<Order> findOrdersByUserUserId(Integer userId, Pageable pageable);
}
