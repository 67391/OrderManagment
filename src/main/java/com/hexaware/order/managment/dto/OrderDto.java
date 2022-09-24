package com.hexaware.order.managment.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexaware.order.managment.domain.Product;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@AllArgsConstructor 
@NoArgsConstructor 
@ToString
public class OrderDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long orderId;
	

	@Min(value = 1, message = "Age should not be less than 1")
	private Long userId;
	
	private String status;
	
	private Long total;
	
	@NotEmpty(message = "description cannot be empty")
	private String description;

	@NotEmpty(message = "Cannot create order without any product")
	private Set<ProductDto> products = new HashSet<>();

	
	
	/*@Override
	public String toString() {
		return "[description:"+description
				+"] [products:"+products.stream().toString()+"]";
	}*/
}
