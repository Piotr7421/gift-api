package io.github.Piotr7421.giftapi.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.model.command.CreateKidCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateKidCommand;
import io.github.Piotr7421.giftapi.model.dto.KidDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class KidMapperTest {

    private final KidMapper kidMapper = Mappers.getMapper(KidMapper.class);

    @Test
    void mapFromCommand_ShouldMapAllFields() {
        CreateKidCommand command = new CreateKidCommand()
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(LocalDate.of(2012, 4, 21));

        Kid kid = kidMapper.mapFromCommand(command);

        assertThat(kid.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(kid.getLastName()).isEqualTo(command.getLastName());
        assertThat(kid.getBirthDate()).isEqualTo(command.getBirthDate());
    }

    @Test
    void mapToDto_ShouldMapAllFields() {
        // given
        Kid kid = Kid.builder()
                .id(7)
                .firstName("Anna")
                .lastName("Smith")
                .birthDate(LocalDate.of(2010, 10, 1))
                .build();

        KidDto dto = kidMapper.mapToDto(kid);

        assertThat(dto.getId()).isEqualTo(kid.getId());
        assertThat(dto.getFirstName()).isEqualTo(kid.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(kid.getLastName());
        assertThat(dto.getBirthDate()).isEqualTo(kid.getBirthDate());
    }

    @Test
    void updateFromCommand_ShouldUpdateAllFields() {
        Kid kid = Kid.builder()
                .id(3)
                .firstName("Old")
                .lastName("Name")
                .birthDate(LocalDate.of(2009, 8, 15))
                .build();

        UpdateKidCommand command = new UpdateKidCommand()
                .setFirstName("New")
                .setLastName("Surname")
                .setBirthDate(LocalDate.of(2008, 12, 24));

        kidMapper.updateFromCommand(kid, command);

        assertThat(kid.getFirstName()).isEqualTo(command.getFirstName());
        assertThat(kid.getLastName()).isEqualTo(command.getLastName());
        assertThat(kid.getBirthDate()).isEqualTo(command.getBirthDate());
    }
}