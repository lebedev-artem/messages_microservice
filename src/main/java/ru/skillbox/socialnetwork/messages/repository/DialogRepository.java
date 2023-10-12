package ru.skillbox.socialnetwork.messages.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;
import ru.skillbox.socialnetwork.messages.models.DialogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DialogRepository extends JpaRepository<DialogModel, UUID> {
	@NotNull
	Page<DialogModel> findAllByConversationAuthor(AuthorModel conversationAuthor, @NotNull Pageable pageable);
	Page<DialogModel> findAllByConversationPartner(AuthorModel conversationPartner, @NotNull Pageable pageable);
	Page<DialogModel> findAllByConversationAuthorOrConversationPartner(AuthorModel conversationAuthor, AuthorModel conversationPartner, @NotNull Pageable pageable);
	List<DialogModel> findAllByConversationAuthorOrConversationPartner(AuthorModel conversationAuthor, AuthorModel conversationPartner);
	List<DialogModel> findAllByConversationAuthor(AuthorModel conversationAuthor);
	Boolean existsByConversationAuthorAndConversationPartner(AuthorModel conversationAuthor, AuthorModel conversationPartner);
	DialogModel findByConversationAuthorAndConversationPartner(AuthorModel conversationAuthor, AuthorModel conversationPartner);
	@Query(value = "SELECT SUM(d.unread_count) AS unread FROM dialogs d WHERE d.conversation_author = :id", nativeQuery = true)
	Integer countUnreadCountByConversationAuthorId(Long id);
	@Query(value = "SELECT SUM(d.unread_count) AS unread FROM dialogs d WHERE d.conversation_partner = :id", nativeQuery = true)
	Integer countUnreadCountByConversationPartnerId(Long id);
	@Modifying
	@Query(value = "UPDATE dialogs d SET unread_count = 0 WHERE d.id = :dialogId", nativeQuery = true)
	void setUnreadCountToZero(UUID dialogId);

//	for bot


}
