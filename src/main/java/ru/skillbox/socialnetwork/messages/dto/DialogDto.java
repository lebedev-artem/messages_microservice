package ru.skillbox.socialnetwork.messages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 *     private Integer unreadCount<p>
 *     private AuthorDTO conversationPartner<p>
 *     private MessageDto lastMessage<p>
 *
 */

@Data
@Schema
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialogDto {

    private Boolean isDeleted;
    private Integer unreadCount;
    private AuthorDto conversationAuthor;
    private AuthorDto conversationPartner;
    @Nullable
    private MessageDto lastMessage;
}
