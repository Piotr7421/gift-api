package io.github.Piotr7421.giftapi.strategy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;
import io.github.Piotr7421.giftapi.strategy.service.KidStrategyService;

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
