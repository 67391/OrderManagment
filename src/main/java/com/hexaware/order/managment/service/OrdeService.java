package com.hexaware.order.managment.service;

import com.hexaware.order.managment.dto.OrderDto;

public interface OrdeService {

	public OrderDto getOrder(Long id);

	public OrderDto addOrder(OrderDto order) throws Exception;
}
