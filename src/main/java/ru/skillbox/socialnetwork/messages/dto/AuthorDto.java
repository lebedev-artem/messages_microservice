package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Lebedev | 20/09/2023 - 11:42 <p><p>
 * Заполняется на основе данных из базы users<p>
 * id<p>
 * firstName<p>
 * lastName<p>
 * photo<p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDto {

	private Long id;
	@Nullable
	private String firstName;
	@Nullable
	private String lastName;
	@Nullable
	private String photo;

	@Override
	public String toString() {
		return "\nAuthor" +
				"\n{" +
				"\n id             :'" + id + '\'' +
				"\n First name     :'" + firstName + '\'' +
				"\n Last name      :'" + lastName + '\'' +
				"\n Photo          :'" + photo + '\'' +
				"\n}";
	}
}
