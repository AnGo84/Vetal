package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.UserRole;
import ua.com.vetal.repositories.UserRoleRepository;

@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl implements SimpleService<UserRole> {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Override
	public UserRole findById(Long id) {
		return userRoleRepository.getOne(id);
	}

	@Override
	public UserRole findByName(String name) {
		return userRoleRepository.findByName(name);
	}

	@Override
	public void saveObject(UserRole userRole) {
		userRoleRepository.save(userRole);
	}

	@Override
	public void updateObject(UserRole userRole) {
		saveObject(userRole);
	}

	@Override
	public void deleteById(Long id) {
		userRoleRepository.deleteById(id);
	}

	@Override
	public List<UserRole> findAllObjects() {
		return userRoleRepository.findAll();
	}

	@Override
	public boolean isObjectExist(UserRole userRole) {
		return findByName(userRole.getName()) != null;
	}

}
