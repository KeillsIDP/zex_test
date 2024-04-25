package me.keills.service;

import me.keills.exception.user.UserAlreadyExistException;
import me.keills.exception.user.UserNotFoundException;
import me.keills.model.User;
import me.keills.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepo userRepo;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void createUser_Correct() {
        User user = new User("name", (byte)0, "password");
        when(userRepo.save(user)).thenReturn(user);
        assertEquals(user, userService.createUser(user));
    }

    @Test
    void createUser_UserAlreadyExistException() {
        User user = new User("name", (byte)0, "password");
        when(userRepo.findByName(user.getName())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistException.class, () ->{userService.createUser(user);});
    }

    @Test
    void getUser_Correct() {
        User user = new User("name", (byte)0, "password");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUser(1L));
    }

    @Test
    void getUser_UserNotFoundException() {
        User user = new User("name", (byte)0, "password");
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->{userService.getUser(1L);});
    }
}