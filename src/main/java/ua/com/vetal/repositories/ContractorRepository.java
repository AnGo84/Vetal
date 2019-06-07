package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.Contractor;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    // Manager findByName(String name);
    Contractor findByEmail(String email);

}
