package ru.skillbox.socialnetwork.messages.services;

import org.springframework.data.domain.Pageable;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DialogService {

    Object createDialog(DialogDto dialogDto);
    Object getDialogs(Pageable pageable);

}
