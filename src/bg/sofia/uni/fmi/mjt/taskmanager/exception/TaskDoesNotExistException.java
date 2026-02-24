package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class TaskDoesNotExistException extends Exception {

    public TaskDoesNotExistException(String message) {
        super(message);
    }

    public TaskDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
