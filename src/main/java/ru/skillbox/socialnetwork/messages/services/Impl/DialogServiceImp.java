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
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.dto.UnreadCountDto;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.exception.exceptions.DialogNotFoundException;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.security.service.UserDetailsServiceImpl;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static ru.skillbox.socialnetwork.messages.security.service.UserDetailsServiceImpl.getPrincipalId;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogServiceImp implements DialogService {
	private final ModelMapper modelMapper;
	private final DialogRepository dialogRepository;
	private final MessageRepository messageRepository;

	/**
	 * Думаю логика должны быть така. Создаем с минимальными параметрами диалог
	 * - isDelete = false
	 * - conversationPartner1 =
	 * - conversationPartner2 =
	 * - unreadCount = 0
	 *
	 * @param dialogDto
	 * @return
	 */


	@Override
	@Transactional
	public Object createDialog(@NotNull DialogDto dialogDto) {

//        Optional<MessageModel> lastMessage = Optional.ofNullable(messageRepository
//                .findByDialogId(UUID.fromString(dialogDto.getId().toString()))
//                .orElseThrow(() -> new DialogNotFoundException("Dialog with id " + dialogDto.getId() + " not found")));
//
//        MessageDto lastMessageDto = modelMapper.map(lastMessage, MessageDto.class);
//        dialogDto.setLastMessage(lastMessageDto);

		DialogModel dm = modelMapper.map(dialogDto, DialogModel.class);
		if (!dialogRepository.existsByConversationPartner1AndConversationPartner2(dialogDto.getConversationPartner1(), dialogDto.getConversationPartner2())) {
			dialogRepository.save(dm);
		} else {
			return new ResponseEntity<>(new ErrorResponse("Dialog already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(modelMapper.map(dm, MessageDto.class), HttpStatus.OK);
	}

	@Override
	public Object getDialogs(Pageable pageable) {
		Long conversationPartner1 = getPrincipalId();
		Page<DialogModel> dialogDtoPage = dialogRepository.findAllByConversationPartner1(conversationPartner1, pageable);

		return new ResponseEntity<>(dialogDtoPage, HttpStatus.OK);
	}

	@Override
	public Object getUnreadCount(UUID dialogId) {
		Optional<Integer> unreadCount = Optional.ofNullable(dialogRepository.findById(dialogId).get().getUnreadCount());
		if (unreadCount.isPresent()) {
			UnreadCountDto unreadCountDto = new UnreadCountDto(unreadCount.get());
			return new ResponseEntity<>(unreadCountDto, HttpStatus.OK);
		}

		return new ResponseEntity<>("Dialog with id " + dialogId + " not found", HttpStatus.BAD_REQUEST);
	}

}
