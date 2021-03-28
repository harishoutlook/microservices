package com.harish.rest.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<User>();
	
	private static int userCount = 3;
	
	static {
		users.add(new User(1, "Harish", new Date()));
		users.add(new User(2, "Kapil", new Date()));
		users.add(new User(3, "Vivek", new Date()));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User save(User user) {
		if(user.getId() == null) {
			user.setId(++userCount);
		}
		users.add(user);
		return user;
	}
	
	public User findOne(int id) {
		for(User user : users) {
			if(user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	public User delete(int id) {
		Iterator<User> useriIterator = users.iterator();
		while(useriIterator.hasNext()) {
			User user = useriIterator.next();
			if(user.getId() == id) {
				useriIterator.remove();
				return user;
			}
		}
		return null;
	}

}
