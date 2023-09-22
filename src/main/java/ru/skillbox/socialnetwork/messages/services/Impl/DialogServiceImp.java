package ru.skillbox.socialnetwork.messages.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.client.UsersClient;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;
import ru.skillbox.socialnetwork.messages.dto.*;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.AuthorRepository;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

import static ru.skillbox.socialnetwork.messages.security.service.UserDetailsServiceImpl.getPrincipalId;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogServiceImp implements DialogService {
    private final ModelMapper modelMapper;
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private final UsersClient usersClient;
    private final AuthorRepository authorRepository;
	private final CustomMapper customMapper;


	/*
	Tested locally
    */
	@Override
	@Transactional
	public Object createDialog(@NotNull DialogDto dialogDto) {
//		init section
		MessageModel mm = new MessageModel();
		DialogModel dm = new DialogModel();
		AccountDto paPrincipal;
		AccountDto auPrincipal;
		long conversationAuthorId = dialogDto.getConversationAuthor().getId();
		long conversationPartnerId = dialogDto.getConversationPartner().getId();
		AuthorModel aum = customMapper.getAuthorModelFromId(conversationAuthorId);
		AuthorModel pam = customMapper.getAuthorModelFromId(conversationPartnerId);
		paPrincipal = customMapper.getAccountPrincipals(dialogDto.getConversationPartner().getId());
		auPrincipal = customMapper.getAccountPrincipals(dialogDto.getConversationAuthor().getId());

//		Trying to get dialog
		if (dialogRepository.existsByConversationAuthorAndConversationPartner
				(aum, pam)) {
			return new ResponseEntity<>(new ErrorResponse("Dialog already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
//		NON NULL checking
		if (dialogDto.getConversationPartner().getId() == null | dialogDto.getConversationAuthor().getId() == null) {
			return new ResponseEntity<>(new ErrorResponse("Wrong data for author / partner", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}

//      Ищем в базе author автора и партнера
		Optional<AuthorModel> aumO = Optional.ofNullable(authorRepository.findById(auPrincipal.getId()).orElse(AuthorModel.builder()
				.id(auPrincipal.getId())
				.firstName(auPrincipal.getFirstName())
				.lastName(auPrincipal.getLastName())
				.photo(auPrincipal.getPhoto())
				.build()));

		if (aumO.isPresent()) {
			aum = aumO.get();
		} else {
			new ResponseEntity<>(new ErrorResponse("Wrong author data", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}

		Optional<AuthorModel> pamO = Optional.ofNullable(authorRepository.findById(paPrincipal.getId()).orElse(AuthorModel.builder()
				.id(paPrincipal.getId())
				.firstName(paPrincipal.getFirstName())
				.lastName(paPrincipal.getLastName())
				.photo(paPrincipal.getPhoto())
				.build()));
		if (pamO.isPresent()) {
			pam = pamO.get();
		} else  {
			new ResponseEntity<>(new ErrorResponse("Wrong partner data", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
//		Создаем сообщение тестовое, оно будет последним в диалоге
		mm = MessageModel.builder()
				.isDeleted(false)
				.time(new Timestamp(System.currentTimeMillis()))
				.author(customMapper.getAuthorModelFromId(dialogDto.getLastMessage().getAuthor().getId()))
				.messageText(dialogDto.getLastMessage().getMessageText())
				.status(EMessageStatus.SENT)
				.dialogId(null) // will set below
				.build();
//		Создаем диалог
		dm = DialogModel.builder()
				.isDeleted(false)
				.unreadCount(1)
				.conversationAuthor(aum)
				.conversationPartner(pam)
				.lastMessage(mm)
				.build();

		DialogDto dtest = objectMapper.convertValue(dm, DialogDto.class);
		log.info(dm.toString());
		log.info(dtest.toString());
		MessageDto mtest = objectMapper.convertValue(mm, MessageDto.class);
		log.info(mm.toString());
		log.info(mtest.toString());


//		dialogRepository.save(dm);
		mm.setDialogId(dm.getId());

		log.info(" * Dialog {} saved", dm.getId());
//		Пока не используется, выводиться сущность созданная
		DialogDto dd = DialogDto.builder()
				.unreadCount(dm.getUnreadCount())
				.conversationPartner(modelMapper.map(aum, AuthorDto.class))
				.lastMessage(modelMapper.map(mm, MessageDto.class))
				.build();

		return new ResponseEntity<>(dm, HttpStatus.OK);
	}

	/*
	Tested locally
	 */
	@Override
	public Object getDialogsList(Pageable pageable) {
		Long conversationAuthorId = getPrincipalId();
		AuthorModel aum = customMapper.getAuthorModelFromId(conversationAuthorId);
		Page<DialogModel> p = dialogRepository.findAllByConversationAuthor(aum, pageable);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}
	/*
	Tested
    */
	@Override
	public Object getUnreadCount() {
		UnreadCountDto ucd = new UnreadCountDto(0);
		try {
			ucd.setCount(dialogRepository.countUnreadCountByConversationAuthorId(getPrincipalId()));
		} catch (RuntimeException e) {
			return new ResponseEntity<>("Dialogs for user " + getPrincipalId() + " not found. " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(ucd, HttpStatus.OK);
	}
}
