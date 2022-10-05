package com.hexaware.order.management.exeption;

import org.hibernate.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderManagementExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = DataAccessException.class)
	private ResponseEntity<Object> handleSqlException(RuntimeException ex, WebRequest request) {
		OrderManagmentError OrderManagmentError = new OrderManagmentError();
		OrderManagmentError.setMessage(ex.getMessage());
		return new ResponseEntity<>(OrderManagmentError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(WebExchangeBindException.class)
    private ResponseEntity<OrderManagmentError> handleWebExchangeBindException(WebExchangeBindException ex, WebRequest request) {
		OrderManagmentError OrderManagmentError = new OrderManagmentError();
		OrderManagmentError.setMessage(ex.getMessage());
		return new ResponseEntity<OrderManagmentError>(OrderManagmentError, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(TypeMismatchException.class)
    private ResponseEntity<OrderManagmentError> handleTypeMismatchException(TypeMismatchException ex, WebRequest request) {
		OrderManagmentError OrderManagmentError = new OrderManagmentError();
		OrderManagmentError.setMessage(ex.getMessage());
		return new ResponseEntity<OrderManagmentError>(OrderManagmentError, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<Object> handleException(ResponseStatusException ex, WebRequest request) {
		OrderManagmentError OrderManagmentError = new OrderManagmentError();
		OrderManagmentError.setMessage(ex.getMessage());
		System.out.println(ex.getMessage());
		return new ResponseEntity<>(OrderManagmentError, ex.getStatus());
    }
	
}
