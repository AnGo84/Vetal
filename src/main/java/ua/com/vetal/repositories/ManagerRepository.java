package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.repositories.common.CommonRepository;

@Repository
public interface ManagerRepository extends CommonRepository<Manager> {

}
/*
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
*/
