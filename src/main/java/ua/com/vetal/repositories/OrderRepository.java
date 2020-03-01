package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
