package io.github.Piotr7421.giftapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import io.github.Piotr7421.giftapi.model.Gift;
import io.github.Piotr7421.giftapi.model.command.CreateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateGiftCommand;
import io.github.Piotr7421.giftapi.model.dto.GiftDto;

@Mapper(componentModel = "spring")
public interface GiftMapper {

    Gift mapFromCommand(CreateGiftCommand command);

    GiftDto mapToDto(Gift gift);

    void updateFromCommand(@MappingTarget Gift gift, UpdateGiftCommand command);
}
