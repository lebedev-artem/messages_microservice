package ru.skillbox.socialnetwork.messages.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Artem Lebedev | 11/09/2023 - 22:37
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

	private String firstName;
	private String lastName;
	private String photo;

}
