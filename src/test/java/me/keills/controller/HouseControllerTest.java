package me.keills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.keills.exception.house.HouseNotFoundException;
import me.keills.exception.user.UserNotFoundException;
import me.keills.model.House;
import me.keills.model.User;
import me.keills.repo.UserRepo;
import me.keills.security.AuthenticationWithJWT;
import me.keills.service.HouseService;
import me.keills.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = HouseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class HouseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HouseService houseService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationWithJWT authentication;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createHouse_Correct() throws Exception {
        User owner = new User("name", (byte)0, "password");
        owner.setId(1L);

        House house = new House("address", owner, List.of());

        when(houseService.createHouse(any())).thenReturn(house);
        when(userService.getUser(anyLong())).thenReturn(owner);
        doAnswer(invocation -> {
            return null;
        }).when(authentication).authenticateForHouse(any(), any());

        ResultActions response = mockMvc.perform(post("/api/house/create")
                        .header("Authorization", jwtToken(owner.getName(), owner.getPassword()))
                .param("address", house.getAddress())
                .param("ownerId", owner.getId().toString()));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(house.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name", CoreMatchers.is(owner.getName().toString())));
    }

    @Test
    void createHouse_UserNotFound() throws Exception {
        User owner = new User("name", (byte)0, "password");
        owner.setId(1L);

        House house = new House("address", owner, List.of());

        when(houseService.createHouse(any())).thenReturn(house);
        when(userService.getUser(anyLong())).thenThrow(new UserNotFoundException("User not found"));
        doAnswer(invocation -> {
            return null;
        }).when(authentication).authenticateForHouse(any(), any());

        ResultActions response = mockMvc.perform(post("/api/house/create")
                .header("Authorization", jwtToken(owner.getName(), owner.getPassword()))
                .param("address", house.getAddress())
                .param("ownerId", owner.getId().toString()));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createHouse_HouseNotFoundException() throws Exception {
        User owner = new User("name", (byte)0, "password");
        owner.setId(1L);

        House house = new House("address", owner, List.of());

        when(houseService.createHouse(any())).thenThrow(new HouseNotFoundException("House not found"));
        when(userService.getUser(anyLong())).thenReturn(owner);
        doAnswer(invocation -> {
            return null;
        }).when(authentication).authenticateForHouse(any(), any());

        ResultActions response = mockMvc.perform(post("/api/house/create")
                .header("Authorization", jwtToken(owner.getName(), owner.getPassword()))
                .param("address", house.getAddress())
                .param("ownerId", owner.getId().toString()));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getHouse_Correct() throws Exception {
        User owner = new User("name", (byte)0, "password");
        owner.setId(1L);

        House house = new House("address", owner, List.of());

        when(houseService.getHouse(any())).thenReturn(house);
        doAnswer(invocation -> {
            return null;
        }).when(authentication).authenticateForHouse(any(), any());

        ResultActions response = mockMvc.perform(get("/api/house/get")
                .header("Authorization", jwtToken(owner.getName(), owner.getPassword()))
                .param("id", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(house.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.name", CoreMatchers.is(owner.getName().toString())));
    }

    @Test
    void getHouse_HouseNotFoundException() throws Exception {
        User owner = new User("name", (byte)0, "password");
        owner.setId(1L);

        House house = new House("address", owner, List.of());

        when(houseService.getHouse(any())).thenThrow(new HouseNotFoundException("House not found"));
        doAnswer(invocation -> {
            return null;
        }).when(authentication).authenticateForHouse(any(), any());

        ResultActions response = mockMvc.perform(get("/api/house/get")
                .header("Authorization", jwtToken(owner.getName(), owner.getPassword()))
                .param("id", "1"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    String jwtToken(String name, String password) {
        return Jwts.builder()
                .claim("name", name)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS256, "zexTestSecret")
                .compact();
    }
}