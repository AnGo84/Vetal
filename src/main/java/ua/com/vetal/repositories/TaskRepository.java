package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Manager findByName(String name);
    Task findByAccount(String account);

    // List<Task> findByFilterData(FilterData filterData);
    //List<Task> findAllByOrderByDateBeginDesc();

}
