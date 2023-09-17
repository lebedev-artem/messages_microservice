package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class DialogServiceImp implements DialogService {
    private final ModelMapper modelMapper;
    private final DialogRepository dialogRepository;


    @Override
    @Transactional
    public Object createDialog(DialogDto dialogDto) {

        DialogModel dm = modelMapper.map(dialogDto, DialogModel.class);
        try {
            dialogRepository.save(dm);
        }
        catch (RuntimeException e) {
            new ResponseEntity<>(
                    new ErrorResponse("Error while creating or saving message", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(modelMapper.map(dm, MessageDto.class), HttpStatus.OK);
    }
}
