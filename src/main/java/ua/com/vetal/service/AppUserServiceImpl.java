package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.AppUser;
import ua.com.vetal.repositories.AppUserRepository;

import java.util.List;

@Service("appUserService")
@Transactional
public class AppUserServiceImpl implements SimpleService<AppUser> {

	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public AppUser findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return appUserRepository.getOne(id);
	}

	@Override
	public AppUser findByName(String name) {
		return appUserRepository.findByUserName(name);
	}

	@Override
	public void saveObject(AppUser user) {
		appUserRepository.save(user);
	}

	@Override
	public void updateObject(AppUser user) {
		saveObject(user);
	}

	@Override
	public void deleteById(Long id) {
		appUserRepository.deleteById(id);
	}

	@Override
	public List<AppUser> findAllObjects() {
		return appUserRepository.findAll();
	}

	@Override
	public boolean isObjectExist(AppUser user) {
		return findByName(user.getUserName()) != null;
	}

}
