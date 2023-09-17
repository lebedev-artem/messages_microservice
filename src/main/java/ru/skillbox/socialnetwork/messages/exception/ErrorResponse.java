package ru.skillbox.socialnetwork.messages.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;

	@JsonProperty(value = "code")
	private int code;

	@JsonProperty(value = "status")
	private String status;

	@JsonProperty(value = "message")
	private String message;


	public ErrorResponse(String message, @NotNull HttpStatus httpStatus) {
		timestamp = new Date();
		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}
}
