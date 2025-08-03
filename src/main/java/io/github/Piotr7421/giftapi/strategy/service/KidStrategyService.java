package io.github.Piotr7421.giftapi.strategy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.repository.KidRepository;
import io.github.Piotr7421.giftapi.strategy.KidCreationStrategy;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KidStrategyService {

    private final Map<String, KidCreationStrategy> kidCreationStrategies;
    private final KidRepository kidRepository;

    public void create(CreateKidStrategyCommand strategyCommand) {
        KidCreationStrategy creationStrategy = kidCreationStrategies.get(strategyCommand.getType());
        Kid kid = creationStrategy.create(strategyCommand);
        log.info("created: {}", kid);
        kidRepository.save(kid);
    }
}
