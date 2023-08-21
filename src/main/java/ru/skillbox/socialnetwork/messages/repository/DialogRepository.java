package ru.skillbox.socialnetwork.messages.repository;

import ru.skillbox.socialnetwork.messages.models.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DialogRepository extends JpaRepository<Dialog, UUID> {
}
