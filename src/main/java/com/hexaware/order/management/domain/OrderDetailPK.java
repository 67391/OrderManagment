package com.hexaware.order.management.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data 
@AllArgsConstructor 
@NoArgsConstructor
@Embeddable
public class OrderDetailPK implements Serializable{


	private static final long serialVersionUID = 1L;

	@Column(name = "order_id")
	private Long orderId;
	@Column(name = "product_id")
	private Long productId;
	
	
	public String toString() {
		return "orderId:"+orderId+",productId"+productId;
	}

}
