package ru.skillbox.socialnetwork.messages.client.dto;

import lombok.Getter;

@Getter
public enum StatusCodeType {
	FRIEND(1L),
	REQUEST_TO(2L),
	REQUEST_FROM(3L),
	BLOCKED(4L),
	DECLINED(5L),
	SUBSCRIBED(6L),
	NONE(7L),
	WATCHING(8L),
	REJECTING(9L),
	RECOMMENDATION(10L);

	private final Long typeId;

	StatusCodeType(Long typeId) {
		this.typeId = typeId;
	}

}
