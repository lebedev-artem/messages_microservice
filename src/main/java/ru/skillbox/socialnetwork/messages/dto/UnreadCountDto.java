package ru.skillbox.socialnetwork.messages.dto;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCountDto {

    // Количество непрочитанных сообщений в диалоге
    private Integer count;

}
