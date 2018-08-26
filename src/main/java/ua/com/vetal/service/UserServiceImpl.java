package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.User;
import ua.com.vetal.repositories.UserRepository;

@Service("userService")
@Transactional
public class UserServiceImpl implements SimpleService<User> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return userRepository.getOne(id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void saveObject(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateObject(User user) {
        saveObject(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllObjects() {
        return userRepository.findAll();
    }

    @Override
    public boolean isObjectExist(User user) {
        return findByName(user.getName()) != null;
    }

    public boolean isCurrentObject(User user) {
        User findUser = findById(user.getId());
        return (findUser != null && findUser.getName().equals(user.getName()));
    }

}
