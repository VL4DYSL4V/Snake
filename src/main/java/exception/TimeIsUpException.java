package exception;

public final class TimeIsUpException extends Exception{

    public TimeIsUpException() {
    }

    public TimeIsUpException(String message) {
        super(message);
    }
}
