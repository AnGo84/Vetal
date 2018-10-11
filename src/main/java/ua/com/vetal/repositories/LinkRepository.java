package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.entity.Link;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

	Link findByFullName(String name);
	List<Link> findByLinkType_Id(Long typeID);

}
