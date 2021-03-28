package com.harish.microservices.testservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harish.microservices.testservice.configuration.Configuration;

@RestController
public class TestController {
	
	@Autowired
	private Configuration configuration;
	
	@GetMapping("/value")
	public String retrieveLimits() {
		return new String("Hello World + " + configuration.getValue());
	}

}
