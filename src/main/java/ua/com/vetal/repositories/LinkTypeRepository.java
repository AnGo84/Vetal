package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.entity.UserRole;

@Repository
public interface LinkTypeRepository extends JpaRepository<LinkType, Long> {

    LinkType findByName(String name);

}
