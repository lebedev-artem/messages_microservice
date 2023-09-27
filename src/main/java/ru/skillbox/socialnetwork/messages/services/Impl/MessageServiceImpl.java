package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.*;
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
import java.util.List;
import java.util.Optional;

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
		dialogService.setLastMessage(mm.getDialogId(), fmm);
		messageRepository.save(fmm);
		log.info(" * Message {} saved", fmm.getDialogId());
		return new ResponseEntity<>(modelMapper.map(fmm, MessageDto.class), HttpStatus.OK);
	}

	/*
	Tested
	 */
	@Override
	@Transactional
	public Object changeMessageStatus(Long partnerId) {
		Optional<DialogModel> dm;
		dm = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(
				customMapper.getAuthorModelFromId(userId),
				customMapper.getAuthorModelFromId(partnerId)));

		if (dm.isEmpty()) {
			dm = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(
					customMapper.getAuthorModelFromId(partnerId),
					customMapper.getAuthorModelFromId(userId)));
		}
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
	public Object getMessagesForDialog(Long partnerId, Pageable pageable) {
		Optional<DialogModel> dialogModel;
		dialogModel = Optional.ofNullable(
				dialogRepository.findByConversationAuthorAndConversationPartner(
						customMapper.getAuthorModelFromId(userId),
						customMapper.getAuthorModelFromId(partnerId)));

		if (dialogModel.isEmpty()) {
			dialogModel = Optional.ofNullable(
					dialogRepository.findByConversationAuthorAndConversationPartner(
							customMapper.getAuthorModelFromId(partnerId),
							customMapper.getAuthorModelFromId(userId)));
		}

		Optional<List<MessageModel>> messageList;
//		AuthorModel pam = customMapper.getAuthorModelFromId(partnerId);
		Page<MessageModel> mmListP = null;
		if (dialogModel.isPresent()) {
//			messageList = messageRepository.findByDialogId(dialogModel.get().getId());
//			messageList = messageRepository.findAllByConversationAuthorAndDialogId(pam, dialogModel.get().getId());
			mmListP = messageRepository.findByDialogId(dialogModel.get().getId(), Pageable.unpaged());
		} else {
			DialogDto dialogDto = DialogDto.builder()
					.conversationAuthor(AuthorDto.builder().id(userId).build())
					.conversationPartner(AuthorDto.builder().id(partnerId).build())
					.build();

			dialogService.createDialog(dialogDto);
			Optional<DialogModel> dm;
			dm = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(
					customMapper.getAuthorModelFromId(partnerId),
					customMapper.getAuthorModelFromId(userId)));
			if (dm.isEmpty()) {
				dm = Optional.ofNullable(dialogRepository.findByConversationAuthorAndConversationPartner(
						customMapper.getAuthorModelFromId(userId),
						customMapper.getAuthorModelFromId(partnerId)));
			}

			mmListP = messageRepository.findByDialogId(dm.get().getId(), Pageable.unpaged());
//			throw new DialogNotFoundException("Dialog satisfying to conditions not found");
		}

//		List<MessageShortTestDto> msdList = new ArrayList<>();

//		for (MessageModel mm : messageList.get()) {
//
//			msdList.add(new MessageShortTestDto(
//					mm.getId(),
//					mm.getTime().toLocalDateTime(),
//					mm.getAuthor().getId(),
//					partnerId,
//					mm.getMessageText()));
//		}

		return mmListP;
	}

	public void setUserId(Long userId) {
		MessageServiceImpl.userId = userId;
	}
}
