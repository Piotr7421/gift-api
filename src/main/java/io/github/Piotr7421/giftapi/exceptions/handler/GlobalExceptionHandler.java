package io.github.Piotr7421.giftapi.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.github.Piotr7421.giftapi.exceptions.GiftNotFoundException;
import io.github.Piotr7421.giftapi.exceptions.GiftOptimisticLockingException;
import io.github.Piotr7421.giftapi.exceptions.InsertSqlException;
import io.github.Piotr7421.giftapi.exceptions.KidLockTimeoutException;
import io.github.Piotr7421.giftapi.exceptions.KidNotFoundException;
import io.github.Piotr7421.giftapi.exceptions.KidOptimisticLockingException;
import io.github.Piotr7421.giftapi.exceptions.LoadingIOException;
import io.github.Piotr7421.giftapi.exceptions.ToManyGiftsException;
import io.github.Piotr7421.giftapi.exceptions.model.ExceptionDto;
import io.github.Piotr7421.giftapi.exceptions.model.ValidationErrorDto;

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
