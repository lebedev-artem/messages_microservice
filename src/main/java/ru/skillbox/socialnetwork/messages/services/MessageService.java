package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.dto.MessageShortDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
@Service
public interface MessageService {
	Object createMessage(MessageDto messageDto);
	Object changeMessageStatus(UUID dialogId);

	List<MessageShortDto> getMessagesForDialog(Long companionId, Integer offset, Integer limit);
}
