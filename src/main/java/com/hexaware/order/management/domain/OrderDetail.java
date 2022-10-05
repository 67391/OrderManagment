package com.hexaware.order.management.domain;

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter 
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class OrderDetail {

	public OrderDetail(OrderDetailPK orderDetailPK,Integer qty, Float price, Integer discount) {
		this.orderDetailPK=orderDetailPK;
		this.qty = qty;
        this.price = price;
        this.discount = discount;
	}
	
//	public OrderDetail(Order order,OrderDetailId orderDetailId,Integer qty, Float price, Integer discount) {
//		this.order = order;
//		this.orderDetailId=orderDetailId;
//		this.qty = qty;
//        this.price = price;
//        this.discount = discount;
//	}
	
	public OrderDetail(Order order, Product product, Integer qty, Float price, Integer discount) {
        this.orderDetailPK = new OrderDetailPK(order.getOrderId(), product.getProductId());
        this.order = order;
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
    }
	
	
	@Id
	@EqualsAndHashCode.Include
	@EmbeddedId
	private OrderDetailPK orderDetailPK;

	@ManyToOne
	@MapsId("oderId")
	@JoinColumn(name = "order_id")
	@JsonIgnore
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private Order order;


	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private Product product;

	private Integer qty;

	private Float price;

	private Integer discount;
}
