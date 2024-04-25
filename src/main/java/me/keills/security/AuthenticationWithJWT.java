package me.keills.security;

import io.jsonwebtoken.Jwts;
import me.keills.exception.auth.InvalidTokenException;
import me.keills.exception.auth.WrongTokenException;
import me.keills.exception.house.HouseNotFoundException;
import me.keills.exception.user.UserNotFoundException;
import me.keills.model.House;
import me.keills.model.User;
import me.keills.repo.HouseRepo;
import me.keills.repo.UserRepo;
import me.keills.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationWithJWT {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private HouseRepo houseRepo;
    public void authenticateForUser(String token, Long id) throws WrongTokenException, UserNotFoundException {
        try {
            String name = jwtParseForString(token,"name");
            String password = jwtParseForString(token,"password");

            User user = userRepo.findByName(name).orElseThrow(() -> new UserNotFoundException("User not found"));

            if(user.getId()!=id || !user.getPassword().equals(password))
                throw new WrongTokenException("Token is wrong for this user");

        } catch (WrongTokenException e){
            throw e;
        } catch (UserNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new InvalidTokenException("Invalid token");
        }
    }

    public void authenticateForHouse(String token,Long id) throws WrongTokenException, HouseNotFoundException {
        try {
            String name = jwtParseForString(token,"name");
            String password = jwtParseForString(token,"password");

            User houseOwner = houseRepo.findById(id).orElseThrow(() -> new HouseNotFoundException("House not found")).getOwner();

            if(!houseOwner.getName().equals(name) || !houseOwner.getPassword().equals(password))
                throw new WrongTokenException("Token is wrong for this user");

        } catch (WrongTokenException e){
            throw e;
        } catch(HouseNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new InvalidTokenException("Invalid token");
        }
    }

    private String jwtParseForString(String token,String name) {
        return Jwts.parser()
                .setSigningKey("zexTestSecret")
                .parseClaimsJws(token)
                .getBody()
                .get(name, String.class);
    }
}
