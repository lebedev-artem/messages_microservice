package ru.skillbox.socialnetwork.messages.exception.exceptions;

/**
 * @author Artem Lebedev | 29/08/2023 - 21:52
 */

public class RequestRejectedException extends RuntimeException{
	public RequestRejectedException(String message) {
		super(message);
	}
}
