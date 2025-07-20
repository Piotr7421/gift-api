package pl.spring.giftapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import pl.spring.giftapi.model.command.CreateKidCommand;

@Aspect
@Component
@Slf4j
public class AutoLogger {

    @Pointcut("execution(* pl.spring.giftapi.service.KidService.save(..)) && args(command)")
    public void kidCreationPointcut(CreateKidCommand command) {
    }

    @After("kidCreationPointcut(command)")
    public void logKidCreation(CreateKidCommand command) {
        log.info("Kid created: {}", command.getLastName());
    }
}
