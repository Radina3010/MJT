package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class UserAlreadyExistInCollaboration extends Exception {

    public UserAlreadyExistInCollaboration(String message) {
        super(message);
    }

    public UserAlreadyExistInCollaboration(String message, Throwable cause) {
        super(message, cause);
    }

}
