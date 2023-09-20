package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bouncycastle.util.Times;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.sql.Timestamp;


@Data
@Schema
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MessageDto {
	@Nullable
	private Timestamp time;
	private Long authorId;
	private String messageText;


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
