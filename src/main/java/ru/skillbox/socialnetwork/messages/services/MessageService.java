package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.dto.telegram.MessageInlineDto;
import ru.skillbox.socialnetwork.messages.dto.telegram.MessageTgDto;

import java.util.List;
import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:00
 */
@Service
public interface MessageService {
	Object createMessage(MessageDto messageDto);
	Object saveMessage(MessageTgDto messageDto);
	Object changeMessageStatus(Long userId);
	Object getMessagesForDialog(Long companionId, Pageable pageable);
	List<MessageInlineDto> getMessagesListForDialog(UUID dialogId);
	List<MessageInlineDto> getUnreadMessagesListForThisMan(Long userId);
}
