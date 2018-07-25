package ua.com.vetal.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.User;

@Repository
@Transactional
public class ProductionDirectoryDAO {

	@Autowired
	private EntityManager entityManager;

	public User findUserAccount(String name) {
		try {
			String sql = "Select e from " + ProductionDirectory.class.getName() + " e " //
					+ " Where e.name = :name ";
			System.out.println(sql);
			Query query = entityManager.createQuery(sql, ProductionDirectory.class);
			query.setParameter("name", name);

			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
