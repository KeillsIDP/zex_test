package me.keills.service;

import me.keills.exception.user.UserAlreadyExistException;
import me.keills.exception.user.UserNotFoundException;
import me.keills.model.User;
import me.keills.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public User createUser(User user) {
        userRepo.findByName(user.getName()).ifPresent(u->{
            throw new UserAlreadyExistException(String.format("User with name - %s already exist",user.getName()));
        });
        return userRepo.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(()->{
            throw new UserNotFoundException(String.format("User with id - %s not found",id));
        });
    }

    @Override
    public User updateUserName(Long id, String name) {
        User user = getUser(id);
        user.setName(name);
        return userRepo.save(user);
    }

    @Override
    public User updateUserPassword(Long id, String password) {
        User user = getUser(id);
        user.setPassword(password);
        return userRepo.save(user);
    }

    @Override
    public User updateUserAge(Long id, byte age) {
        User user = getUser(id);
        user.setAge(age);
        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
