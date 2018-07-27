package ua.com.vetal.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CringleDirectoryDAO {

	@Autowired
	private EntityManager entityManager;

}
