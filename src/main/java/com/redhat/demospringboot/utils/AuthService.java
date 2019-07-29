package com.redhat.demospringboot.utils;

import org.springframework.stereotype.Component;

@Component
public class AuthService {

	private String getPublicKey() {
		return "";
	}
	
	public String getInfo(String token) {
		getPublicKey();
		return "YADA";
	}
}
