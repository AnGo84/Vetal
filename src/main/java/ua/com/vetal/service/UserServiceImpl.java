package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.User;
import ua.com.vetal.repositories.UserRepository;

import java.util.List;

@Service("userService")
@Transactional
@Slf4j
public class UserServiceImpl implements SimpleService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
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

    public String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
