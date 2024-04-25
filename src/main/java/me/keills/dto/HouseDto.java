package me.keills.dto;

import lombok.Getter;
import lombok.Setter;
import me.keills.model.House;
import me.keills.repo.HouseRepo;

import java.util.List;

@Getter
@Setter
public class HouseDto {
    private String address;
    private UserDto owner;
    private List<UserDto> occupants;

    private HouseDto(String address, UserDto owner, List<UserDto> occupants) {
        this.address = address;
        this.owner = owner;
        this.occupants = occupants;
    }
    public static HouseDto fromModel(House house) {
        return new HouseDto(house.getAddress(),
                UserDto.fromModel(house.getOwner()),
                house.getOccupants().stream().map(UserDto::fromModel).toList());
    }
}
