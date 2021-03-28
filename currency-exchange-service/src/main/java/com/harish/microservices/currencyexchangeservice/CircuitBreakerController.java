package com.harish.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	@Retry(name = "sample-api", fallbackMethod = "fallbackResponse")
	public String sampleApi() {
		logger.info("Sample Api request received.");
		ResponseEntity<String> responseEntity = new RestTemplate().getForEntity("http://localhost:1234/dummy-service", String.class);
		return responseEntity.getBody();
	}
	
	private String fallbackResponse(Exception ex) {
		return "This is fallback response!!";
	}

}
