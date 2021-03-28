package com.harish.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserJPAController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/jpa/users")
	public MappingJacksonValue retrieveAllUsers() {
		List<User> users = userRepository.findAll();
		
		// to return users with all attributes
		//return users;
		
		// Apply filtering to exclude birthDate from response
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(users);
		mapping.setFilters(filterProvider);
		return mapping;
	}

	@GetMapping("/jpa/users/{id}")
	public MappingJacksonValue retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id - " + id);
		}
		
		// Apply filtering to exclude birthDate from response
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","birthDate");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(user.get());
		mapping.setFilters(filterProvider);
		return mapping;

		// HATEOAS implementation to send back links as well
	//	EntityModel<User> resource = EntityModel.of(user);
	//	WebMvcLinkBuilder myLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
	//	resource.add(myLink.withRel("all-users"));
	//	return resource;
	}

	@PostMapping("/jpa/users")
	public ResponseEntity createUser(@Valid @RequestBody User user) {

		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/jpa/users/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		
		userRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePosts(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id - " + id);
		}
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id - " + id);
		}
		User user = userOptional.get();
		post.setUser(user);
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
}
