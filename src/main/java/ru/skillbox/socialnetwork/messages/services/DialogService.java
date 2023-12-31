package ru.skillbox.socialnetwork.messages.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.models.MessageModel;

import java.util.List;
import java.util.UUID;

@Service
public interface DialogService {

    Object createDialog(DialogDto dialogDto);
    Object createDialogWithThisMan(@NotNull Long authorId, Long partnerId);
    Object getDialogsPage(Pageable pageable);
    Object getUnreadCount();
//    Object getDialogOrCreate(UUID id, Long conversationPartner1, Long conversationPartner2);
    void  setLastMessage(UUID dialogId, MessageModel message);
    Object delDialogWithThisMan(Long id);

//    for bot
    List<DialogDto> getDialogsList(Long id);
    Integer getMessagesCountForDialog(UUID dialogId);

    void deleteThisDialog(UUID dialogId);

}
