package me.keills.exception.house;

public class HouseWithAddressAlreadyExistException extends RuntimeException{
    public HouseWithAddressAlreadyExistException(String message) {
        super(message);
    }
}
