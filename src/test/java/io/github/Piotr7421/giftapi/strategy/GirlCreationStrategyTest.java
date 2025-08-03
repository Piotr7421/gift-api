package io.github.Piotr7421.giftapi.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.strategy.model.Girl;
import io.github.Piotr7421.giftapi.strategy.model.command.CreateKidStrategyCommand;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GirlCreationStrategyTest {

    @InjectMocks
    private GirlCreationStrategy girlStrategy;

    @Test
    void create_ShouldReturnGirlWithPopulatedFields() {
        // given
        CreateKidStrategyCommand cmd = new CreateKidStrategyCommand()
                .setType("GIRL")
                .setParams(Map.of(
                        "firstName", "Ana",
                        "lastName", "Smith",
                        "birthDate", "2015-08-12",
                        "skirtColor", "pink"
                ));

        Girl girl = girlStrategy.create(cmd);

        assertThat(girl).isNotNull();
        assertThat(girl).isInstanceOf(Girl.class);
        assertThat(girl).isInstanceOf(Kid.class);
        assertThat(girl.getFirstName()).isEqualTo("Ana");
        assertThat(girl.getLastName()).isEqualTo("Smith");
        assertThat(girl.getBirthDate()).isEqualTo(LocalDate.parse("2015-08-12"));
        assertThat(girl.getSkirtColor()).isEqualTo("pink");
    }
}