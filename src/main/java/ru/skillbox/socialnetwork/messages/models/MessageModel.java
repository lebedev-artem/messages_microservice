package ru.skillbox.socialnetwork.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import ru.skillbox.socialnetwork.messages.dto.EMessageStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 17/09/2023 - 23:55
 */

@Entity
@Data
@Table(name="messages")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageModel {

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
	private UUID id;
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
	@Column(name = "time")
	private Timestamp time = new Timestamp(System.currentTimeMillis());
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
