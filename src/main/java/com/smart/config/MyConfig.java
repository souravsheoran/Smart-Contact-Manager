package com.smart.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfiguration {

@Bean
public UserDetailsService getUserDetailService() {
	
	return  new UserDetailsServiceImpl();
}

@Bean
public BCryptPasswordEncoder  passwordEncoder() {
	
	return new  BCryptPasswordEncoder();
}

@Bean
 public DaoAuthenticationProvider authenticationProvider() {
	 
	 DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	 
	 daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
	 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
 
 return daoAuthenticationProvider;
 }

@Override
public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
	// TODO Auto-generated method stub
	return super.webSecurityExpressionHandler();
}

@Override
public Filter springSecurityFilterChain() throws Exception {
	// TODO Auto-generated method stub
	return super.springSecurityFilterChain();
}

@Override
public WebInvocationPrivilegeEvaluator privilegeEvaluator() {
	// TODO Auto-generated method stub
	return super.privilegeEvaluator();
}

@Override
public void setFilterChainProxySecurityConfigurer(ObjectPostProcessor<Object> objectPostProcessor,
		ConfigurableListableBeanFactory beanFactory) throws Exception {
	// TODO Auto-generated method stub
	super.setFilterChainProxySecurityConfigurer(objectPostProcessor, beanFactory);
}

@Override
public void setImportMetadata(AnnotationMetadata importMetadata) {
	// TODO Auto-generated method stub
	super.setImportMetadata(importMetadata);
}

// Configure Method

protected void configure(HttpSecurity http) throws Exception{
    http
        .authorizeRequests()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/users/**").hasRole("USER")
            .requestMatchers("/**").permitAll()
            .and()
        .formLogin().loginPage("/signin")
            .and()
        .csrf().disable();
}




protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
auth.authenticationProvider(authenticationProvider());

}


}


