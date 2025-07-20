package pl.spring.giftapi.exceptions;

public class KidNotFoundException extends RuntimeException {

    public KidNotFoundException(String message) {
        super(message);
    }
}
