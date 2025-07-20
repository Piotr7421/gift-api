package pl.spring.giftapi.exceptions;

public class GiftNotFoundException extends RuntimeException {

    public GiftNotFoundException(String message) {
        super(message);
    }
}
