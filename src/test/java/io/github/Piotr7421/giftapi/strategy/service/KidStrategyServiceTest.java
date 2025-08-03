package io.github.Piotr7421.giftapi.strategy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.repository.KidRepository;
import io.github.Piotr7421.giftapi.strategy.KidCreationStrategy;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KidStrategyServiceTest {

    @Mock
    private KidRepository kidRepository;

    @Mock
    private KidCreationStrategy boyStrategy;

    @InjectMocks
    private KidStrategyService kidStrategyService;

    @BeforeEach
    void init() {
        Map<String, KidCreationStrategy> map = Map.of("BOY", boyStrategy);
        kidStrategyService = new KidStrategyService(map, kidRepository);
    }

    @Test
    void create_WithExistingStrategy_ShouldSaveKid() {
        CreateKidStrategyCommand cmd = new CreateKidStrategyCommand();
        cmd.setType("BOY");

        Kid kid = Kid.builder().firstName("Tim").build();
        when(boyStrategy.create(cmd)).thenReturn(kid);

        kidStrategyService.create(cmd);

        verify(boyStrategy).create(cmd);
        verify(kidRepository).save(kid);
    }
}