package io.github.Piotr7421.giftapi.strategy;

import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

public interface KidCreationStrategy {

    Kid create(CreateKidStrategyCommand strategyCommand);
}
