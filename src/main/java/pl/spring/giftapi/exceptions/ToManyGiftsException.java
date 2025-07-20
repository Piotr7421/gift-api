package pl.spring.giftapi.exceptions;

public class ToManyGiftsException extends RuntimeException {

    public ToManyGiftsException(String message) {
        super(message);
    }
}
