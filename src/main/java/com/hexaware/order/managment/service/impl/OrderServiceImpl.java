package com.hexaware.order.managment.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.order.managment.domain.Order;
import com.hexaware.order.managment.domain.OrderDetail;
import com.hexaware.order.managment.domain.OrderDetailId;
import com.hexaware.order.managment.domain.Product;
import com.hexaware.order.managment.dto.OrderDto;
import com.hexaware.order.managment.dto.ProductDto;
import com.hexaware.order.managment.repository.OrderDetailRepository;
import com.hexaware.order.managment.repository.OrderRepository;
import com.hexaware.order.managment.repository.ProductRepository;
import com.hexaware.order.managment.service.OrdeService;
import com.hexaware.order.managment.utils.ConstantsUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrdeService{

	private static final org.slf4j.Logger log = 
			org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrderDto getOrder(Long id) {

		Optional<Order> order = orderRepository.findById(id);

		if(order.isEmpty())
			return new OrderDto();
		else {
			OrderDto orderDto = modelMapper.map(order.get(),OrderDto.class);
			orderDto.getProducts().addAll(
					order.get().getOrdersDetails().stream().map(t ->{
						return modelMapper.map(t.getProduct(),ProductDto.class);
					}).collect(Collectors.toList())
			);
			return  orderDto;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderDto addOrder(OrderDto orderDto) throws Exception {

		//========================
		//Saving order
		orderDto.setStatus(ConstantsUtils.ORDER_STATUS_CREATED);
		Order newOrder = orderRepository.save(modelMapper.map(orderDto,Order.class));
		//========================
		
		List<OrderDetail> orderDeatils = orderDto.getProducts()
		.stream()
		.map(t ->{
			Optional<Product> p = productRepository.findById(t.getProductId());
			OrderDetail od = new OrderDetail(newOrder,p.get(),4,3,4);
			return od;
		}
		).collect(Collectors.toList());
		
		
		//========================
		//Saving orderDetail
		orderDetailRepository.saveAll(orderDeatils);
		//========================
		
		orderDto = modelMapper.map(newOrder, OrderDto.class);
		
		orderDto.getProducts().addAll(
				orderDeatils.stream().map(t ->{
					return modelMapper.map(t.getProduct(),ProductDto.class);
				}).collect(Collectors.toList())
		);
		
		return orderDto;
	}

}
