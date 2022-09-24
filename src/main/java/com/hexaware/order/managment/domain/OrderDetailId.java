package com.hexaware.order.managment.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data @AllArgsConstructor @NoArgsConstructor
@Embeddable
public class OrderDetailId implements Serializable{


	private static final long serialVersionUID = 1L;

	@Column(name = "order_id")
	private Long orderId;
	@Column(name = "product_id")
	private Long productId;

	/*public OrderDetailId(Long orderId, Long productId) {
		this.orderId = orderId;
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderDetailId)) return false;
		OrderDetailId orderDetailId = (OrderDetailId) o;
		return Objects.equals(getOrderId(), orderDetailId.getOrderId()) &&
				Objects.equals(getProductId(), orderDetailId.getProductId());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getOrderId(), getProductId());
	}*/
}
