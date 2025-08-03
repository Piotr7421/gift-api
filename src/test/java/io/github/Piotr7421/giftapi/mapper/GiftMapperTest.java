package io.github.Piotr7421.giftapi.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import io.github.Piotr7421.giftapi.model.Gift;
import io.github.Piotr7421.giftapi.model.command.CreateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateGiftCommand;
import io.github.Piotr7421.giftapi.model.dto.GiftDto;

import static org.assertj.core.api.Assertions.assertThat;

class GiftMapperTest {

    private final GiftMapper giftMapper = Mappers.getMapper(GiftMapper.class);

    @Test
    void mapFromCommand_ShouldMapAllFields() {
        CreateGiftCommand command = new CreateGiftCommand()
                .setName("Lego")
                .setPrice(199.99);

        Gift gift = giftMapper.mapFromCommand(command);

        assertThat(gift.getName()).isEqualTo(command.getName());
        assertThat(gift.getPrice()).isEqualTo(command.getPrice());
    }

    @Test
    void mapToDto_ShouldMapAllFields() {
        Gift gift = Gift.builder()
                .id(5)
                .name("Bike")
                .price(999.99)
                .build();

        GiftDto dto = giftMapper.mapToDto(gift);

        assertThat(dto.getId()).isEqualTo(gift.getId());
        assertThat(dto.getName()).isEqualTo(gift.getName());
        assertThat(dto.getPrice()).isEqualTo(gift.getPrice());
    }

    @Test
    void updateFromCommand_ShouldUpdateFields() {
        Gift gift = Gift.builder()
                .id(3)
                .name("Old Toy")
                .price(50.0)
                .build();

        UpdateGiftCommand command = new UpdateGiftCommand()
                .setName("New Toy")
                .setPrice(75.0);

        giftMapper.updateFromCommand(gift, command);

        assertThat(gift.getName()).isEqualTo(command.getName());
        assertThat(gift.getPrice()).isEqualTo(command.getPrice());
    }
}