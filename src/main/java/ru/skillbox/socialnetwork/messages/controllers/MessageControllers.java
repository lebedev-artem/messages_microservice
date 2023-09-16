package ru.skillbox.socialnetwork.messages.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.skillbox.socialnetwork.messages.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socialnetwork.messages.services.DialogService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/dialogs")
@SecurityRequirement(name = "JWT")
@Tag(name = "Message service", description = "Сервис друзей")
public class MessageControllers {

    private DialogService dialogService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Обновление статуса сообщений")
    @PutMapping(path = "/{dialogId}")
    public void dialogID(@PathVariable(value = "dialogId") UUID dialogId) {
//        dialogService.gedDialogByID(dialogId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Создание сообщения, для тестирования")
    @PostMapping(path = "/createMessage", produces = {"application/json"}, consumes = {"application/json"})
    public MessageDto createMessage
            (@Valid
             @Parameter(
                     in = ParameterIn.DEFAULT,
                     description = "",
                     required = true,
                     schema = @Schema())
             @RequestBody MessageDto messageDto) {
//        dialogService
        return MessageDto.builder()
                .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .isDeleted(true)
                .time(LocalDateTime.now())
                .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .messageText("string")
                .readStatus("string")
                .dialogId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Создание диалога, для тестирования")
    @PostMapping(path = "/createDialog", produces = {"application/json"}, consumes = {"application/json"})
    public DialogDto createDialog
            (@Valid
             @Parameter(
                     in = ParameterIn.DEFAULT,
                     description = "",
                     required = true,
                     schema = @Schema())
             @RequestBody DialogDto dialogDto) {

        return DialogDto.builder()
                .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .isDeleted(true)
                .unreadCount(0)
                .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .lastMessage(MessageDto.builder()
                        .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .isDeleted(true)
                        .time(LocalDateTime.now())
                        .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .messageText("string")
                        .readStatus("string")
                        .dialogId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .build())
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение списка диалогов")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDialogDto pageable
            (@RequestParam(required = false) Pageable pageable) {
        return PageDialogDto.builder()
                .totalPages(0)
                .sort(Sort.builder()
                        .unsorted(true)
                        .sorted(true)
                        .empty(true)
                        .build())
                .numberOfElements(0)
                .pagable(PageableObject.builder()
                        .sort(Sort.builder()
                                .sorted(true)
                                .unsorted(true)
                                .empty(true)
                                .build())
                        .paged(true)
                        .unpaged(true)
                        .offset(0)
                        .pageNumber(0)
                        .pageSize(0)
                        .build())
                .first(true)
                .last(true)
                .size(0)
                .content(DialogDto.builder()
                        .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .isDeleted(true)
                        .unreadCount(0)
                        .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .lastMessage(MessageDto.builder()
                                .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                                .isDeleted(true)
                                .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa")
                                .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa")
                                .messageText("string")
                                .readStatus("string")
                                .dialogId("3fa85f64-5717-4562-b3fc-2c963f66afa")
                                .build())
                        .build())
                .number(0)
                .empty(true)
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение количества непрочитанных сообщений")
    @GetMapping(value = "/unreaded", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnreadCountDto unread() {
        UnreadCountDto unreadCountDto = new UnreadCountDto();
        unreadCountDto.setCount(12);
        return unreadCountDto;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение(создание) диалога между пользователями")
    @GetMapping(value = "/recipientId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DialogDto recipientId(@RequestParam(required = false) String id) {

        return DialogDto.builder()
                .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .isDeleted(true)
                .unreadCount(0)
                .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                .lastMessage(MessageDto.builder()
                        .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .isDeleted(true)
                        .time(LocalDateTime.now())
                        .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .messageText("string")
                        .readStatus("string")
                        .dialogId("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .build())
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @Operation(summary = "Получение сообщений сообщений диалога")
    @GetMapping(value = "/messages")
    public PageMessageShortDto messages(@RequestParam(required = false) String recipientid, Pageable pageable) {
        return PageMessageShortDto.builder()
                .totalPages(0)
                .totalElements(0)
                .sort(Sort.builder()
                        .empty(true)
                        .sorted(true)
                        .unsorted(true)
                        .build())
                .numberOfElements(0)
                .pageable(PageableObject.builder()
                        .sort(Sort.builder()
                                .empty(true)
                                .sorted(true)
                                .unsorted(true)
                                .build())
                        .unpaged(true)
                        .paged(true)
                        .pageSize(0)
                        .pageNumber(0)
                        .offset(0)
                        .build())
                .first(true)
                .last(true)
                .size(0)
                .content(MessageShortDto.builder()
                        .id("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .isDeleted(true)
                        .time(LocalDateTime.now())
                        .conversationPartner1("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .conversationPartner2("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .messageText("string")
                        .build())
                .build();
    }
}
