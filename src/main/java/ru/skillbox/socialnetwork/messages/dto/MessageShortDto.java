package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageShortDto {

    @NonNull
    String id;

    boolean isDeleted;

    //Дата и время отправки
    LocalDateTime time;

    //UUID первого собеседника
    @NonNull
    Long conversationPartner1;

    //UUID второго собеседника
    @NonNull
    Long conversationPartner2;

    //Текст сообщения
    String messageText;
}
