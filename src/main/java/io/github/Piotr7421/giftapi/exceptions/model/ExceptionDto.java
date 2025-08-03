package io.github.Piotr7421.giftapi.exceptions.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ExceptionDto {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
}
