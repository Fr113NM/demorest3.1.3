package com.crud.demorest.services;

import com.crud.demorest.entitys.Role;
import com.crud.demorest.entitys.User;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteById(long id);
    User findById(long id);
    List<User> findAllUsers();
    List<Role> findAllRoles();
    Role getRoleById(Long id);
}
