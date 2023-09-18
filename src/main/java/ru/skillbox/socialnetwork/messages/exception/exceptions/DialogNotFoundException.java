package ru.skillbox.socialnetwork.messages.exception.exceptions;

/**
 * @author Artem Lebedev | 18/09/2023 - 08:24
 */
public class DialogNotFoundException extends RuntimeException{
	public DialogNotFoundException(String message) {
		super(message);
	}
}
