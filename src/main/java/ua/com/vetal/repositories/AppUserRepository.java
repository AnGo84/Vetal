package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUserName(String name);

}
