package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Artem Lebedev | 25/09/2023 - 02:02
 */
@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageShortTestDto {

	UUID id;

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
