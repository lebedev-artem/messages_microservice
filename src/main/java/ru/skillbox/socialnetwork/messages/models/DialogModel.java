package ru.skillbox.socialnetwork.messages.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name="dialogs")
@NoArgsConstructor
public class DialogModel {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "`is_deleted`", nullable = false)
    private boolean isDeleted;

    @Column(name = "`unread_count`", nullable = false)
    private int unreadCount;

    @Column(name = "`conversation_partner_1`", nullable = false)
    private UUID conversationPartner1;

    @Column(name = "`conversation_partner_2`", nullable = false)
    private UUID conversationPartner2;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "last_message")
    private MessageModel lastMessage;

}
