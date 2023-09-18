package ru.skillbox.socialnetwork.messages.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class Person extends org.springframework.security.core.userdetails.User {

	private Long userId;

	private String firstName;

	private String lastName;

	public void setId(Long id) {
		this.userId = id;
	}

	public Long getId() {
		return userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Person(
			String username,
			String password,
			Collection<? extends GrantedAuthority> authorities,
			Long userId,
			String firstName,
			String lastName) {
		super(username, password, authorities);
		setId(userId);
		setFirstName(firstName);
		setLastName(lastName);
	}
}
