package ru.skillbox.socialnetwork.messages.dto;

import lombok.Getter;

import javax.persistence.GeneratedValue;

/**
 * @author Artem Lebedev | 17/09/2023 - 23:49
 */
@Getter
public enum EMessageStatus {
	SENT(1L),
	READ(2L);

	private final Long status;

	EMessageStatus(Long status){
		this.status = status;
	}
}
