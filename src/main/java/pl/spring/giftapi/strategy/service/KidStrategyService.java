package pl.spring.giftapi.strategy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.repository.KidRepository;
import pl.spring.giftapi.strategy.KidCreationStrategy;
import pl.spring.giftapi.strategy.model.command.CreateKidStrategyCommand;

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
