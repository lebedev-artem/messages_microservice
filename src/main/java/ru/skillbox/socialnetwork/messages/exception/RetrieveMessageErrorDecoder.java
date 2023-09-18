package ru.skillbox.socialnetwork.messages.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.skillbox.socialnetwork.messages.exception.exceptions.*;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class RetrieveMessageErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(String methodKey, @NotNull Response response) {
		ExceptionMessage message;

		try (InputStream bodyIs = response.body().asInputStream()) {
			ObjectMapper mapper = new ObjectMapper();
			message = mapper.readValue(bodyIs, ExceptionMessage.class);
		} catch (IOException e) {
			return new Exception(e.getMessage());
		}
		log.info(" * Try decode feign exceptions message {} with method key {}", message, methodKey);
		switch (response.status()) {
			case 500: {
				return new ConnectException(message.getMessage() != null ? message.getMessage() : "Connection refused. Remote service unavailable");
			}
			case 409: {
				return new EmailNotUniqueException(message.getMessage() != null ? message.getMessage() : "Email not unique");
			}
			case 406: {
				return new EmailIsBlankException(message.getMessage() != null ? message.getMessage() : "Email cant be blank");
			}
			case 404: {
				return new EmailNotFoundException(message.getMessage() != null ? message.getMessage() : "User not found");
			}
			case 400: {
				return new BadRequestException(message.getMessage() != null ? message.getMessage() : "Bad request");
			}
			case 403: {
				return new ForbiddenException(message.getMessage() != null ? message.getMessage() : "Forbidden");
			}
			default: {
				log.warn(" * No case candidate for retrieve message. Will use full exceptions message");
				return errorDecoder.decode(methodKey, response);
			}
		}
	}
}

