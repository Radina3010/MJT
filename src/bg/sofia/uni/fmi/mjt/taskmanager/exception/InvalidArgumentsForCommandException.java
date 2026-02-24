package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class InvalidArgumentsForCommandException extends Exception {

    public InvalidArgumentsForCommandException(String message) {
        super(message);
    }

    public InvalidArgumentsForCommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
