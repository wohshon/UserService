package com.redhat.demospringboot.utils;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Component
@Order(1)
public class AuthFilter implements Filter{
	Logger log=Logger.getLogger(this.getClass().getName());

	@Autowired
	private AuthService authService;
	
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		//get header
		String jwt=(String)(((HttpServletRequest)request).getHeader("Authorization")).substring(7);
		log.info("jwt "+jwt);
		String publicKey=authService.getInfo(jwt);
		log.info("publicKey:"+publicKey);
		
		chain.doFilter(request, response);
	}
}
