package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Stencil;

@Repository
public interface StencilRepository extends JpaRepository<Stencil, Long> {
    Stencil findByAccount(String account);
}
