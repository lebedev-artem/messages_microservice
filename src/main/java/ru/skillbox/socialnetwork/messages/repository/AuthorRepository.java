package ru.skillbox.socialnetwork.messages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;

/**
 * @author Artem Lebedev | 21/09/2023 - 01:26
 */
@Repository
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {
}
