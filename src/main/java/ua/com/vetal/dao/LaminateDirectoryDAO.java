package ua.com.vetal.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LaminateDirectoryDAO {

	@Autowired
	private EntityManager entityManager;

	/*
	 * public User findUserAccount(String name) { try { String sql =
	 * "Select e from " + LaminateDirectory.class.getName() + " e " // +
	 * " Where e.name = :name "; // System.out.println(sql); Query query =
	 * entityManager.createQuery(sql, LaminateDirectory.class);
	 * query.setParameter("name", name);
	 * 
	 * return (User) query.getSingleResult(); } catch (NoResultException e) {
	 * return null; } }
	 */

}