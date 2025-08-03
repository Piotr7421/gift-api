package io.github.Piotr7421.giftapi.strategy;

import org.springframework.stereotype.Component;
import io.github.Piotr7421.giftapi.strategy.model.Boy;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component("BOY")
public class BoyCreationStrategy implements KidCreationStrategy {

    @Override
    public Boy create(CreateKidStrategyCommand strategyCommand) {
        Map<String, String> params = strategyCommand.getParams();
        return Boy.builder()
                .firstName(params.get("firstName"))
                .lastName(params.get("lastName"))
                .birthDate(LocalDate.parse(params.get("birthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .pantsLength(Integer.parseInt(params.get("pantsLength")))
                .build();
    }
}
