package ru.skillbox.socialnetwork.messages.services;

import ru.skillbox.socialnetwork.messages.dto.MessageDto;

import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
public interface MessageService {
	Object createMessage(MessageDto messageDto);
	Object changeMessageStatus(UUID dialogId);
}
