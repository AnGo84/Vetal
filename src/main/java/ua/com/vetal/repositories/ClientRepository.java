package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.ClientDirectory;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByFullName(String name);

}