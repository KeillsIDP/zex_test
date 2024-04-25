package me.keills.exception.house;

public class HouseNotFoundException extends RuntimeException{
    public HouseNotFoundException(String message) {
        super(message);
    }
}
