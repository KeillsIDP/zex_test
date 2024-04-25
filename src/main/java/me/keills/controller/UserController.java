package me.keills.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.keills.model.User;
import me.keills.security.AuthenticationWithJWT;
import me.keills.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationWithJWT authentication;
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestParam("name") String name, @RequestParam("age") byte age, @RequestParam("password") String password){
        try{
            String jwtToken = jwtToken(name,password);
            userService.createUser(new User(name, age, password));
            return ResponseEntity.ok().body("User created. JWT-token: " + jwtToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestHeader(value = "Authorization", required = true) String token,@RequestParam("id") Long id){
        try{
            authentication.authenticateForUser(token,id);
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-name")
    public ResponseEntity<?> updateUserName(@RequestHeader(value = "Authorization", required = true) String token,
                                            @RequestParam("id") Long id, @RequestParam("name") String name){
        try{
            authentication.authenticateForUser(token,id);
            return ResponseEntity.ok(userService.updateUserName(id,name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-age")
    public ResponseEntity<?> updateUserAge(@RequestHeader(value = "Authorization", required = true) String token,
                                           @RequestParam("id") Long id, @RequestParam("age") byte age){
        try{
            authentication.authenticateForUser(token,id);
            return ResponseEntity.ok(userService.updateUserAge(id,age));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/update-password")
    public ResponseEntity<?> updateUserPassword(@RequestHeader(value = "Authorization", required = true) String token,
                                                @RequestParam("id") Long id, @RequestParam("password") String password){
        try{
            authentication.authenticateForUser(token,id);
            return ResponseEntity.ok(userService.updateUserPassword(id,password));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization", required = true) String token,
                                        @RequestParam("id") Long id){
        try{
            authentication.authenticateForUser(token,id);
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String jwtToken(String name, String password) {
        return Jwts.builder()
                .claim("name", name)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS256, "zexTestSecret")
                .compact();
    }
}
