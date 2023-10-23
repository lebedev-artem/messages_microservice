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
import java.util.stream.Collectors;

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
//		NON NULL checking
		if (dialogDto.getConversationPartner().getId() == null | dialogDto.getConversationAuthor().getId() == null) {
			log.error(" ! Author or partner id is NULL");
			return new ResponseEntity<>(
					new ErrorResponse("Wrong data for author / partner", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		AccountDto auPrncpl =
				customMapper.getAccountPrincipals(
						dialogDto.getConversationAuthor().getId());
		AccountDto paPrncpl =
				customMapper.getAccountPrincipals(
						dialogDto.getConversationPartner().getId());

//      Ищем в базе author автора и партнера, если не находим, создаем новых
		AuthorModel auM = customMapper.getAuthorModelFromId(auPrncpl.getId());
		AuthorModel paM = customMapper.getAuthorModelFromId(paPrncpl.getId());

//		Trying to get dialog
		if (dialogRepository.existsByConversationAuthorAndConversationPartner(auM, paM) |
				dialogRepository.existsByConversationAuthorAndConversationPartner(paM, auM)) {
			log.error(" ! Dialog with income conditions already exists");
			return new ResponseEntity<>(
					new ErrorResponse("Dialog with income conditions already exists", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
//      create dialogs
		DialogModel diMo;
		DialogModel revDiMo;
//		Создаем диалог
		diMo = DialogModel.builder()
				.isDeleted(false)
				.conversationAuthor(auM)
				.conversationPartner(paM)
//				.lastMessage(new MessageModel())
				.build();
		revDiMo = DialogModel.builder()
				.isDeleted(false)
				.conversationAuthor(paM)
				.conversationPartner(auM)
//				.lastMessage(new MessageModel())
				.build();

//		Создаем init message
//		MessageModel initM = getInitMessage(diMo, auM);
//		MessageModel revInitM = getInitMessage(revDiMo, paM);

//		устанавливаем в диалоги последнее сообщение
//		diMo.setLastMessage(initM);
//		revDiMo.setLastMessage(revInitM);

		diMo.setUnreadCount(diMo.getLastMessage() == null ? 0 : 1);
		revDiMo.setUnreadCount(diMo.getLastMessage() == null ? 0 : 1);

		dialogRepository.save(diMo);
		dialogRepository.save(revDiMo);

//		initM.setDialogId(diMo.getId());
//		revInitM.setDialogId(revDiMo.getId());
//		messageRepository.save(initM);

		log.info(" * Dialogs {}, {} saved", diMo.getId(), revDiMo.getId());

//		mapping Model to DTO for output
		DialogDto dDto = objectMapper.convertValue(diMo, DialogDto.class);
		return new ResponseEntity<>(dDto, HttpStatus.OK);
	}

	@Override
	@Transactional
	public Object createDialogWithThisMan(@NotNull Long authorId, Long partnerId) {

//		NON NULL checking
		if (authorId == null | partnerId == null) {
			log.error(" ! Author or partner id is NULL");
			return new ResponseEntity<>(
					new ErrorResponse("Wrong data for author / partner", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		AccountDto auPrncpl =
				customMapper.getAccountPrincipals(
						authorId);
		AccountDto paPrncpl =
				customMapper.getAccountPrincipals(
						partnerId);

//      Ищем в базе author автора и партнера, если не находим, создаем новых
		AuthorModel auM = customMapper.getAuthorModelFromId(auPrncpl.getId());
		AuthorModel paM = customMapper.getAuthorModelFromId(paPrncpl.getId());

//		Trying to get dialog
		if (dialogRepository.existsByConversationAuthorAndConversationPartner(auM, paM) |
				dialogRepository.existsByConversationAuthorAndConversationPartner(paM, auM)) {
			log.error(" ! Dialog with income conditions already exists");
			return new ResponseEntity<>(
					new ErrorResponse("Dialog with income conditions already exists", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
//      create dialogs
		DialogModel diMo;
		DialogModel revDiMo;
//		Создаем диалог
		diMo = DialogModel.builder()
				.isDeleted(false)
				.conversationAuthor(auM)
				.conversationPartner(paM)
//				.lastMessage(new MessageModel())
				.build();
		revDiMo = DialogModel.builder()
				.isDeleted(false)
				.conversationAuthor(paM)
				.conversationPartner(auM)
//				.lastMessage(new MessageModel())
				.build();

		diMo.setUnreadCount(diMo.getLastMessage() == null ? 0 : 1);
		revDiMo.setUnreadCount(diMo.getLastMessage() == null ? 0 : 1);

		dialogRepository.save(diMo);
		dialogRepository.save(revDiMo);
		log.info(" * Dialogs {}, {} saved", diMo.getId(), revDiMo.getId());

//		mapping Model to DTO for output
		DialogDto dDto = objectMapper.convertValue(diMo, DialogDto.class);
		return new ResponseEntity<>(dDto, HttpStatus.OK);
	}

	private static MessageModel getInitMessage(@NotNull DialogModel dialogModel, AuthorModel authorModel) {
		return MessageModel.builder()
				.time(new Timestamp(System.currentTimeMillis()))
				.dialogId(dialogModel.getId())
				.status(EMessageStatus.SENT)
				.messageText("")
				.author(authorModel)
				.isDeleted(false)
				.dialogId(dialogModel.getId())
				.build();
	}

	/*
	Tested locally
	 */
	@Override
	public Object getDialogsPage(Pageable pageable) {
		Long authorId = getPrincipalId();
		AuthorModel aum = customMapper.getAuthorModelFromId(authorId);
		Optional<Page<DialogModel>> dialogsPage = Optional.of(dialogRepository.findByConversationAuthor(aum, Pageable.unpaged()));
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
		Optional<List<DialogModel>> dmList = Optional.ofNullable(dialogRepository.findByConversationAuthor(aum));
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


	@Override
	@Transactional
	public void setLastMessage(UUID dialogId, MessageModel messageModel) {
		Optional<DialogModel> dm = dialogRepository.findById(dialogId);
		if (dm.isPresent()) {
			dm.get().setLastMessage(messageModel);
			if (!dm.get().getConversationAuthor().getId().equals(messageModel.getAuthor().getId())) {
				dm.get().setUnreadCount(dm.get().getUnreadCount() + 1);
			}
		} else throw new DialogNotFoundException("Dialog " + dialogId + " not found");
	}

	@Override
	@Transactional
	public Object delDialog(Long id) {
		AuthorModel aum = customMapper.getAuthorModelFromId(getPrincipalId());
		AuthorModel pam = customMapper.getAuthorModelFromId(id);
		Optional<DialogModel> dm = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(aum, pam));
		Optional<DialogModel> dmrev = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(pam, aum));
		if (dm.isPresent() & dmrev.isPresent())  {
			dialogRepository.delete(dm.get());
			dialogRepository.delete(dmrev.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return ResponseEntity.badRequest();
	}

//	coded spec for BOT
	@Override
	public List<DialogDto> getDialogsList(Long id) {
		List<DialogModel> dmList = dialogRepository.findByConversationAuthor(customMapper.getAuthorModelFromId(id));
		return dmList.stream().map(dm -> objectMapper.convertValue(dm, DialogDto.class)).collect(Collectors.toList());
	}

	@Override
	public Integer getMessageCountForDialog(UUID dialogId) {
		return messageRepository.countByDialogId(dialogId);
	}

}
