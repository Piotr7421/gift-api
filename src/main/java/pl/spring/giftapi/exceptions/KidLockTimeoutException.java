package pl.spring.giftapi.exceptions;

public class KidLockTimeoutException extends RuntimeException {

    public KidLockTimeoutException(String message) {
        super(message);
    }
}
