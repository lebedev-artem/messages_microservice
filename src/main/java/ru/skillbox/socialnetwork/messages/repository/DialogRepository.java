package ru.skillbox.socialnetwork.messages.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface DialogRepository extends JpaRepository<DialogModel, UUID> {
	@NotNull
	Page<DialogModel> findAllByConversationAuthor(Long conversationAuthor, @NotNull Pageable pageable);
	Set<DialogModel> findByConversationAuthor(Long conversationAuthor);
	Boolean existsByConversationAuthorAndConversationPartner(Long conversationAuthor, Long conversationPartner);

}
