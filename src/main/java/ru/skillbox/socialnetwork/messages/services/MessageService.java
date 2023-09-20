package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.dto.PageMessageShortDto;

import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
public interface MessageService {
	Object createMessage(MessageDto messageDto);
	Object changeMessageStatus(UUID dialogId);

    PageMessageShortDto getMessagesForDialog(String recipientid, Pageable pageable);
}
