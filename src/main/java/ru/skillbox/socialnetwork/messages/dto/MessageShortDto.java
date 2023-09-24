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
	@NonNull
	private Timestamp time;
	@NonNull
	private AuthorDto author;
	@NonNull
	private AuthorDto partner;
	private String messageText;
}
