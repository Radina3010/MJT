package bg.sofia.uni.fmi.mjt.taskmanager.exception;

public class NoChangesDetectedException  extends Exception {

    public NoChangesDetectedException(String message) {
        super(message);
    }

    public NoChangesDetectedException(String message, Throwable cause) {
        super(message, cause);
    }

}