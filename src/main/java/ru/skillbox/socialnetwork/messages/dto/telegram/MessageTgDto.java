package ru.skillbox.socialnetwork.messages.dto.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import ru.skillbox.socialnetwork.messages.dto.AuthorDto;
import ru.skillbox.socialnetwork.messages.dto.EMessageStatus;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * 	Timestamp time;<p>
 * 	Long authorId;<p>
 * 	String messageText;<p>
 */

@Data
@Schema
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MessageTgDto {

	private Timestamp time;
	private Long author;
	private Long partner;
	private String messageText;
	private String status;
	@Nullable
	private UUID dialogId;

}
