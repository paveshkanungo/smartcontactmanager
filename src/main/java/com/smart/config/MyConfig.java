package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{
	
	@Bean
	public UserDetailsService getUserDetailService(){
		return new UserDetailsServiceImpl();
	}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return daoAuthenticationProvider;
	}
	
    /// configure method
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    		http
    			.csrf(csrf -> csrf.disable())
    			.authorizeHttpRequests(auth -> auth
    					.requestMatchers("/admin/**").hasRole("ADMIN")
    					.requestMatchers("/user/**").hasRole("USER")
    					.requestMatchers("/**").permitAll()
    			)
    			.formLogin(login -> login
    		            .loginPage("/signin")               
    		            .loginProcessingUrl("/dologin")    
    		            .defaultSuccessUrl("/user/index", true)
    		            .failureUrl("/login-fail")
    		            .permitAll()
    		        );
    		
    		return http.build();
    }
    
}
