package ru.skillbox.socialnetwork.messages.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DialogRepository extends JpaRepository<DialogModel, UUID> {
	@NotNull
	Page<DialogModel> findAllByConversationPartner1(Long conversationPartner1, @NotNull Pageable pageable);
	Boolean existsByConversationPartner1AndConversationPartner2(Long conversationPartner1, Long conversationPartner2);

}
