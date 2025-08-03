package io.github.Piotr7421.giftapi.model.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
public class KidDto {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}
