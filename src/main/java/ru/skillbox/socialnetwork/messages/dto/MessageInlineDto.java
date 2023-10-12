package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
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
public class MessageInlineDto {

	//Дата и время отправки
	private Timestamp timestamp;
	@NonNull
	private String author;
	@NonNull
	private String text;

	@Override
	public String toString() {
		return  timestamp +	author + text + "\n";
	}
}
