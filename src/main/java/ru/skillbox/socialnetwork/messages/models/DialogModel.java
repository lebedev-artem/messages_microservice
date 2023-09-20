package ru.skillbox.socialnetwork.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;

import javax.persistence.*;
import java.util.UUID;

/**
 *UUID id; <p>
 *Boolean isDeleted;<p>
 *Integer unreadCount;<p>
 *Long conversationAuthor;<p>
 *Long conversationPartner;<p>
 *MessageModel lastMessage;<p>
 */

@Entity
@Data
@Builder
@Table(name="dialogs")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DialogModel {

    @Id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @JsonIgnore
    private UUID id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "unread_count", nullable = false)
    private Integer unreadCount;

    @Column(name = "conversation_author", nullable = false, columnDefinition = "LONG")
    private Long conversationAuthor;

    @Column(name = "conversation_partner", nullable = false, columnDefinition = "LONG")
    private Long conversationPartner;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "last_message")
    private MessageModel lastMessage;

}
