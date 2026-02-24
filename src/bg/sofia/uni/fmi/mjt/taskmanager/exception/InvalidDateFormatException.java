package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class InvalidDateFormatException extends Exception {

    public InvalidDateFormatException(String message) {
        super(message);
    }

    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
