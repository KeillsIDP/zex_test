package me.keills.service;

import me.keills.model.User;

public interface UserService {
    User createUser(User user);
    User getUser(Long id);
    User updateUserName(Long id, String name);
    User updateUserPassword(Long id, String password);
    User updateUserAge(Long id, byte age);
    void deleteUser(Long id);
}
