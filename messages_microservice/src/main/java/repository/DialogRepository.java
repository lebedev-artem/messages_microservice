package repository;

import models.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DialogRepository extends JpaRepository<Dialog, UUID> {
}
