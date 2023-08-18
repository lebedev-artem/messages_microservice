package services;

import dto.DialogDto;
import models.Dialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repository.DialogRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class DialogServiceImp implements DialogService{

    @Autowired
    private DialogRepository dialogRepository;
    @Override
    public ResponseEntity<DialogDto> gedDialogByID(UUID id) {
        Optional<Dialog> dialogOptional = dialogRepository.findById(id);
        if(dialogOptional.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DialogDto dialogDto = new DialogDto();
        dialogDto.setId(dialogOptional.get().getId().toString());
        dialogDto.setConversationPartner1(dialogOptional.get().getConversationPartner1().toString());
        dialogDto.setConversationPartner2(dialogOptional.get().getConversationPartner2().toString());
        dialogDto.setDeleted(dialogOptional.get().isDeleted());
        dialogDto.setUnreadCount(dialogOptional.get().getUnreadCount());
        return new ResponseEntity<>(dialogDto, HttpStatus.OK);
    }
}
