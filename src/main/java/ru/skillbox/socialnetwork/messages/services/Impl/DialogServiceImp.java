package ru.skillbox.socialnetwork.messages.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.messages.client.UsersClient;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;
import ru.skillbox.socialnetwork.messages.dto.*;
import ru.skillbox.socialnetwork.messages.exception.ErrorResponse;
import ru.skillbox.socialnetwork.messages.exception.exceptions.UserPrincipalsNotFound;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.repository.AuthorRepository;
import ru.skillbox.socialnetwork.messages.repository.DialogRepository;
import ru.skillbox.socialnetwork.messages.repository.MessageRepository;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

import static ru.skillbox.socialnetwork.messages.security.service.UserDetailsServiceImpl.getPrincipalId;

@Slf4j
@Service
@RequiredArgsConstructor
public class DialogServiceImp implements DialogService {
    private final ModelMapper modelMapper;
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private final UsersClient usersClient;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Object getDialogOrCreate(@NotNull DialogDto dialogDto) {
        AuthorModel am = new AuthorModel();
        MessageModel mm = new MessageModel();
        DialogModel dm = new DialogModel();
        AccountDto partnerPrincipal;

        if (dialogRepository.existsByConversationAuthorAndConversationPartner
                (getPrincipalId(), dialogDto.getConversationPartner().getId())) {
            return new ResponseEntity<>(new ErrorResponse("Dialog already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } else if (dialogDto.getConversationPartner().getId() != null) {
            try {
                partnerPrincipal = usersClient.getUserDetailsById(dialogDto.getConversationPartner().getId());
            } catch (RuntimeException e) {
                throw new UserPrincipalsNotFound("User with id " + dialogDto.getConversationPartner().getId() + " does not exist");
            }
            Optional<AuthorModel> amO = Optional.ofNullable(authorRepository.findById(partnerPrincipal.getId()).orElse(AuthorModel.builder()
                    .id(partnerPrincipal.getId())
                    .firstName(partnerPrincipal.getFirstName())
                    .lastName(partnerPrincipal.getLastName())
                    .photo(partnerPrincipal.getPhoto())
                    .build()));
            if (amO.isPresent()) {
                am = amO.get();
            }
            mm = MessageModel.builder()
                    .isDeleted(false)
                    .time(new Timestamp(System.currentTimeMillis()))
                    .author(am)
                    .messageText("")
                    .status(EMessageStatus.SENT)
                    .dialogId(null) // for test
                    .build();
            dm = DialogModel.builder()
                    .isDeleted(false)
                    .unreadCount(0)
                    .conversationAuthor(getPrincipalId())
                    .conversationPartner(dialogDto.getConversationPartner().getId())
                    .lastMessage(mm)
                    .build();
            dialogRepository.save(dm);
            mm.setDialogId(dm.getId());
        }
        log.info(" * Dialog {} saved", dm.getId());
        DialogDto dd = DialogDto.builder()
                .unreadCount(dm.getUnreadCount())
                .conversationPartner(modelMapper.map(am, AuthorDto.class))
                .lastMessage(modelMapper.map(mm, MessageDto.class))
                .build();

        return new ResponseEntity<>(dd, HttpStatus.OK);
    }

    @Nullable
    private AuthorDto getAuthorFromId(Long id) {
        Optional<AuthorModel> amO =
                Optional.ofNullable(
                        authorRepository.findById(id).orElseThrow(
                                () -> new UserPrincipalsNotFound("User with id " + id + " does not exist")));
        AuthorModel am;
        if (amO.isEmpty()) {
            log.error(" * User with id " + id + " does not exist");
            throw new UserPrincipalsNotFound("User with id " + id + " does not exist");
        }
        am = amO.get();
        return modelMapper.map(am, AuthorDto.class);
    }


    @Override
    public Object getDialogs(Pageable pageable) {
        Long conversationAuthor = getPrincipalId();
        Set<DialogModel> dmSet = dialogRepository.findByConversationAuthor(conversationAuthor);
        List<DialogDto> ddSet = new ArrayList<>();
        for (DialogModel dm : dmSet) {
            Optional<MessageModel> mm = messageRepository.findById(dm.getLastMessage().getId());
            DialogDto dd = DialogDto.builder()
                    .unreadCount(dm.getUnreadCount())
                    .conversationPartner(getAuthorFromId(dm.getConversationPartner()))
                    .lastMessage(new MessageDto(mm.get().getTime(), mm.get().getAuthor().getId(), mm.get().getMessageText())).build();
            ddSet.add(dd);
        }
        Page<DialogDto> p = new PageImpl<>(ddSet, pageable, ddSet.size());

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @Override
    public Object getUnreadCount() {
        Optional<Object> list = Optional.ofNullable(dialogRepository.findByConversationAuthor(getPrincipalId()));
        if (list.isPresent()) {
            List<DialogModel> dialogModelList = (List<DialogModel>) list.get();
            int unreadCount = dialogModelList.stream().mapToInt(DialogModel::getUnreadCount).sum();
            UnreadCountDto unreadCountDto = new UnreadCountDto(unreadCount);
            return new ResponseEntity<>(unreadCountDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Dialogs for user " + getPrincipalId() + " not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public DialogDto getDialogOrCreate(String id, Long conversationPartner1, Long conversationPartner2) {
        if (id == null) {
            return createDialog(conversationPartner1, conversationPartner2);
        }
        Optional<DialogModel> dialogModel = dialogRepository.findById(UUID.fromString(id));
        return objectMapper.convertValue(dialogModel.orElseThrow(), DialogDto.class);
    }

    private DialogDto createDialog(Long conversationAuthor, Long conversationPartner) {
        DialogModel dialogModel = DialogModel.builder()
                .isDeleted(false)
                .conversationAuthor(conversationAuthor)
                .conversationPartner(conversationPartner)
                .unreadCount(0)
                .lastMessage(new MessageModel())
                .build();
        dialogRepository.save(dialogModel);
        return objectMapper.convertValue(dialogModel, DialogDto.class);
    }
}
