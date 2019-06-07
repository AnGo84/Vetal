package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;

@Repository
public interface StencilRepository extends JpaRepository<Stencil, Long> {

    // Manager findByName(String name);
    Stencil findByAccount(String account);

    // List<Task> findByFilterData(FilterData filterData);
    //List<Task> findAllByOrderByDateBeginDesc();

}
