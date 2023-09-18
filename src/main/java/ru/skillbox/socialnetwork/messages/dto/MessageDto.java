package ru.skillbox.socialnetwork.messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private String id;
    private Boolean isDeleted = false;
    //Дата и время отправки
    private Timestamp time = new Timestamp(System.currentTimeMillis());
    //id первого собеседника
    private Long conversationPartner1;
    //id второго собеседника
    private Long conversationPartner2;
    //Текст сообщения
    private String messageText;
    //Статус прочтения: SENT, READ - отправлен, прочитан
    private EMessageStatus status = EMessageStatus.SENT;
    // UUID диалога
    private UUID dialogId;

    public MessageDto(Long conversationPartner1, Long conversationPartner2, String messageText, EMessageStatus status, UUID dialogId) {
        this.isDeleted = false;
        this.time = new Timestamp(System.currentTimeMillis());
        this.conversationPartner1 = conversationPartner1;
        this.conversationPartner2 = conversationPartner2;
        this.messageText = messageText;
        this.status = status;
        this.dialogId = dialogId;
    }
}
