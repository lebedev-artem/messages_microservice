package ru.skillbox.socialnetwork.messages.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
	private Long id;
	private boolean isDeleted;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String roles;
	private String authority;
	private String phone;
	private String photo;
	private String profileCover;
	private String about;
	private String city;
	private String country;
	private String gender;
	private StatusCodeType statusCode;
	private LocalDateTime regDate;
	private LocalDateTime birthDate;
	private String messagePermission;
	private LocalDateTime lastOnlineTime;
	private boolean isOnline;
	private boolean isBlocked;
	private String emojiStatus;
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	private LocalDateTime deletionDate;

	public AccountDto(String firstName, String lastName, String email, String password, String roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
}
