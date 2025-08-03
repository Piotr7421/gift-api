package io.github.Piotr7421.giftapi.exceptions;

public class GiftOptimisticLockingException extends RuntimeException {

    public GiftOptimisticLockingException(String message) {
        super(message);
    }
}
