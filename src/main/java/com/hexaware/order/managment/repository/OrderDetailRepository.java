package com.hexaware.order.managment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.order.managment.domain.Order;
import com.hexaware.order.managment.domain.OrderDetail;
import com.hexaware.order.managment.domain.OrderDetailId;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, OrderDetailId> {

}
