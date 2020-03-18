package xyz.silverspoon.exception;

public class NotFoundException extends Exception {
    private int status;
    private String message;

    public NotFoundException(String message) {
        super(message);
        status = 404;
        this.message = message;
    }
}
