package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.*;
import ru.skillbox.socialnetwork.messages.dto.telegram.MessageInlineDto;
import ru.skillbox.socialnetwork.messages.dto.telegram.MessageTgDto;
import ru.skillbox.socialnetwork.messages.exception.exceptions.DialogNotFoundException;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.AuthorRepository;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.DialogService;
import ru.skillbox.socialnetwork.messages.services.MessageService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:14
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final ModelMapper modelMapper;
	private final MessageRepository messageRepository;
	private final DialogRepository dialogRepository;

	private final AuthorRepository authorRepository;
	private final CustomMapper customMapper;
	private final DialogService dialogService;

	private static Long userId;

	@Override
	@Transactional
	public Object createMessage(MessageDto messageDto) {

		MessageModel mm = modelMapper.map(messageDto, MessageModel.class);
		AuthorModel aum = customMapper.getAuthorModelFromId(mm.getAuthor().getId());

		MessageModel fmm = MessageModel.builder()
				.isDeleted(false)
				.time(mm.getTime() == null ? new Timestamp(System.currentTimeMillis()) : mm.getTime())
				.author(aum)
				.messageText(mm.getMessageText())
				.status(EMessageStatus.SENT)
				.dialogId(mm.getDialogId())
				.build();
//		TODO
//		дублировать сообщения
		dialogService.setLastMessage(mm.getDialogId(), fmm);
		messageRepository.save(fmm);
		log.info(" * Message {} saved", fmm.getDialogId());
		return new ResponseEntity<>(modelMapper.map(fmm, MessageDto.class), HttpStatus.OK);
	}

	@Override
	@Transactional
	public Object saveMessage(MessageTgDto msg) {

//		MessageModel mm = modelMapper.map(messageDto, MessageModel.class);

		/*
		Artem Lebedev написал в диалог с Tema Lebedev: first message
		Artem Lebedev автор сообзения в двух диалогах
		 */
		AuthorModel aum = customMapper.getAuthorModelFromId(msg.getAuthor());
		AuthorModel pam = customMapper.getAuthorModelFromId(msg.getPartner());

		DialogModel dm = dialogRepository.findByConversationAuthorAndConversationPartner(aum, pam);
		DialogModel revdm = dialogRepository.findByConversationAuthorAndConversationPartner(pam, aum);


		MessageModel mm = MessageModel.builder()
				.isDeleted(false)
				.time(msg.getTime() == null ? new Timestamp(System.currentTimeMillis()) : msg.getTime())
				.author(aum)
				.messageText(msg.getMessageText())
				.status(EMessageStatus.SENT)
				.dialogId(dm.getId())
				.build();


		MessageModel revmm = MessageModel.builder()
				.isDeleted(false)
				.time(msg.getTime() == null ? new Timestamp(System.currentTimeMillis()) : msg.getTime())
				.author(aum)
				.messageText(msg.getMessageText())
				.status(EMessageStatus.SENT)
				.dialogId(revdm.getId())
				.build();

		dialogService.setLastMessage(mm.getDialogId(), mm);
		dialogService.setLastMessage(revmm.getDialogId(), revmm);

		messageRepository.save(mm);
		messageRepository.save(revmm);
		log.info(" * Message {} and {} saved", mm.getId(), revmm.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	Tested
	 */
	@Override
	@Transactional
	public Object changeMessageStatus(Long partnerId) {
		Optional<DialogModel> dm;
		dm = chooseBetweenTwoDialogs(userId, partnerId);
		if (dm.isEmpty()) {
			throw new DialogNotFoundException("Dialog with income conditions not found");
		}

		log.info(" * Found dialog with id {}", dm.get().getId());

		Optional<List<MessageModel>> mmList =
				Optional.ofNullable(messageRepository
						.findByDialogId(dm.get().getId())
						.orElseThrow(() -> new DialogNotFoundException("No message found for dialog")));
		if (mmList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		mmList.get().forEach(m -> m.setStatus(EMessageStatus.READ));
		log.info(" * Status of messages for {} changed to READ", partnerId);
		dialogRepository.setUnreadCountToZero(dm.get().getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public Object getMessagesForDialog(Long companionId, Pageable pageable) {

		Optional<DialogModel> dialogModel = chooseBetweenTwoDialogs(userId, companionId);

		Page<MessageModel> unpagedMessages = null;
		if (dialogModel.isPresent()) {
			unpagedMessages = messageRepository.findByDialogId(dialogModel.get().getId(), Pageable.unpaged());
		} else {
			DialogDto dialogDto = DialogDto.builder()
					.conversationAuthor(AuthorDto.builder().id(userId).build())
					.conversationPartner(AuthorDto.builder().id(companionId).build())
					.build();

			dialogService.createDialog(dialogDto);
			Optional<DialogModel> dm = chooseBetweenTwoDialogs(companionId, userId);
			if (dm.isPresent()) {
				unpagedMessages = messageRepository.findByDialogId(dm.get().getId(), Pageable.unpaged());
			} else {
				throw new DialogNotFoundException("Dialog with income conditions not found");
			}
		}
		return unpagedMessages;
	}

	@Override
	@Transactional
	public List<MessageInlineDto> getMessagesListForDialog(UUID dialogId) {
		Optional<List<MessageModel>> om = messageRepository.findByDialogId(dialogId);

		List<MessageInlineDto> listOfMsg = new ArrayList<>();
		if (om.isEmpty()) {
			throw new DialogNotFoundException("Dialog with income conditions not found");
		}
		for (MessageModel mm : om.get()) {
			MessageInlineDto m = MessageInlineDto.builder()
					.text(mm.getMessageText())
					.timestamp(mm.getTime())
					.author(mm.getAuthor().getFirstName() + " " + mm.getAuthor().getLastName())
					.authorId(mm.getAuthor().getId())
					.build();
			listOfMsg.add(m);
			mm.setStatus(EMessageStatus.READ);
			messageRepository.save(mm);
			dialogRepository.setUnreadCountToZero(mm.getDialogId());
		}

		return listOfMsg;
	}

	@Override
	@Transactional
	public List<MessageInlineDto> getUnreadMessagesListForThisMan(Long userId) {
		List<MessageInlineDto> listOfMsg = new ArrayList<>();

		Optional<List<DialogModel>> dialogsForThisMan = Optional.ofNullable(dialogRepository.findByConversationAuthorAndUnreadCountNot(customMapper.getAuthorModelFromId(userId), 0));
		if (dialogsForThisMan.isEmpty()) {
			return listOfMsg;
		}

		for (DialogModel dm : dialogsForThisMan.get()){
			if (dm.getUnreadCount() == 0) {
				break;
			}
			Optional<List<MessageModel>> modelList = messageRepository.findByDialogIdAndStatusAndAuthor_Id(dm.getId(), EMessageStatus.SENT, dm.getConversationPartner().getId());
			if (modelList.isPresent()) {

				for (MessageModel mm : modelList.get()) {

					MessageInlineDto m = MessageInlineDto.builder()
							.text(mm.getMessageText())
							.timestamp(mm.getTime())
							.author(mm.getAuthor().getFirstName() + " " + mm.getAuthor().getLastName())
							.authorId(mm.getAuthor().getId())
							.build();
					listOfMsg.add(m);
					mm.setStatus(EMessageStatus.READ);
					messageRepository.save(mm);
					dialogRepository.setUnreadCountToZero(mm.getDialogId());
				}
			}
		}
		return listOfMsg;
	}

	@NotNull
	private Optional<DialogModel> chooseBetweenTwoDialogs(Long userId, Long companionId) {
		Optional<DialogModel> dialogModel;
		dialogModel = Optional.ofNullable(
				dialogRepository.findByConversationAuthorAndConversationPartner(
						customMapper.getAuthorModelFromId(userId),
						customMapper.getAuthorModelFromId(companionId)));

		if (dialogModel.isEmpty()) {
			dialogModel = Optional.ofNullable(
					dialogRepository.findByConversationAuthorAndConversationPartner(
							customMapper.getAuthorModelFromId(companionId),
							customMapper.getAuthorModelFromId(userId)));
		}
		return dialogModel;
	}

	public void setUserId(Long userId) {
		MessageServiceImpl.userId = userId;
	}
}
