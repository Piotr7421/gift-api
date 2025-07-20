package pl.spring.giftapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.model.command.CreateKidCommand;
import pl.spring.giftapi.model.command.UpdateKidCommand;
import pl.spring.giftapi.model.dto.KidDto;

@Mapper(componentModel = "spring")
public interface KidMapper {

    Kid mapFromCommand(CreateKidCommand command);

    KidDto mapToDto(Kid kid);

    void updateFromCommand(@MappingTarget Kid kid, UpdateKidCommand command);
}
