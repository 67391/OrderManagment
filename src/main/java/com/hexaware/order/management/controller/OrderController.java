package com.hexaware.order.management.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.hexaware.order.management.dto.OrderDto;
import com.hexaware.order.management.service.OrdeService;


@CrossOrigin
@RestController
public class OrderController {

	private static final org.slf4j.Logger log = 
			org.slf4j.LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrdeService orderService;

	@GetMapping("/order/{id}")
	public ResponseEntity<OrderDto> get(@PathVariable Long id) {
		
		return ResponseEntity.ok(orderService.getOrder(id));
	}

//	@PostMapping(value = "/order",consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody OrderDto orderDto) throws Exception {
//		
//		return ResponseEntity.ok(orderService.addOrder(orderDto));
//	}
	
	@PostMapping(value = "/order/create",consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto orderDto) {
		return new ResponseEntity<OrderDto>(orderService.addOrder(orderDto),
				HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/order/delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Long> delete(@PathVariable Long id) {
		return new ResponseEntity<Long>(orderService.deleteOrder(id),
				HttpStatus.ACCEPTED);
	}
	
	@PutMapping(value = "/order/update",consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<OrderDto> update(@Valid @RequestBody OrderDto orderDto) throws Exception {
		orderService.updateOrder(orderDto);
		return new ResponseEntity<OrderDto>(orderDto,
				HttpStatus.CREATED);
	}
}
