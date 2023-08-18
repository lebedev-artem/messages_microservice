package dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class MessageShortDto {


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


}
