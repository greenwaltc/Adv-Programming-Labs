package hangman;

public class GuessAlreadyMadeException extends Exception {

    public GuessAlreadyMadeException() {
        super();
    }

    public GuessAlreadyMadeException(String message) {
        super(message);
    }

    public GuessAlreadyMadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuessAlreadyMadeException(Throwable cause) {
        super(cause);
    }

    protected GuessAlreadyMadeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
