package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class DueDateBeforeDateException extends Exception {

    public DueDateBeforeDateException(String message) {
        super(message);
    }

    public DueDateBeforeDateException(String message, Throwable cause) {
        super(message, cause);
    }

}

