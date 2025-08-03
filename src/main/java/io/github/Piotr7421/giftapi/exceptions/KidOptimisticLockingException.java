package io.github.Piotr7421.giftapi.exceptions;

public class KidOptimisticLockingException extends RuntimeException {

    public KidOptimisticLockingException(String message) {
        super(message);
    }
}
