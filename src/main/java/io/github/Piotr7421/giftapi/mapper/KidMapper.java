package io.github.Piotr7421.giftapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.model.command.CreateKidCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateKidCommand;
import io.github.Piotr7421.giftapi.model.dto.KidDto;

@Mapper(componentModel = "spring")
public interface KidMapper {

    Kid mapFromCommand(CreateKidCommand command);

    KidDto mapToDto(Kid kid);

    void updateFromCommand(@MappingTarget Kid kid, UpdateKidCommand command);
}
