package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Payment;
import ua.com.vetal.entity.State;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByName(String name);

}
