package me.keills.dto;

import lombok.Getter;
import lombok.Setter;
import me.keills.model.User;

@Getter
@Setter
public class UserDto {
    private String name;
    private byte age;

    private UserDto(String name, byte age) {
        this.name = name;
        this.age = age;
    }
    public static UserDto fromModel(User user) {
        return new UserDto(user.getName(), user.getAge());
    }
}
