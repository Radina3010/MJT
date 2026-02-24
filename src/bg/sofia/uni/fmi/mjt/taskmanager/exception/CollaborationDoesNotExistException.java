package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class CollaborationDoesNotExistException extends Exception {

    public CollaborationDoesNotExistException(String message) {
        super(message);
    }

    public CollaborationDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
