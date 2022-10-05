package com.hexaware.order.management.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long productId;
	
    private String tittle;
	 
	private String description;
	
	private Float price;
	
	private Integer stock;
	
	public Product(Long productId, String tittle, String description, Float price, Integer stock) {
		
		this.productId = productId;
		this.tittle = tittle;
		this.description = description;
		this.price = price;
		this.stock = stock;
	}

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<OrderDetail> ordersDetails = new ArrayList<>();
	
	public String toString() {
		return "productId:"+productId+",tittle"+tittle;
	}
}
