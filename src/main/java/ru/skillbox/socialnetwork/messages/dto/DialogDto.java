package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogDto {

    private UUID id;
    private Boolean isDeleted;
    //Количество непрочитанных сообщений диалога
    private Integer unreadCount;
    //id первого собеседника
    private Long conversationPartner1;
    //id второго собеседника
    private Long conversationPartner2;
    //Последнее сообщение
    private MessageDto lastMessage;
}
