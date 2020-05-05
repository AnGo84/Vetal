package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.StatisticOrder;

@Repository
public interface OrderRepository extends JpaRepository<StatisticOrder, Long> {

}
