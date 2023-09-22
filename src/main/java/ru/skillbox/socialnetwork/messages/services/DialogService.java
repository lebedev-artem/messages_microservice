package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;

@Service
public interface DialogService {

    Object getDialogOrCreate(DialogDto dialogDto);
    Object getDialogs(Pageable pageable);
    Object getUnreadCount();

    DialogDto getDialogOrCreate(String id, Long conversationPartner1, Long conversationPartner2);

}
