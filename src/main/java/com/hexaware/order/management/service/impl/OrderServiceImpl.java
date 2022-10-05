package com.hexaware.order.management.service.impl;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.order.management.domain.Order;
import com.hexaware.order.management.domain.OrderDetail;
import com.hexaware.order.management.domain.OrderDetailPK;
import com.hexaware.order.management.domain.Product;
import com.hexaware.order.management.dto.OrderDto;
import com.hexaware.order.management.dto.ProductDto;
import com.hexaware.order.management.repository.OrderDetailRepository;
import com.hexaware.order.management.repository.OrderRepository;
import com.hexaware.order.management.repository.ProductRepository;
import com.hexaware.order.management.service.OrdeService;
import com.hexaware.order.management.utils.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrdeService{


	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public OrderDto getOrder(Long id) {

		Optional<Order> order = orderRepository.findById(id);

		if(order.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					MessageFormat.format(ErrorMessageUtils.ORDER_NOT_FOUND, String.valueOf(id)));
		else {
			OrderDto orderDto = mapper.map(order.get(),OrderDto.class);
			orderDto.getProducts().addAll(
					order.get().getOrdersDetails().stream().map(t ->{
						return mapper.map(t.getProduct(),ProductDto.class);
					}).collect(Collectors.toList())
					);
			return  orderDto;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderDto addOrder(OrderDto orderDto)  {



		//========================
		//Saving order
		orderDto.setStatus(ConstantsUtils.ORDER_STATUS_CREATED);
		Order newOrder = orderRepository.save(mapper.map(orderDto,Order.class));
		//========================

		List<OrderDetail> orderDeatils = orderDto.getProducts()
				.stream()
				.map(p ->{

					if(!productRepository.existsById(p.getProductId()))
						throw new ResponseStatusException(HttpStatus.NOT_FOUND,
								MessageFormat.format(ErrorMessageUtils.PRODUCT_NOT_FOUND, String.valueOf(p.getProductId())));

					Optional<Product> productOpt = productRepository.findById(p.getProductId());
					Product product = productOpt.get();
					OrderDetail od = new OrderDetail(newOrder,product,p.getQty(),p.getPrice(),p.getDiscount());
					return od;
				}
						).collect(Collectors.toList());


		//========================
		//Saving orderDetail
		orderDetailRepository.saveAll(orderDeatils);
		//========================

		orderDto = mapper.map(newOrder, OrderDto.class);

		orderDto.getProducts().addAll(
				orderDeatils.stream().map(t ->{
					return mapper.map(t.getProduct(),ProductDto.class);
				}).collect(Collectors.toList())
				);

		return orderDto;
	}

	@Override
	public Long deleteOrder(Long id) {

		if(!orderRepository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					MessageFormat.format(ErrorMessageUtils.ORDER_NOT_FOUND, String.valueOf(id)));
		else {
			orderRepository.deleteById(id);
			return  id;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateOrder(OrderDto orderDto) throws Exception {



		if(!orderRepository.existsById(orderDto.getOrderId()))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					MessageFormat.format(ErrorMessageUtils.ORDER_NOT_FOUND, String.valueOf(orderDto.getOrderId())));
		else {

			Order order = orderRepository.findById(orderDto.getOrderId()).get();
			if(order.getStatus().equals("COMFIRMED"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						MessageFormat.format(ErrorMessageUtils.ORDER_NON_MODIFIABLE, String.valueOf(order.getStatus())));

			validateExistenceProducts(orderDto.getProducts());		
			updateOrderProducts(order,  orderDto.getProducts(), orderDto.getStatus());
		}
	}

	private void validateExistenceProducts(Set<ProductDto> products) {

		products.stream().forEach(p -> {
			if(!productRepository.existsById(p.getProductId())) 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						MessageFormat.format(ErrorMessageUtils.PRODUCT_NOT_FOUND, String.valueOf(p.getProductId())));
		});
	}


	private void updateOrderProducts(Order order, Set<ProductDto> products, String status) throws Exception {

		Set<OrderDetail> orderDetailLst = order.getOrdersDetails().stream().collect(Collectors.toSet());

				Set<OrderDetail> orderDetailLstDelete = orderDetailLst.stream()
						.filter(od-> 
									products.stream().allMatch(p->!(od.getOrderDetailPK().getProductId().equals(p.getProductId())))
								).map(p ->   new OrderDetail(order,productRepository.findById(p.getProduct().getProductId()).get(),p.getQty(),p.getPrice(),p.getDiscount()))
						.collect(Collectors.toSet());
				log.info("orderDetailLstDelete is empty:"+orderDetailLstDelete.isEmpty());

//		Set<OrderDetailPK> orderDetailLstDelete = orderDetailLst.stream()
//				.filter(od-> 
//				products.stream().allMatch(p->!(od.getOrderDetailPK().getProductId().equals(p.getProductId())))
//						).map(p ->   new OrderDetailPK(p.getOrder().getOrderId() ,p.getProduct().getProductId()))
//				.collect(Collectors.toSet());
		log.info("orderDetailLstDelete is empty:"+orderDetailLstDelete.isEmpty());

		if(!orderDetailLstDelete.isEmpty()){

			Set<OrderDetail> orderDetailLstInsertUpdate = products.stream()
					.filter(p -> 
					orderDetailLstDelete.stream().allMatch(odd->!(odd.getOrderDetailPK().getProductId().equals(p.getProductId()))))
					.map(p ->   new OrderDetail(order,productRepository.findById(p.getProductId()).get(),p.getQty(),p.getPrice(),p.getDiscount()))
					.collect(Collectors.toSet());

			log.info("updatingInserting==================");
			//orderDetailLstInsertUpdate.stream().forEach(System.out::println);	
			//			for(OrderDetail od : orderDetailLstInsertUpdate) {
			//				log.info("insertingUpdating:"+od);
			//				orderDetailRepository.save(od);
			//			}
			if(status.equals("COMFIRMED"))
				saveAndValidateStockProduct(orderDetailLstInsertUpdate,orderDetailLstDelete);
			orderDetailRepository.saveAll(orderDetailLstInsertUpdate);
			log.info("deleting===================");
			
			orderDetailRepository.deleteAllByIdInBatch(orderDetailLstDelete.stream().map(od-> od.getOrderDetailPK()).collect(Collectors.toList()));
			
			
			//			for(OrderDetailPK od : orderDetailLstDelete) {
			//				log.info("orderDetailLstDelete:"+orderDetailRepository.existsById(od));
			//				//orderDetailRepository.deleteById(new OrderDetailPK(od.getOrderId(),od.getProductId()));
			//				
			//				orderDetailRepository.deleteAById(new OrderDetailPK(15l,1l));
			//			}
			//orderDetailRepository.deleteAllById(orderDetailLstDelete);
			//orderDetailRepository.deleteById(new OrderDetailPK(15l,1l));
			//orderDetailLstDelete.stream().forEach(System.out::println);	
			//orderDetailLstInsertUpdate.forEach(od->orderDetailRepository.deleteById(od.getOrderDetailPK()));
			//orderDetailRepository.deleteAll(orderDetailLstDelete);

			//			orderDetailRepository.deleteById(new OrderDetailPK(15l,21l));
			//			orderDetailLstDelete.stream().forEach(p->{
			//			Optional<Product> productOpt = productRepository.findById(p.getOrderDetailId().getProductId());
			//			
			//			Product product = productOpt.get();
			//			
			//			product.setStock(product.getStock()+p.getQty());
			//			
			//			productRepository.save(product);
			//			
			//		});
			//		
			//	orderDetailRepository.findAll();
		}else {

			Set<OrderDetail> orderDetailLstInsertUpdate = products.stream()
					.map(p ->  new OrderDetail(order,mapper.map(p,Product.class)
							,p.getQty(),p.getPrice(),p.getDiscount()))
					.collect(Collectors.toSet());
			if(status.equals("COMFIRMED"))
				saveAndValidateStockProduct(orderDetailLstInsertUpdate,null);
			orderDetailLstInsertUpdate.stream().forEach(System.out::println);				
			orderDetailRepository.saveAll(orderDetailLstInsertUpdate);
			
			//			orderDetailLstInsertUpdate.forEach(p->{
			//			
			//			Optional <OrderDetail> orderDetailOpt = orderDetailRepository.findById(p.getOrderDetailId());
			//			
			//			
			//			
			////			
			//			
			//		});
		}
	}

	private void saveAndValidateStockProduct(Set<OrderDetail> orderDetailLstInsertUpdate, Set<OrderDetail> orderDetailLstDelete) {
		
		
//		Set<Product> products = orderDetailLstInsertUpdate.stream()
//				.filter(p -> 
//				orderDetailLstDelete.stream().allMatch(odd->!(odd.getOrderDetailPK().getProductId().equals(p.getOrderDetailPK().getProductId()))))
//				.map(p ->   new Product(p.getProduct().getProductId(),p.getProduct().getTittle(),p.getProduct().getDescription(),p.getProduct().getPrice(), p.getProduct().getStock()))
//				.collect(Collectors.toSet());
		
		if(!Objects.isNull(orderDetailLstDelete))
			orderDetailLstDelete.stream().forEach(p->{
				Optional<Product> productOpt = productRepository.findById(p.getOrderDetailPK().getProductId());
				
				Product product = productOpt.get();
				
				product.setStock(product.getStock()+orderDetailRepository.findById(p.getOrderDetailPK()).get().getQty());
				
				productRepository.save(product);
			});
			
			
			
		orderDetailLstInsertUpdate.stream().forEach(p->{
			Optional<Product> productOpt = productRepository.findById(p.getOrderDetailPK().getProductId());

			Product product = productOpt.get();

			if(p.getQty()>product.getStock())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						MessageFormat.format(ErrorMessageUtils.PRODUCT_NOT_ENOUGH_STOCK_TO_ORDER, String.valueOf(p.getOrderDetailPK().getProductId())));
			
			product.setStock(product.getStock()-p.getQty());
			
			productRepository.save(product);
		});
	}

	//	@Override
	//	@Transactional(rollbackFor = Exception.class)
	//	public OrderDto updateOrder(OrderDto orderDto)  {
	//
	//		//========================
	//		//Saving order
	//		orderDto.setStatus(ConstantsUtils.ORDER_STATUS_CREATED);
	//		Order newOrder = orderRepository.save(modelMapper.map(orderDto,Order.class));
	//		//========================
	//		
	//		List<OrderDetail> orderDeatils = orderDto.getProducts()
	//		.stream()
	//		.map(t ->{
	//			Optional<Product> p = productRepository.findById(t.getProductId());
	//			OrderDetail od = new OrderDetail(newOrder,p.get(),4,3,4);
	//			return od;
	//		}
	//		).collect(Collectors.toList());
	//		
	//		
	//		//========================
	//		//Saving orderDetail
	//		orderDetailRepository.saveAll(orderDeatils);
	//		//========================
	//		
	//		orderDto = modelMapper.map(newOrder, OrderDto.class);
	//		
	//		orderDto.getProducts().addAll(
	//				orderDeatils.stream().map(t ->{
	//					return modelMapper.map(t.getProduct(),ProductDto.class);
	//				}).collect(Collectors.toList())
	//		);
	//		
	//		return orderDto;
	//	}
	//	
	//	@Override
	//	@Transactional(rollbackFor = Exception.class)
	//	public OrderDto deleteOrder(OrderDto orderDto)  {
	//
	//		//========================
	//		//Saving order
	//		orderDto.setStatus(ConstantsUtils.ORDER_STATUS_CREATED);
	//		Order newOrder = orderRepository.save(modelMapper.map(orderDto,Order.class));
	//		//========================
	//		
	//		List<OrderDetail> orderDeatils = orderDto.getProducts()
	//		.stream()
	//		.map(t ->{
	//			Optional<Product> p = productRepository.findById(t.getProductId());
	//			OrderDetail od = new OrderDetail(newOrder,p.get(),4,3,4);
	//			return od;
	//		}
	//		).collect(Collectors.toList());
	//		
	//		
	//		//========================
	//		//Saving orderDetail
	//		orderDetailRepository.saveAll(orderDeatils);
	//		//========================
	//		
	//		orderDto = modelMapper.map(newOrder, OrderDto.class);
	//		
	//		orderDto.getProducts().addAll(
	//				orderDeatils.stream().map(t ->{
	//					return modelMapper.map(t.getProduct(),ProductDto.class);
	//				}).collect(Collectors.toList())
	//		);
	//		
	//		return orderDto;
	//	}

}
