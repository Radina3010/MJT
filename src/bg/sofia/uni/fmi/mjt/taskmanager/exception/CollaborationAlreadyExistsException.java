package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class CollaborationAlreadyExistsException extends Exception {

    public CollaborationAlreadyExistsException(String message) {
        super(message);
    }

    public CollaborationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
