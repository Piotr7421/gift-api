package pl.spring.giftapi.strategy.model.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class CreateKidStrategyCommand {

    private String type;
    private Map<String, String> params;
}
