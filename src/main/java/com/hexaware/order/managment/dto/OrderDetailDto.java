package com.hexaware.order.managment.dto;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class OrderDetailDto {

	
	private ProductDto product;

	
	private Integer qty;
	
	private Float price;
	
	private Integer discount;
}
