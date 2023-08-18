package dto;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

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
