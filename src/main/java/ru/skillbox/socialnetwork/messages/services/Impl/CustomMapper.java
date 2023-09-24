package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.skillbox.socialnetwork.messages.client.UsersClient;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;
import ru.skillbox.socialnetwork.messages.dto.AuthorDto;
import ru.skillbox.socialnetwork.messages.exception.exceptions.UserPrincipalsNotFoundException;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;
import ru.skillbox.socialnetwork.messages.repository.AuthorRepository;

import java.util.Optional;

/**
 * @author Artem Lebedev | 22/09/2023 - 14:53
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CustomMapper {
	private final UsersClient usersClient;
	private final AuthorRepository authorRepository;
	private final ModelMapper modelMapper;

	public AccountDto getAccountPrincipals(@NotNull Long id) {
		AccountDto accPrincipals;
		try {
			accPrincipals = usersClient.getUserDetailsById(id);
		} catch (RuntimeException e) {
			log.error(" ! User with id `{}` does not exist", id);
			throw new UserPrincipalsNotFoundException("User with id " + id + " does not exist");
		}
		return accPrincipals;
	}

	@NotNull
	public AuthorModel getAuthorModelFromId(Long id) {
		AccountDto acDto = getAccountPrincipals(id);
		Optional<AuthorModel> amO =
				Optional.ofNullable(authorRepository.findById(id).
						orElse(AuthorModel.builder()
								.id(acDto.getId())
								.firstName(acDto.getFirstName())
								.firstName(acDto.getLastName())
								.firstName(acDto.getPhoto()).build()));
		if (amO.isEmpty()) {
			log.error(" ! User with id `{}` does not exist", id);
			throw new UserPrincipalsNotFoundException("User with id " + id + " does not exist");
		}
		return amO.get();
	}

	@Nullable
	private AuthorDto getAuthorDtoFromId(Long id) {
		Optional<AuthorModel> amO =
				Optional.ofNullable(authorRepository.findById(id)
						.orElseThrow(() -> new UserPrincipalsNotFoundException("User with id " + id + " does not exist")));
		if (amO.isEmpty()) {
			log.error(" ! User with id `{}` does not exist", id);
			throw new UserPrincipalsNotFoundException("User with id " + id + " does not exist");
		}
		return modelMapper.map(amO.get(), AuthorDto.class);
	}

}
