package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
