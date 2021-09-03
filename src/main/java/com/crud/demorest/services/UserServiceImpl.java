package com.crud.demorest.services;

import com.crud.demorest.entitys.Role;
import com.crud.demorest.entitys.User;
import com.crud.demorest.exceptionHandling.NoSuchUserException;
import com.crud.demorest.repositories.RoleRepository;
import com.crud.demorest.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword().equals("")) {
            user.setPassword(userRepository.getOne(user.getId()).getPassword());
        }
        else if (!user.getPassword().equals(userRepository.getOne(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteById(long id) {
        User user = userRepository.getOne(id);
        userRepository.delete(user);
    }

    @Override
    public User findById(long id) {
        User user;
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new NoSuchUserException(" There is no user with ID = " + id);
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }
}
