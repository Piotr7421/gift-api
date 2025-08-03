package io.github.Piotr7421.giftapi.strategy;

import org.springframework.stereotype.Component;
import io.github.Piotr7421.giftapi.strategy.model.Girl;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component("GIRL")
public class GirlCreationStrategy implements KidCreationStrategy {

    @Override
    public Girl create(CreateKidStrategyCommand strategyCommand) {
        Map<String, String> params = strategyCommand.getParams();
        return Girl.builder()
                .firstName(params.get("firstName"))
                .lastName(params.get("lastName"))
                .birthDate(LocalDate.parse(params.get("birthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .skirtColor(params.get("skirtColor"))
                .build();
    }
}
