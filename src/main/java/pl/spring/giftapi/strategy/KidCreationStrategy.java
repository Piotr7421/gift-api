package pl.spring.giftapi.strategy;

import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.strategy.model.command.CreateKidStrategyCommand;

public interface KidCreationStrategy {

    Kid create(CreateKidStrategyCommand strategyCommand);
}
