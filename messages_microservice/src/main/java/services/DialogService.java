package services;

import dto.DialogDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DialogService {

    ResponseEntity<DialogDto> gedDialogByID (UUID id);

}
