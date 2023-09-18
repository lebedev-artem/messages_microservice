package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.MessageService;

import javax.transaction.Transactional;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:14
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final ModelMapper modelMapper;
	private final MessageRepository messageRepository;

	@Override
	public Object createMessage(MessageDto messageDto) {

		MessageModel mm = modelMapper.map(messageDto, MessageModel.class);
		try {
			messageRepository.save(mm);
		}
		catch (RuntimeException e) {
			new ResponseEntity<>(
					new ErrorResponse("Error while creating or saving message", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(modelMapper.map(mm, MessageDto.class), HttpStatus.OK);
	}
}
