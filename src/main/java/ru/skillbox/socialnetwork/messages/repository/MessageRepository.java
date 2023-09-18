package ru.skillbox.socialnetwork.messages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.socialnetwork.messages.models.MessageModel;

import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:13
 */
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {

}
