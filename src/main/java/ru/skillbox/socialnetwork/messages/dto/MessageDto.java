package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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
public class MessageDto {

	private Boolean isDeleted;
	private Timestamp time;
	private AuthorDto author;
//	private AuthorDto conversationPartner;
	private String messageText;
	private EMessageStatus status;
	private UUID dialogId;

	/**
	 *     "Последнее сообщение". Или просто сообщение
	 *     Было:
	 *     private UUID id;
	 *     private Boolean isDeleted = false;
	 *     private Timestamp time;
	 *     private Long conversationPartner1;
	 *     private Long conversationPartner2;
	 *     private String messageText;
	 *     private EMessageStatus status;
	 *     private UUID dialogId; (это поле возможно надо будет удалить, добавил чтоб делать выборку непрочитанных по айди диалога, но это кажется не нужно)
	 *     --------------------cut---------------------
	 *     Должно стать:
	 *      private UUID id;
	 *      private Boolean isDeleted;
	 *      private Timestamp time;
	 *      private Long Author author;
	 *      private String messageText;
	 *      private EMessageStatus status;
	 *      private UUID dialogId; (это поле возможно надо будет удалить, добавил чтоб делать выборку непрочитанных по айди диалога, но это кажется не нужно)
	 */

}
