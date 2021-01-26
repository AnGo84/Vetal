package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByAccount(String account);
}
