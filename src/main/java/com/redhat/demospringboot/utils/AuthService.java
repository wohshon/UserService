package com.redhat.demospringboot.utils;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthService {
    @Autowired
    private Environment env;
	Logger log=Logger.getLogger(this.getClass().getName());
    
	private String getPublicKey() {
		
		RestTemplate restTemplate = new RestTemplate();
        final String uri = env.getProperty("sso.internal.endpoint");
        log.info("uri: "+uri);
		
		ResponseEntity<String> response
		  = restTemplate.getForEntity(uri + "/", String.class);
		log.info(response.getBody());
		return response.getBody();
	}
	
	public String getInfo(String token) {
		getPublicKey();
		return "YADA";
	}
}
