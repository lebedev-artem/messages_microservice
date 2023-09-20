package ru.skillbox.socialnetwork.messages.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Artem Lebedev | 20/09/2023 - 11:32 <p><p>
 *Long id;<p>
 *String firstName;<p>
 *String lastName;<p>
 *String photo;<p>
 */
@Data
@Entity(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "author")
public class AuthorModel {

	@Id
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "photo")
	private String photo;
}

