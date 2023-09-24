package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import java.util.UUID;

@Service
public interface DialogService {

    Object createDialog(DialogDto dialogDto);
    Object getDialogsList(Pageable pageable);
    Object getUnreadCount();
    void  setLastMessage(UUID dialogId, MessageModel message);

    void delDialog(UUID dialogId);

}
