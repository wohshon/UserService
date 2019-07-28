package com.redhat.demospringboot;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.redhat.demospringboot.model.UserObject;

@RestController
@RequestMapping("/rest")
@PropertySource("classpath:application.properties")
public class UserServiceFacadeController {
	Logger log=Logger.getLogger(this.getClass().getName());
    @Autowired
    private Environment env;
    @RequestMapping("/")
    public String index() {
        return "This is Facade Service";
    }
		
    @GetMapping("/userService/{userId}")
    public String getUser(@PathVariable String userId, @RequestHeader("Authorization") String token) {
        log.info("User Facade- get info for  "+userId);
        //call backend
        final String uri = env.getProperty("backend.endpoint");
        log.info("uri: "+uri);
        log.info("got token from header "+token);
        RestTemplate restTemplate = new RestTemplate();
        UserObject user=restTemplate.getForObject(uri+userId, UserObject.class);
    	return user.getName() ;
        //return "This is User Service  : " ;
    }

    @RequestMapping("/secured/{message}")
    public String secured(@PathVariable String message) {
        log.info("in secured "+message);
    	return "This is Service 1 Secured : "+message ;
    }

    
    @RequestMapping("/facade/{message}")
//    public String facade(@PathVariable String message, @RequestHeader("Authorization") String token) {
    public String facade(@PathVariable String message) {    
        log.info("in facade "+message);
        final String uri = env.getProperty("service2.endpoint");
        //use springboot
        AccessToken token=getAccessToken();
        log.info("================I got token "+token.getName()+"- "+token.getEmail());
        //+"-"+getAccessTokenString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+getAccessTokenString());
        HttpEntity<String> entity = new HttpEntity<String>("",headers);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.exchange(uri,HttpMethod.GET,entity,String.class).getBody();
        //String result = restTemplate.getForObject(uri, String.class);
    	return "This is Service 1 Facade calling : "+result ;
    }
    
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,
           proxyMode = ScopedProxyMode.TARGET_CLASS)
    private AccessToken getAccessToken() {
    	  HttpServletRequest request =
    			    ((ServletRequestAttributes) RequestContextHolder
    			      .currentRequestAttributes()).getRequest();
    			  return ((KeycloakPrincipal) request.getUserPrincipal())
    			    .getKeycloakSecurityContext().getToken();
    }

    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
     private String getAccessTokenString() {
     	  HttpServletRequest request =
     			    ((ServletRequestAttributes) RequestContextHolder
     			      .currentRequestAttributes()).getRequest();
     			  return ((KeycloakPrincipal) request.getUserPrincipal())
     			    .getKeycloakSecurityContext().getTokenString();
     }
}
