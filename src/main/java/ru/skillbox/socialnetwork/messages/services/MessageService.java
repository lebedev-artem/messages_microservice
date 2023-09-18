package ru.skillbox.socialnetwork.messages.services;

import ru.skillbox.socialnetwork.messages.dto.MessageDto;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
public interface MessageService {
	Object createMessage(MessageDto messageDto);
}
