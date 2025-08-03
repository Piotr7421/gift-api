package io.github.Piotr7421.giftapi.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateGiftCommand {

    @NotBlank(message = "BLANK_VALUE")
    private String name;

    @Positive(message = "NOT_POSITIVE")
    private double price;
}
