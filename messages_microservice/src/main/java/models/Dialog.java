package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="DialogDto")

public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @Column(name = "`isDeleted`", nullable = false)
    public boolean isDeleted;

    @Column(name = "`unreadCount`", nullable = false)
    public int unreadCount;


    @Column(name = "`conversationPartner1`", nullable = false)
    public UUID conversationPartner1;

    @Column(name = "`conversationPartner2`", nullable = false)
    public UUID conversationPartner2;


}
