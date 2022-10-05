package com.hexaware.order.management.utils;

public class ErrorMessageUtils {
	
	public static final String ORDER_NOT_FOUND = "No order found with id = {0}";
	public static final String PRODUCT_NOT_FOUND = "No product found with id = {0}";
	public static final String USER_NOT_FOUND = "No user found with id = {0}";
	public static final String USER_BY_EMAIL_NOT_FOUND = "No user found with email = {0}";
	public static final String PRODUCT_NOT_ENOUGH_STOCK_TO_ORDER = "Not enough stock to order, product id = {0}";
	public static final String DUPLICATE_PRODUCT_FOR_ORDER = "Product with id = {0} is duplicated in the order";
	public static final String ORDER_NON_MODIFIABLE = "Order with status {0} cannot be modified";
}
