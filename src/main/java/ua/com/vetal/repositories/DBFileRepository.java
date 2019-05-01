package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.DBFile;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, Long> {

}
