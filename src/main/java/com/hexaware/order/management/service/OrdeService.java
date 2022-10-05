package com.hexaware.order.management.service;


import com.hexaware.order.management.dto.OrderDto;

public interface OrdeService {

	public OrderDto getOrder(Long id);

	public OrderDto addOrder(OrderDto order);

	public Long deleteOrder(Long id);

	public void updateOrder(OrderDto orderDto) throws Exception;
}
