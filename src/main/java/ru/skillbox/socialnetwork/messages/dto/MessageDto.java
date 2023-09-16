package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @NonNull
    String id;

    boolean isDeleted;

    //Дата и время отправки
    LocalDateTime time;

    //UUID первого собеседника
    @NonNull
    String conversationPartner1;

    //UUID второго собеседника
    @NonNull
    String conversationPartner2;

    //Текст сообщения
    String messageText;

    //Статус прочтения: SENT, READ - отправлен, прочитан
    String readStatus;

    // UUID диалога
    String dialogId;
}
