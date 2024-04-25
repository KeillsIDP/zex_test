package me.keills.service;

import me.keills.exception.house.HouseNotFoundException;
import me.keills.exception.house.HouseWithAddressAlreadyExistException;
import me.keills.exception.user.UserNotFoundException;
import me.keills.model.House;
import me.keills.model.User;
import me.keills.repo.HouseRepo;
import me.keills.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService{
    @Autowired
    private HouseRepo houseRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public House createHouse(House house) {
        houseRepo.findByAddress(house.getAddress()).ifPresent(h->{
            throw new HouseWithAddressAlreadyExistException(String.format("House with address - %s already exist",house.getAddress()));
        });
        return houseRepo.save(house);
    }

    @Override
    public House getHouse(Long id) {
        return houseRepo.findById(id).orElseThrow(()->{
            throw new HouseNotFoundException(String.format("House with id - %s not found",id));
        });
    }

    @Override
    public House updateHouseAddress(Long id, String address) {
        House house = getHouse(id);
        house.setAddress(address);
        return houseRepo.save(house);
    }

    @Override
    public House updateHouseOwner(Long id, Long ownerId) {
        House house = getHouse(id);
        house.setOwner(userRepo.findById(ownerId).get());
        return houseRepo.save(house);
    }

    @Override
    public House updateHouseOccupants(Long id, List<Long> occupantsId) {
        House house = getHouse(id);
        house.setOccupants((List<User>) userRepo.findAllById(occupantsId.stream()
                .distinct()
                .collect(Collectors.toList())));
        return null;
    }

    @Override
    public House addOccupant(Long id, Long occupantId) {
        House house = getHouse(id);
        house.getOccupants().add(userRepo.findById(occupantId).orElseThrow(() -> new UserNotFoundException("User not found")));
        house.setOccupants(house.getOccupants()
                .stream()
                .distinct()
                .collect(Collectors.toList()));
        return houseRepo.save(house);
    }

    @Override
    public House removeOccupant(Long id, Long occupantId) {
        House house = getHouse(id);
        house.getOccupants().remove(userRepo.findById(occupantId).orElseThrow(() -> new UserNotFoundException("User not found")));
        return houseRepo.save(house);
    }

    @Override
    public void deleteHouse(Long id) {
        houseRepo.deleteById(id);
    }
}
