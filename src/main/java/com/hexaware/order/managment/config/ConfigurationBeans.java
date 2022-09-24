package com.hexaware.order.managment.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
public class ConfigurationBeans {

	@Bean
	public ModelMapper modelMapper() {
		System.out.println("creating modelmapper");
	    return new ModelMapper();
	}
}
