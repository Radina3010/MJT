package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class TaskAlreadyExistsException extends Exception {

    public TaskAlreadyExistsException(String message) {
        super(message);
    }

    public TaskAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
