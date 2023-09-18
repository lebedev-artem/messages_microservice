package ru.skillbox.socialnetwork.messages.services.Impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.dto.DialogDto;
import ru.skillbox.socialnetwork.messages.dto.MessageDto;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.exception.exceptions.DialogNotFoundException;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DialogServiceImp implements DialogService {
    private final ModelMapper modelMapper;
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;


    @Override
    @Transactional
    public Object createDialog(@NotNull DialogDto dialogDto) {


//        TODO
//        Надо подумать как десериализовывтаь UUID
        Optional<MessageModel> lastMessage = Optional.ofNullable(messageRepository
                .findByDialogId(UUID.fromString(dialogDto.getId()))
                .orElseThrow(() -> new DialogNotFoundException("Dialog with id " + dialogDto.getId() + " not found")));

        MessageDto lastMessageDto = modelMapper.map(lastMessage, MessageDto.class);
        dialogDto.setLastMessage(lastMessageDto);
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
