package ru.skillbox.socialnetwork.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @author Artem Lebedev | 17/09/2023 - 23:55 <p><p>
 *UUID id;<p>
 *Boolean isDeleted;<p>
 *Timestamp time;<p>
 *AuthorModel author;<p>
 *String messageText;<p>
 *EMessageStatus status;<p>
 *UUID dialogId;<p>
 */

@Entity
@Data
@Table(name="messages")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageModel {

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
	private UUID id;
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@Column(name = "time")
	private Timestamp time;
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "author_id")
	private AuthorModel author;
	@Column(name = "message_text")
	private String messageText;
	@Column(name = "status")
	private EMessageStatus status;
	@Column(name = "dialog_id")
	private UUID dialogId;

}
