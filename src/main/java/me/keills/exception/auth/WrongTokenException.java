package me.keills.exception.auth;

public class WrongTokenException extends RuntimeException{
    public WrongTokenException(String message) {
        super(message);
    }
}
