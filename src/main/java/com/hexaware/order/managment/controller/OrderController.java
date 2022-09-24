package com.hexaware.order.managment.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.order.managment.dto.OrderDto;
import com.hexaware.order.managment.service.OrdeService;
import com.hexaware.order.managment.service.impl.OrderServiceImpl;

@RestController
public class OrderController {

	private static final org.slf4j.Logger log = 
			org.slf4j.LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrdeService orderService;

	@GetMapping("/getOrderDetails/{id}")
	public ResponseEntity<OrderDto> getOrderDetails(@PathVariable Long id) {
		if(Objects.isNull(orderService.getOrder(id)))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found order with id: "+id);
		else
			return ResponseEntity.ok(orderService.getOrder(id));
	}

	@PostMapping(value = "/addOrder",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody OrderDto orderDto) throws Exception {
		
		return ResponseEntity.ok(orderService.addOrder(orderDto));
	}
}
