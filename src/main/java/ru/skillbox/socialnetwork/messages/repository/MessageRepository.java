package ru.skillbox.socialnetwork.messages.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.messages.models.MessageModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Artem Lebedev | 18/09/2023 - 00:13
 */
@Repository
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {

	Optional<List<MessageModel>> findByDialogId(UUID dialogId);

}
