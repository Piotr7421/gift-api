package pl.spring.giftapi.exceptions;

public class KidOptimisticLockingException extends RuntimeException {

    public KidOptimisticLockingException(String message) {
        super(message);
    }
}
