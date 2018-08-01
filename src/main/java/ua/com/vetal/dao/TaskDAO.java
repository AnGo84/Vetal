package ua.com.vetal.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.User;

@Repository
@Transactional
public class TaskDAO {

	@Autowired
	private EntityManager entityManager;

	public User findTaskByAccount(String account) {
		try {
			String sql = "Select e from " + Task.class.getName() + " e " //
					+ " Where e.account = :account ";
			System.out.println(sql);
			Query query = entityManager.createQuery(sql, User.class);
			query.setParameter("account", account);

			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
