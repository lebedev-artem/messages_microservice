package ru.skillbox.socialnetwork.messages.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;
import ru.skillbox.socialnetwork.messages.dto.*;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.exception.exceptions.DialogNotFoundException;
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
	private final DialogRepository dialogRepository;
	private final MessageRepository messageRepository;
	private final ObjectMapper objectMapper;
	private final AuthorRepository authorRepository;
	private final CustomMapper customMapper;


	/*
	Tested locally
    */
	@Override
	@Transactional
	public Object createDialog(@NotNull DialogDto dialogDto) {
//		init section
		DialogModel dm = new DialogModel();
//		DialogModel revdm = new DialogModel();
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
			log.error(" ! Dialog with income conditions already exists");
			return new ResponseEntity<>(
					new ErrorResponse("Dialog with income conditions already exists", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		if (dialogRepository.existsByConversationAuthorAndConversationPartner
				(pam, aum)) {
			log.error(" ! Dialog with income conditions already exists");
			return new ResponseEntity<>(
					new ErrorResponse("Dialog with income conditions already exists", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

//		NON NULL checking
		if (dialogDto.getConversationPartner().getId() == null | dialogDto.getConversationAuthor().getId() == null) {
			log.error(" ! Author or partner id is NULL");
			return new ResponseEntity<>(
					new ErrorResponse("Wrong data for author / partner", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
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
			log.error(" ! Error while getting or create Author model");
			new ResponseEntity<>(
					new ErrorResponse("Wrong author data", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		Optional<AuthorModel> pamO = Optional.ofNullable(authorRepository.findById(paPrincipal.getId()).orElse(AuthorModel.builder()
				.id(paPrincipal.getId())
				.firstName(paPrincipal.getFirstName())
				.lastName(paPrincipal.getLastName())
				.photo(paPrincipal.getPhoto())
				.build()));
		if (pamO.isPresent()) {
			pam = pamO.get();
		} else {
			log.error(" ! Error while getting or create Partner model");
			new ResponseEntity<>(
					new ErrorResponse("Wrong partner data", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		//TODO
//		все же надо два диалога делать
//		Создаем сообщение тестовое, оно будет последним в диалоге
//		mm = MessageModel.builder()
//				.isDeleted(false)
//				.time(new Timestamp(System.currentTimeMillis()))
//				.author(customMapper.getAuthorModelFromId(dialogDto.getLastMessage().getAuthor().getId()))
//				.messageText(dialogDto.getLastMessage().getMessageText())
//				.status(EMessageStatus.SENT)
//				.dialogId(null) // will set below
//				.build();
//		Создаем диалог
		dm = DialogModel.builder()
				.isDeleted(false)
				.unreadCount(1)
				.conversationAuthor(aum)
				.conversationPartner(pam)
				.lastMessage(new MessageModel())
				.build();

//		Создаем init message
		MessageModel initM = MessageModel.builder()
				.time(new Timestamp(System.currentTimeMillis()))
				.dialogId(dm.getId())
				.status(EMessageStatus.SENT)
				.messageText("")
				.author(aum)
				.isDeleted(false)
				.build();

//		Создаем диалог для партнера
//		revdm = dm;
//		Меняем местами автора и партнера
//		revdm.setConversationAuthor(pam);
//		revdm.setConversationPartner(aum);

//		test mapping
//		DialogDto dtest = objectMapper.convertValue(dm, DialogDto.class);
//		log.info(dm.toString());
//		log.info(dtest.toString());
//		MessageDto mtest = objectMapper.convertValue(mm, MessageDto.class);
//		log.info(mm.toString());
//		log.info(mtest.toString());
//		end test mapping
		dm.setLastMessage(initM);
		dialogRepository.save(dm);
		initM.setDialogId(dm.getId());
		messageRepository.save(initM);


//		dialogRepository.save(revdm);
//		mm.setDialogId(dm.getId());

		log.info(" * Dialog {} saved", dm.getId());
//		log.info(" * Dialog {} saved", revdm.getId());

//		mapping Model to DTO for output
		DialogDto ddto = objectMapper.convertValue(dm, DialogDto.class);
//		DialogDto revddto = objectMapper.convertValue(revdm, DialogDto.class);
//		String output = ddto.toString().concat(revddto.toString());

		return new ResponseEntity<>(ddto, HttpStatus.OK);
	}

	/*
	Tested locally
	 */
	@Override
	public Object getDialogsList(Pageable pageable) {
		Long authorId = getPrincipalId();
		AuthorModel aum = customMapper.getAuthorModelFromId(authorId);
		Optional<Page<DialogModel>> dialogsPage = Optional.ofNullable(dialogRepository.findAllByConversationAuthorOrConversationPartner(aum, aum, Pageable.unpaged()));
		if (dialogsPage.isEmpty()) {
			throw new DialogNotFoundException("Dialogs for Author id " + authorId + " not found");
		}
		log.info(" * Dialogs for Author {} contains {} elements", authorId, dialogsPage.get().getTotalElements());
		return new ResponseEntity<>(dialogsPage.get(), HttpStatus.OK);
	}

	/*
	Tested
	FIX: count as author and as partner
    */
	@Override
	public Object getUnreadCount() {
		UnreadCountDto ucd = new UnreadCountDto(0);
		AuthorModel aum = customMapper.getAuthorModelFromId(getPrincipalId());
		int count;
		Optional<List<DialogModel>> dmList = Optional.ofNullable(dialogRepository.findAllByConversationAuthorOrConversationPartner(aum, aum));
		if (dmList.isPresent()) {
			count = dmList.get().stream().mapToInt(DialogModel::getUnreadCount).sum();
		} else {
			log.error(" ! Dialog for user {} no found.", getPrincipalId());
			return new ResponseEntity<>(
					new ErrorResponse("Dialogs for user " + getPrincipalId() + " not found. ", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		ucd.setCount(count);
		return new ResponseEntity<>(ucd, HttpStatus.OK);
	}

	/*
	Postman tested
	need refactor
	 */
//	@Override
//	public Object getDialogOrCreate(UUID dialogId, Long conversationPartner1, Long conversationPartner2) {
//		if (dialogId == null) {
//			return createDialog(conversationPartner1, conversationPartner2);
//		}
//		Optional<DialogModel> dialogModel = dialogRepository.findById(dialogId);
//		return objectMapper.convertValue(dialogModel.orElseThrow(), DialogDto.class);
//	}
//
//	private DialogDto createDialog(Long conversationAuthor, Long conversationPartner) {
//		DialogModel dialogModel = DialogModel.builder()
//				.isDeleted(false)
//				.conversationAuthor(customMapper.getAuthorModelFromId(conversationAuthor))
//				.conversationPartner(customMapper.getAuthorModelFromId(conversationPartner))
//				.unreadCount(0)
//				.lastMessage(new MessageModel())
//				.build();
//		dialogRepository.save(dialogModel);
//		return objectMapper.convertValue(dialogModel, DialogDto.class);
//	}


	@Override
	@Transactional
	public void setLastMessage(UUID dialogId, MessageModel messageModel) {
		Optional<DialogModel> dm = dialogRepository.findById(dialogId);
		if (dm.isPresent()) {
			dm.get().setLastMessage(messageModel);
			dm.get().setUnreadCount(dm.get().getUnreadCount() + 1);
		} else throw new DialogNotFoundException("Dialog " + dialogId + " not found");
	}

	@Override
	@Transactional
	public void delDialog(UUID dialogId) {
		dialogRepository.deleteById(dialogId);
	}


}
