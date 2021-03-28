package com.harish.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class UserController {

	@Autowired
	private UserDaoService userDaoService;

	@GetMapping("/users")
	public MappingJacksonValue retrieveAllUsers() {
		List<User> users = userDaoService.findAll();
		
		// to return users with all attributes
		//return users;
		
		// Apply filtering to exclude birthDate from response
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(users);
		mapping.setFilters(filterProvider);
		return mapping;
	}

	@GetMapping("/users/{id}")
	public MappingJacksonValue retrieveUser(@PathVariable int id) {
		User user = userDaoService.findOne(id);
		if(user == null) {
			throw new UserNotFoundException("id - " + id);
		}
		
		// Apply filtering to exclude birthDate from response
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","birthDate");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(filterProvider);
		return mapping;

		// HATEOAS implementation to send back links as well
	//	EntityModel<User> resource = EntityModel.of(user);
	//	WebMvcLinkBuilder myLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
	//	resource.add(myLink.withRel("all-users"));
	//	return resource;
	}

	@PostMapping("/users")
	public ResponseEntity createUser(@Valid @RequestBody User user) {

		User savedUser = userDaoService.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		User user = userDaoService.delete(id);
		if(user == null) {
			throw new UserNotFoundException("id - " + id);
		}
		return ResponseEntity.noContent().build();
	}
}
