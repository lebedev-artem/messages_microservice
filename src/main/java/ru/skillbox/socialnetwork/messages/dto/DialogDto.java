package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogDto {

    @NonNull
    String id;

    boolean isDeleted;

    //Количество непрочитанных сообщений диалога
    Integer unreadCount;

    //UUID первого собеседника
    @NonNull
    String conversationPartner1;

    //UUID второго собеседника
    @NonNull
    String conversationPartner2;

    MessageDto lastMessage;
}
