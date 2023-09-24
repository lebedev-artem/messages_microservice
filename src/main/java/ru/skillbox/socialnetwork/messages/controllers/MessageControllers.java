package ru.skillbox.socialnetwork.messages.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.socialnetwork.messages.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socialnetwork.messages.models.MessageModel;
import ru.skillbox.socialnetwork.messages.services.DialogService;
import ru.skillbox.socialnetwork.messages.services.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/dialogs")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@Tag(name = "Message service", description = "Сервис сообщений")
public class MessageControllers {

    private final DialogService dialogService;
    private final MessageService messageService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Обновление статуса сообщений")
    @PutMapping(path = "/{userId}")
    public Object changeMessageStatus(@PathVariable(value = "userId") Long userId) {
        return messageService.changeMessageStatus(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Создание сообщения, для тестирования")
    @PostMapping(path = "/createMessage", produces = {"application/json"}, consumes = {"application/json"})
    public Object createMessage
            (@Valid
             @Parameter(
                     in = ParameterIn.DEFAULT,
                     description = "",
                     required = true,
                     schema = @Schema())
             @RequestBody MessageDto messageDto) {
        log.info(" * GET \"/createMessage\"");
        log.info(" * Payload: \n{}", messageDto);
        return messageService.createMessage(messageDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Создание диалога, для тестирования")
    @PostMapping(path = "/createDialog", produces = {"application/json"}, consumes = {"application/json"})
    public Object createDialog
            (@Valid @Parameter(
                    in = ParameterIn.DEFAULT,
                    description = "",
                    required = true,
                    schema = @Schema())
             @RequestBody DialogDto dialogDto) {
        log.info(" * GET \"/createDialog\"");
        log.info(" * Payload: \n{}", dialogDto);
        return dialogService.createDialog(dialogDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение списка диалогов")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDialogs
            (@RequestParam(required = false, defaultValue = "0") Integer page,
             @RequestParam(required = false, defaultValue = "1") Integer size,
             @RequestParam(required = false, defaultValue = "unreadCount") @Nullable String sort) {
        log.info(" * GET \"/\"");
        log.info(" * Payload: page?{}, size?{}, sort?{}", page, size, sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return dialogService.getDialogsList(pageable);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение количества непрочитанных сообщений")
    @GetMapping(value = "/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object unread() {
        log.info(" * GET \"/unread\"");
        return dialogService.getUnreadCount();
    }

//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful operation"),
//            @ApiResponse(responseCode = "400", description = "Bad request")})
//    @Operation(summary = "Получение сообщений сообщений диалога")
//    @GetMapping(value = "/messages")
//    public Object messages(@RequestParam(name = "companionId") Long companionId,
//                                       @RequestParam(defaultValue = "0") Integer page,
//                                       @RequestParam(defaultValue = "1") Integer size) {
//        log.info(" * GET \"/messages\"");
//        log.info(" * Payload: companionId={}, offset={}, limit={}", companionId, page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by("time").ascending());
//        return messageService.getMessagesFromPartner(companionId, pageable);
//    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение сообщений сообщений диалога")
    @GetMapping(value = "/messages")
    public Object messages(@RequestParam(name = "companionId") Long companionId,
                           @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "1") Integer size) {
        log.info(" * GET \"/messages\"");
        log.info(" * Payload: companionId={}, offset={}, limit={}", companionId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").ascending());
        return messageService.getMessagesFromPartner(companionId, pageable);
    }
    @DeleteMapping(value = "/delDialogById")
    public void delDialog(@RequestParam UUID dialogId) {
        dialogService.delDialog(dialogId);
    }
}
