package pl.spring.giftapi.exceptions;

public class InsertSqlException extends RuntimeException {

    public InsertSqlException(String message) {
        super(message);
    }
}
