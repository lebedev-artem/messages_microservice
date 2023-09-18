package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.exception.exceptions.DialogNotFoundException;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.MessageService;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:14
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final ModelMapper modelMapper;
	private final MessageRepository messageRepository;
	private final DialogRepository dialogRepository;

	/**
	 * Потом создаем сообщения к конкретному диалогу.
	 * Инкрементим unreadCount
	 */
	@Override
	public Object createMessage(MessageDto messageDto) {

		MessageModel mm = modelMapper.map(messageDto, MessageModel.class);
		Optional<DialogModel> dm = Optional.ofNullable(
				dialogRepository
						.findById(mm.getDialogId())
						.orElseThrow(() -> new DialogNotFoundException("Dialog with id " + mm.getDialogId() + " not found")));
		if (dm.isPresent()) {
			dm.get().setLastMessage(mm);
			dm.get().setUnreadCount(dm.get().getUnreadCount() + 1);
		}

		messageRepository.save(mm);

		return new ResponseEntity<>(modelMapper.map(mm, MessageDto.class), HttpStatus.OK);
	}
}
