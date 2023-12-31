package ru.skillbox.socialnetwork.messages.dto.kafka;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class JsonMessage {

	private long number;
	private String message;
	private Date timestamp;
}

