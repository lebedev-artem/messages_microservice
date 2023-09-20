package ru.skillbox.socialnetwork.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name="dialogs")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DialogModel {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID id;

    @Column(name = "`is_deleted`", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "`unread_count`", nullable = false)
    private int unreadCount;

    @Column(name = "`conversation_partner_1`", nullable = false)
    private Long conversationPartner1;

    @Column(name = "`conversation_partner_2`", nullable = false)
    private Long conversationPartner2;

    @OneToOne(fetch = FetchType.EAGER, optional = true, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "last_message")
    private MessageModel lastMessage;

}
