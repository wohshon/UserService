package com.redhat.demospringboot.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthService {
    @Autowired
    private Environment env;
	Logger log=Logger.getLogger(this.getClass().getName());
    
	private String getPublicKey() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
	                    .loadTrustMaterial(null, acceptingTrustStrategy)
	                    .build();

	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

	    CloseableHttpClient httpClient = HttpClients.custom()
	                    .setSSLSocketFactory(csf)
	                    .build();
	    HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

	    requestFactory.setHttpClient(httpClient);
	    final String uri = env.getProperty("sso.internal.endpoint");
        log.info("uri: "+uri);
		
        ResponseEntity<String> response 
        = new RestTemplate(requestFactory).exchange(
        uri, HttpMethod.GET, null, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode publicKey = root.path("public_key");        
		//return response.getBody();
        return publicKey.asText();
	}
	
	public String getInfo(String token) {
		String results=null;
		try {
			results=getPublicKey();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}
}
