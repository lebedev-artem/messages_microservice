package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
@Service
public interface MessageService {
	Object createMessage(MessageDto messageDto);
	Object changeMessageStatus(Long userId);
	Object getMessagesFromPartner(Long companionId, Pageable pageable);
}
