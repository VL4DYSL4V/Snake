package exception;

public final class EndOfGameException extends Exception{

    public EndOfGameException() {
    }

    public EndOfGameException(String message) {
        super(message);
    }
}
