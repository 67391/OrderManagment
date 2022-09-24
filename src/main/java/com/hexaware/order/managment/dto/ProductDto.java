package com.hexaware.order.managment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor 
@AllArgsConstructor
@ToString
public class ProductDto {

	private Long productId;
	
    private String tittle;
	 
	private String description;
	
	private Integer price;
	
	private Integer stock;
}
