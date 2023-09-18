package ru.skillbox.socialnetwork.messages.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skillbox.socialnetwork.messages.security.model.Person;

public interface UserService extends UserDetailsService {

	@Override
	Person loadUserByUsername(String email) throws UsernameNotFoundException;

}
