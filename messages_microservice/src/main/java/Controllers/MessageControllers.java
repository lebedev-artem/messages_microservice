package Controllers;


import dto.DialogDto;
import dto.MessageDto;
import dto.UnreadCountDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.DialogService;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.io.IOException;


@RestController
@RequestMapping("/api/v1/dialogs")

public class MessageControllers {


    private DialogService dialogService;


    //Обновление статуса сообщений
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @PutMapping(path ="/{dialogId}")
    public ResponseEntity<DialogService> dialogID(@RequestBody DialogService dialogService) throws IOException {

        return ResponseEntity.ok(dialogService);
    }

    //Создание сообщения, для тестирования
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @PostMapping(path ="/createMessage", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<MessageDto> createMessage
    (@Valid
     @Parameter(
             in = ParameterIn.DEFAULT,
             description = "",
             required = true,
             schema = @Schema())
     @RequestBody MessageDto body) throws IOException {

        return ResponseEntity.ok(body);
    }


    //Создание диалога, для тестирования
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @PostMapping(path ="/createDialog", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<DialogDto> createDialog
    (@Valid
     @Parameter(
             in = ParameterIn.DEFAULT,
             description = "",
             required = true,
             schema = @Schema())
     @RequestBody DialogDto body) throws IOException {

        return ResponseEntity.ok(body);
    }

//Получение списка диалогов

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @GetMapping(value = "/dialogs", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity dialogs(@RequestParam(required = false) Pageable pageable) {
        return ResponseEntity.ok(pageable);
    }



//Получение количества непрочитанных сообщений

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @GetMapping(value = "/dialogs/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity unread(@RequestParam(required = false) UnreadCountDto quantity) {
        return ResponseEntity.ok(quantity);
    }

//Получение(создание) диалога между пользователями

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @GetMapping(value = "/dialogs/recipientId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity recipientId(@RequestParam(required = false) String id) {

        return ResponseEntity.ok(id);
    }


    //Получение сообщений сообщений диалога

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")})

    @GetMapping(value = "/dialogs/messages")
    public ResponseEntity<String> messages(@RequestParam(required = false)  String recipientid, Pageable pageable) {
        return ResponseEntity.ok(recipientid); //????????
    }



}
