package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Stencil;

@Repository
public interface StencilRepository extends JpaRepository<Stencil, Long> {
    Stencil findByAccount(String account);

    @Procedure(procedureName = "current_krascoottisk_amount")
    Double getCurrentKrascoottiskAmount();
}
