package com.redhat.demospringboot.utils;

import java.util.logging.Logger;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthService {
    @Autowired
    private Environment env;
	Logger log=Logger.getLogger(this.getClass().getName());
    
	private String getPublicKey() {
		   CloseableHttpClient httpClient
		      = HttpClients.custom()
		        .setSSLHostnameVerifier(new NoopHostnameVerifier())
		        .build();
		    HttpComponentsClientHttpRequestFactory requestFactory 
		      = new HttpComponentsClientHttpRequestFactory();
		    requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
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
