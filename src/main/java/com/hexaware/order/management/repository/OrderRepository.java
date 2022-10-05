package com.hexaware.order.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.order.management.domain.Order;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<Order, Long> {

}
