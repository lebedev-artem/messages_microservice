package ru.skillbox.socialnetwork.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;
import ru.skillbox.socialnetwork.messages.dto.EMessageStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 17/09/2023 - 23:55
 */

@Entity
@Getter
@Setter
@Table(name="messages")
@NoArgsConstructor
public class MessageModel {

	@Id
	@UuidGenerator
	private String id;
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@Column(name = "time")
	private Timestamp time;
	@Column(name = "conversation_partner_1")
	private Long conversationPartner1;
	@Column(name = "conversation_partner_2")
	private Long conversationPartner2;
	@Column(name = "message_text")
	private String messageText;
	@Column(name = "status")
	private EMessageStatus status;
	@Column(name = "dialog_id")
	private UUID dialogId;
}
