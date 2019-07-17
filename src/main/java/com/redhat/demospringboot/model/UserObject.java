package com.redhat.demospringboot.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter @Setter @NoArgsConstructor
public class UserObject {

	private String userId;
	private String name;
	
}
