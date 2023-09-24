package ru.skillbox.socialnetwork.messages.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.client.UsersClient;
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
	private final ObjectMapper objectMapper;
	private final AuthorRepository authorRepository;
	private final UsersClient usersClient;
	private final CustomMapper customMapper;
	private final DialogService dialogService;

	private static Long userId;

	@Override
	@Transactional
	public Object createMessage(MessageDto messageDto) {

		MessageModel mm = modelMapper.map(messageDto, MessageModel.class);
		Optional<AuthorModel> aum = Optional.of(authorRepository
				.findById(mm.getAuthor().getId())
				.orElse(customMapper.getAuthorModelFromId(mm.getAuthor().getId())));
		Optional<AuthorModel> pam = Optional.of(authorRepository
				.findById(mm.getPartner().getId())
				.orElse(customMapper.getAuthorModelFromId(mm.getPartner().getId())));

		MessageModel fmm = MessageModel.builder()
				.isDeleted(false)
				.time(mm.getTime() == null ? new Timestamp(System.currentTimeMillis()) : mm.getTime())
				.author(aum.get())
				.partner(pam.get())
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
	public Object changeMessageStatus(Long authorId) {
		Optional<List<MessageModel>> mmList =
				Optional.ofNullable(messageRepository
						.findByAuthorId(authorId)
						.orElseThrow(() -> new DialogNotFoundException("Dialog with author id " + authorId + " not found")));
		if (mmList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		mmList.get().forEach(m -> m.setStatus(EMessageStatus.READ));
		log.info(" * Status of messages for {} changed to READ", authorId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public Object getMessagesFromPartner(Long partnerId, Pageable pageable) {
		Optional<DialogModel> dialogModel = Optional.ofNullable(dialogRepository
				.findByConversationAuthorAndConversationPartner(customMapper.getAuthorModelFromId(userId), customMapper.getAuthorModelFromId(partnerId)));

		Optional<Page<MessageModel>> messageModels;
		AuthorModel aum = customMapper.getAuthorModelFromId(partnerId);

		if (dialogModel.isPresent()) {
			messageModels = messageRepository.findByAuthorAndDialogId(aum, dialogModel.get().getId(), pageable);
		} else
			return new ResponseEntity<>(new ErrorResponse("Dialog satisfying to conditions not found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

        MessageShortDto msd = new MessageShortDto();
		if (messageModels.isEmpty()) {
			return new ResponseEntity<>(new ErrorResponse("Messages not found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		msd = modelMapper.map(messageModels.get().getContent().get(0), MessageShortDto.class);

		return new ResponseEntity<>(msd, HttpStatus.OK);
	}

	public void setUserId(Long userId) {
		MessageServiceImpl.userId = userId;
	}
}
