package com.harish.rest.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
	
	@GetMapping("/v1/person")
	public PersonV1 getPerson1() {
		return new PersonV1("Harish Kumar");
	}
	
	@GetMapping("/v2/person")
	public PersonV2 getPerson2() {
		return new PersonV2(new Name("Harish", "Kumar"));
	}
	
	@GetMapping(value="/person/param", params = "version=1")
	public PersonV1 personParam1() {
		return new PersonV1("Harish Kumar");
	}
	
	@GetMapping(value="/person/param", params = "version=2")
	public PersonV2 personParam2() {
		return new PersonV2(new Name("Harish", "Kumar"));
	}
	
	@GetMapping(value="/person/header", headers = "X-API-VERSION=1")
	public PersonV1 personHeader1() {
		return new PersonV1("Harish Kumar");
	}
	
	@GetMapping(value="/person/header", headers = "X-API-VERSION=2")
	public PersonV2 personHeader2() {
		return new PersonV2(new Name("Harish", "Kumar"));
	}
	
	@GetMapping(value="/person/produces", produces = "application/harish.company-v1+json")
	public PersonV1 personProduces1() {
		return new PersonV1("Harish Kumar");
	}
	
	@GetMapping(value="/person/produces", produces = "application/harish.company-v2+json")
	public PersonV2 personProduces2() {
		return new PersonV2(new Name("Harish", "Kumar"));
	}
	

}
