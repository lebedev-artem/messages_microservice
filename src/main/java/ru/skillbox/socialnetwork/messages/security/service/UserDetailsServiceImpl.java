package ru.skillbox.socialnetwork.messages.security.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.client.HealthChecker;
import ru.skillbox.socialnetwork.messages.client.UsersClient;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;
import ru.skillbox.socialnetwork.messages.security.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {

	private final UsersClient usersClient;
	@Value("${users-service.host}")
	private String usersHost;
	@Value("${users-service.port}")
	private String usersPort;

	@Override
	public Person loadUserByUsername(String email) throws UsernameNotFoundException{
		HealthChecker.checkHealthyService(usersHost, Integer.valueOf(usersPort));

		AccountDto accountDto = usersClient.getUserDetails(email);

		return new Person(
				accountDto.getEmail(),
				accountDto.getPassword(),
				createListOfRoles(
						accountDto.getRoles())
						.stream()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList()),
				accountDto.getId(),
				accountDto.getFirstName(),
				accountDto.getLastName());
	}

	public List<String> createListOfRoles(@NonNull String rolesAsString) {
		return Arrays
				.stream(rolesAsString.split(", "))
				.collect(Collectors.toList());
	}

	public static Long getPrincipalId() {
		Person person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return person.getId();
	}
}
