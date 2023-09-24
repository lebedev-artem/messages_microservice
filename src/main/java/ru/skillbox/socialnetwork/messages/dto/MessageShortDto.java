package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageShortDto {

    @JsonIgnore
	private UUID id;
	private Boolean isDeleted;
	private Timestamp time;
	private AuthorDto conversationAuthor;
	private AuthorDto conversationPartner;
	private String messageText;
}
