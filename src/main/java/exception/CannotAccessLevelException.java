package exception;

public class CannotAccessLevelException extends Exception{

    public CannotAccessLevelException() {
    }

    public CannotAccessLevelException(String message) {
        super(message);
    }
}
