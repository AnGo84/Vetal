package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.ViewTask;

@Repository
public interface ViewTaskRepository extends JpaRepository<ViewTask, Long> {
}
