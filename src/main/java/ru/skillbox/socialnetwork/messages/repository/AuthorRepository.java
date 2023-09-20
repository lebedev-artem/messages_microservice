package ru.skillbox.socialnetwork.messages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.socialnetwork.messages.models.AuthorModel;

/**
 * @author Artem Lebedev | 21/09/2023 - 01:26
 */
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {
}
