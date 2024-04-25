package me.keills.service;

import me.keills.model.House;
import me.keills.model.User;

import java.util.List;

public interface HouseService {
    House createHouse(House house);
    House getHouse(Long id);
    House updateHouseAddress(Long id, String address);
    House updateHouseOwner(Long id, Long ownerId);
    House updateHouseOccupants(Long id, List<Long> occupantsId);
    House addOccupant(Long id, Long occupantId);
    House removeOccupant(Long id, Long occupantId);
    void deleteHouse(Long id);
}
