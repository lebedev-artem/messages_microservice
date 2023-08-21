package ru.skillbox.socialnetwork.messages.services;

import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DialogService {

    ResponseEntity<DialogDto> gedDialogByID (UUID id);

}
