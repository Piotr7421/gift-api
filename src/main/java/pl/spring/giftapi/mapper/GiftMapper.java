package pl.spring.giftapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.spring.giftapi.model.Gift;
import pl.spring.giftapi.model.command.CreateGiftCommand;
import pl.spring.giftapi.model.command.UpdateGiftCommand;
import pl.spring.giftapi.model.dto.GiftDto;

@Mapper(componentModel = "spring")
public interface GiftMapper {

    Gift mapFromCommand(CreateGiftCommand command);

    GiftDto mapToDto(Gift gift);

    void updateFromCommand(@MappingTarget Gift gift, UpdateGiftCommand command);
}
