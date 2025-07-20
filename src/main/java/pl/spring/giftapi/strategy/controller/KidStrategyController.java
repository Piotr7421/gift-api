package pl.spring.giftapi.strategy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.spring.giftapi.strategy.model.command.CreateKidStrategyCommand;
import pl.spring.giftapi.strategy.service.KidStrategyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategy/kids")
public class KidStrategyController {

    private final KidStrategyService kidStrategyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateKidStrategyCommand strategyCommand) {
        kidStrategyService.create(strategyCommand);
    }
}
