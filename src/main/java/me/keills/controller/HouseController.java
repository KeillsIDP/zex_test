package me.keills.controller;

import me.keills.dto.HouseDto;
import me.keills.model.House;
import me.keills.security.AuthenticationWithJWT;
import me.keills.service.HouseService;
import me.keills.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/house")
public class HouseController {
    @Autowired
    private HouseService houseService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationWithJWT authentication;
    @PostMapping("/create")
    public ResponseEntity<?> createHouse(@RequestHeader(value = "Authorization", required = true) String token,
                                         @RequestParam("address") String address, @RequestParam("ownerId") Long ownerId){
        try{
            authentication.authenticateForUser(token, ownerId);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.createHouse(new House(address, userService.getUser(ownerId), List.of())
                    )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getHouse(@RequestHeader(value = "Authorization", required = true) String token,
                                      @RequestParam("id") Long id){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.getHouse(id)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-address")
    public ResponseEntity<?> updateHouseAddress(@RequestHeader(value = "Authorization", required = true) String token,
                                                @RequestParam("id") Long id, @RequestParam("address") String address){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.updateHouseAddress(id, address)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-owner")
    public ResponseEntity<?> updateHouseOwner(@RequestHeader(value = "Authorization", required = true) String token,
                                              @RequestParam("id") Long id, @RequestParam("ownerId") Long ownerId){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.updateHouseOwner(id, ownerId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-occupants")
    public ResponseEntity<?> updateHouseOccupants(@RequestHeader(value = "Authorization", required = true) String token,
                                                  @RequestParam("id") Long id, @RequestParam("occupants") List<Long> occupants){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.updateHouseOccupants(id, occupants)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-add-occupant")
    public ResponseEntity<?> updateHouseAddOccupant(@RequestHeader(value = "Authorization", required = true) String token,
                                                    @RequestParam("id") Long id, @RequestParam("occupant") Long occupant){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.addOccupant(id, occupant)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-remove-occupant")
    public ResponseEntity<?> updateHouseRemoveOccupant(@RequestHeader(value = "Authorization", required = true) String token,
                                                       @RequestParam("id") Long id, @RequestParam("occupant") Long occupant){
        try{
            authentication.authenticateForHouse(token, id);
            return ResponseEntity.ok(HouseDto.fromModel(
                    houseService.removeOccupant(id, occupant)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHouse(@RequestHeader(value = "Authorization", required = true) String token,
                                         @RequestParam("id") Long id){
        try{
            authentication.authenticateForHouse(token, id);
            houseService.deleteHouse(id);
            return ResponseEntity.ok().body("House deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}