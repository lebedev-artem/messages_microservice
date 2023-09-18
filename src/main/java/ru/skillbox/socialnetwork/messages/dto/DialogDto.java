package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogDto {

    String id;
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
