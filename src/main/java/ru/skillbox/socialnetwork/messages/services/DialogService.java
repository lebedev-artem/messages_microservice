package ru.skillbox.socialnetwork.messages.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;

import java.util.List;
import java.util.UUID;

@Service
public interface DialogService {

    Object createDialog(DialogDto dialogDto);
    Object createDialogForBot(@NotNull Long authorId, Long partnerId);
    Object getDialogsPage(Pageable pageable);
    Object getUnreadCount();
//    Object getDialogOrCreate(UUID id, Long conversationPartner1, Long conversationPartner2);
    void  setLastMessage(UUID dialogId, MessageModel message);
    void delDialog(UUID dialogId);

//    for bot
    List<DialogDto> getDialogsList(Long id);

}
