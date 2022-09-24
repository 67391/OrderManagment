package com.hexaware.order.managment.domain;

import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString
public class OrderDetail {

	public OrderDetail(Order order, Product product, int qty, float price, int discount) {
        this.orderDetailId = new OrderDetailId(order.getOrderId(), product.getProductId());
        this.order = order;
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
    }
	
	@EmbeddedId
	private OrderDetailId orderDetailId;

	@ManyToOne
	@MapsId("oderId")
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;


	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;

	private int qty;

	private float price;

	private int discount;
}
