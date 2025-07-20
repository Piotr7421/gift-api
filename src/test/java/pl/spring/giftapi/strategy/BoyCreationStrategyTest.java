package pl.spring.giftapi.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.strategy.model.Boy;
import pl.spring.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoyCreationStrategyTest {

    @InjectMocks
    private BoyCreationStrategy boyStrategy;

    @Test
    void create_ShouldReturnBoyWithPopulatedFields() {
        CreateKidStrategyCommand cmd = new CreateKidStrategyCommand()
                .setType("BOY")
                .setParams(Map.of(
                        "firstName", "Tim",
                        "lastName", "Lee",
                        "birthDate", "2014-05-01",
                        "pantsLength", "23"
                ));

        Boy boy = boyStrategy.create(cmd);

        assertThat(boy).isNotNull();
        assertThat(boy).isInstanceOf(Boy.class);
        assertThat(boy).isInstanceOf(Kid.class);
        assertThat(boy.getFirstName()).isEqualTo("Tim");
        assertThat(boy.getLastName()).isEqualTo("Lee");
        assertThat(boy.getBirthDate()).isEqualTo(LocalDate.parse("2014-05-01"));
        assertThat(boy.getPantsLength()).isEqualTo(23);
    }
}