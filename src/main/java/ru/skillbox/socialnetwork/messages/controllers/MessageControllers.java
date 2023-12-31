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
import org.springframework.security.access.prepost.PreAuthorize;
import ru.skillbox.socialnetwork.messages.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socialnetwork.messages.dto.telegram.MessageTgDto;
import ru.skillbox.socialnetwork.messages.services.DialogService;
import ru.skillbox.socialnetwork.messages.services.MessageService;

import javax.validation.Valid;
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
	public Object changeMessageStatus(@PathVariable(value = "userId") Long partnerId) {
		return messageService.changeMessageStatus(partnerId);
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
		log.debug(" * GET \"/createMessage\"");
		log.debug(" * Payload: \n{}", messageDto);
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
		log.debug(" * GET \"/createDialog\"");
		log.debug(" * Payload: \n{}", dialogDto);
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
		log.debug(" * GET \"/\"");
		log.debug(" * Payload: page?{}, size?{}, sort?{}", page, size, sort);
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
		return dialogService.getDialogsPage(pageable);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad request")})
	@Operation(summary = "Получение количества непрочитанных сообщений")
	@GetMapping(value = "/unread", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object unread() {
		log.debug(" * GET \"/unread\"");
		return dialogService.getUnreadCount();
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad request")})
	@Operation(summary = "Получение сообщений сообщений диалога")
	@GetMapping(value = "/messages")
	public Object messages(@RequestParam(name = "companionId") Long companionId,
	                          @RequestParam(defaultValue = "0") Integer page,
	                          @RequestParam(defaultValue = "1") Integer size) {
		log.debug(" * GET \"/messages\"");
		log.debug(" * Payload: companionId={}, offset={}, limit={}", companionId, page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.by("time").ascending());
		return messageService.getMessagesForDialog(companionId, pageable);
	}

	//  BOT
	@GetMapping(value = "/messages/thisdialog/{dialogId}")
	public Object getMessages(@PathVariable(name = "dialogId") UUID dialogId) {
		log.debug(" * GET \"/messages/thisdialog/{dialogId}\"");
		return messageService.getMessagesListForDialog(dialogId);
	}

	//	BOT
	@GetMapping(value = "/messages/unread/thisman/{userId}")
	public Object getUnreadMessages(@PathVariable(name = "userId") Long userId) {
		log.debug(" * GET \"/messages/unread/thisman/{userId}\"");
		return messageService.getUnreadMessagesListForThisMan(userId);
	}

	//	BOT
	@PostMapping(path = "/saveMessage", produces = {"application/json"})
	public Object saveMessage(@RequestBody MessageTgDto messageDto) {
		log.debug(" * GET \"/saveMessage\"");
		log.debug(" * Payload: \n{}", messageDto);
		return messageService.saveMessage(messageDto);
	}

	//	BOT
	@PostMapping(path = "/createDialogWithThisMan", produces = {"application/json"})
	public Object createDialogWithThisMan(@RequestParam Long authorId, @RequestParam Long partnerId) {
		log.debug(" * GET \"/createDialogFWithThisMan\"");
		log.debug(" * Payload: authorId - {}, partnerId - {}", authorId, partnerId);
		return dialogService.createDialogWithThisMan(authorId, partnerId);
	}

	//	BOT
	@GetMapping(value = "/thisman/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getDialogsList(@PathVariable Long id) {
		log.debug(" * GET \"/thisman/{id}\"");
		return dialogService.getDialogsList(id);
	}

	@GetMapping(value = "/thisdialog/{dialogId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getMessageCountForDialog(@PathVariable UUID dialogId) {
		log.debug(" * GET \"/thisdialog/{dialogId}/count\"");
		return dialogService.getMessagesCountForDialog(dialogId);
	}

	@DeleteMapping(value = "/deleteDialogWithThisMan/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object delDialog(@PathVariable Long id) {
		return 	dialogService.delDialogWithThisMan(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/thisdialog/{dialogId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteThisDialog(@PathVariable UUID dialogId) {
		log.debug(" * DELETE \"/thisdialog/{dialogId}/delete\"");
		dialogService.deleteThisDialog(dialogId);
	}


}
