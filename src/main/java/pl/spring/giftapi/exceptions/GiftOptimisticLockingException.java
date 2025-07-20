package pl.spring.giftapi.exceptions;

public class GiftOptimisticLockingException extends RuntimeException {

    public GiftOptimisticLockingException(String message) {
        super(message);
    }
}
