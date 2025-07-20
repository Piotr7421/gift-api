package pl.spring.giftapi.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.spring.giftapi.exceptions.GiftNotFoundException;
import pl.spring.giftapi.exceptions.GiftOptimisticLockingException;
import pl.spring.giftapi.exceptions.InsertSqlException;
import pl.spring.giftapi.exceptions.KidLockTimeoutException;
import pl.spring.giftapi.exceptions.KidNotFoundException;
import pl.spring.giftapi.exceptions.KidOptimisticLockingException;
import pl.spring.giftapi.exceptions.LoadingIOException;
import pl.spring.giftapi.exceptions.ToManyGiftsException;
import pl.spring.giftapi.exceptions.model.ExceptionDto;
import pl.spring.giftapi.exceptions.model.ValidationErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ValidationErrorDto errorDto = new ValidationErrorDto();
        exception.getFieldErrors().forEach(fieldError ->
                errorDto.addViolation(fieldError.getField(), fieldError.getDefaultMessage()));
        return errorDto;
    }

    @ExceptionHandler(KidLockTimeoutException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ExceptionDto handleKidLockTimeoutException(KidLockTimeoutException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler({
            GiftOptimisticLockingException.class,
            KidOptimisticLockingException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleOptimisticLockingExceptions(RuntimeException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(ToManyGiftsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleSpecifiedException(RuntimeException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler({
            GiftNotFoundException.class,
            KidNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNoContentException(RuntimeException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler({
            InsertSqlException.class,
            LoadingIOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleInsertSqlException(InsertSqlException exception) {
        return new ExceptionDto(exception.getMessage());
    }
}
