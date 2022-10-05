package com.hexaware.order.management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bCryptPassowordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
    
	@Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {
		 httpSecurity
		 .cors()
		 .and()
		 .csrf()
		 .disable()
         .authorizeRequests()
         .anyRequest()
         .authenticated()
         .and()
         .httpBasic();
		 return httpSecurity.build();
    }
//		httpSecurity.cors().and().csrf()
//		.disable().authorizeRequests()
//		.anyRequest().authenticated()
//		.and().httpBasic();
//	
//	return httpSecurity.build();
//}

   	@Bean
    public UserDetailsService userService() {
   		UserDetails userDetails = User.builder()
   				.username("user")
   				.password(bCryptPassowordEncoder().encode("pass"))
   				.roles("USER")
   				.build();
   		
   		return new InMemoryUserDetailsManager(userDetails);
   	}
}