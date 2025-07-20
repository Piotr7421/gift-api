package pl.spring.giftapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.spring.giftapi.exceptions.KidNotFoundException;
import pl.spring.giftapi.mapper.KidMapper;
import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.model.command.CreateKidCommand;
import pl.spring.giftapi.model.command.UpdateKidCommand;
import pl.spring.giftapi.model.dto.KidDto;
import pl.spring.giftapi.repository.GiftRepository;
import pl.spring.giftapi.repository.KidRepository;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KidServiceTest {

    @Mock
    private KidRepository kidRepository;

    @Mock
    private KidMapper kidMapper;

    @Mock
    private GiftRepository giftRepository;

    @InjectMocks
    private KidService kidService;

    @Captor
    private ArgumentCaptor<Kid> kidCaptor;

    private Kid kid;
    private KidDto kidDto;
    private CreateKidCommand createKidCommand;
    private UpdateKidCommand updateKidCommand;

    @BeforeEach
    void setUp() {
        int kidId = 1;
        createKidCommand = new CreateKidCommand()
                .setFirstName("Jaś")
                .setLastName("Fasola")
                .setBirthDate(LocalDate.of(2015, 6, 14));

        kid = Kid.builder()
                .id(kidId)
                .firstName(createKidCommand.getFirstName())
                .lastName(createKidCommand.getLastName())
                .birthDate(createKidCommand.getBirthDate())
                .build();

        kidDto = KidDto.builder()
                .id(kidId)
                .firstName(kid.getFirstName())
                .lastName(kid.getLastName())
                .birthDate(kid.getBirthDate())
                .build();

        updateKidCommand = new UpdateKidCommand()
                .setFirstName("Johnny")
                .setLastName("Bean")
                .setBirthDate(LocalDate.of(2014, 8, 22));
    }

    @Test
    void findAll_ShouldReturnPageOfKidDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Kid> kids = List.of(kid);
        Page<Kid> kidPage = new PageImpl<>(kids, pageable, kids.size());

        when(kidRepository.findAll(pageable)).thenReturn(kidPage);
        when(kidMapper.mapToDto(kid)).thenReturn(kidDto);

        Page<KidDto> result = kidService.findAll(pageable);

        assertThat(result).isEqualTo(kidPage.map(k -> kidDto));
        verify(kidRepository).findAll(pageable);
        verify(kidMapper).mapToDto(kid);
    }

    @Test
    void findAll_NoKids_ShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Kid> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(kidRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<KidDto> result = kidService.findAll(pageable);

        assertThat(result).isEmpty();
        verify(kidRepository).findAll(pageable);
        verify(kidMapper, never()).mapToDto(any(Kid.class));
    }

    @Test
    void findById_ShouldReturnKidDto() {
        int kidId = kid.getId();
        when(kidRepository.findById(kidId)).thenReturn(Optional.of(kid));
        when(kidMapper.mapToDto(kid)).thenReturn(kidDto);

        KidDto result = kidService.findById(kidId);

        assertThat(result).isEqualTo(kidDto);
        verify(kidRepository).findById(kidId);
        verify(kidMapper).mapToDto(kid);
    }

    @Test
    void findById_WhenKidNotFound_ShouldThrowException() {
        int kidId = kid.getId();
        String expectedMsg = MessageFormat.format("Kid with id={0} not found", kidId);
        when(kidRepository.findById(kidId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(KidNotFoundException.class)
                .isThrownBy(() -> kidService.findById(kidId))
                .withMessage(expectedMsg);

        verify(kidRepository).findById(kidId);
        verify(kidMapper, never()).mapToDto(any(Kid.class));
    }

    @Test
    void save_HappyPath_ShouldSaveKid() {
        Kid kidToSave = Kid.builder()
                .firstName(createKidCommand.getFirstName())
                .lastName(createKidCommand.getLastName())
                .birthDate(createKidCommand.getBirthDate())
                .build();

        when(kidMapper.mapFromCommand(createKidCommand)).thenReturn(kidToSave);
        when(kidRepository.save(kidToSave)).thenReturn(kid);
        when(kidMapper.mapToDto(kid)).thenReturn(kidDto);

        KidDto result = kidService.save(createKidCommand);

        assertThat(result).isEqualTo(kidDto);
        verify(kidMapper).mapFromCommand(createKidCommand);
        verify(kidRepository).save(kidCaptor.capture());
        verify(kidMapper).mapToDto(kid);

        Kid captured = kidCaptor.getValue();
        assertThat(captured.getFirstName()).isEqualTo(createKidCommand.getFirstName());
        assertThat(captured.getLastName()).isEqualTo(createKidCommand.getLastName());
        assertThat(captured.getBirthDate()).isEqualTo(createKidCommand.getBirthDate());
    }

    @Test
    void save_WhenCreateKidCommandHasMissingFields_ShouldThrowException() {
        CreateKidCommand invalidCommand = new CreateKidCommand(); // all fields null
        when(kidMapper.mapFromCommand(invalidCommand))
                .thenThrow(new IllegalArgumentException("Invalid command"));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> kidService.save(invalidCommand))
                .withMessage("Invalid command");

        verify(kidMapper).mapFromCommand(invalidCommand);
        verify(kidRepository, never()).save(any(Kid.class));
    }

    @Test
    void update_HappyPath_ShouldUpdateKid() {
        int kidId = kid.getId();
        when(kidRepository.findWithLockingById(kidId)).thenReturn(Optional.of(kid));
        doNothing().when(kidMapper).updateFromCommand(kid, updateKidCommand);
        when(kidRepository.save(kid)).thenReturn(kid);
        when(kidMapper.mapToDto(kid)).thenReturn(kidDto);

        KidDto result = kidService.update(kidId, updateKidCommand);

        assertThat(result).isEqualTo(kidDto);
        verify(kidRepository).findWithLockingById(kidId);
        verify(kidMapper).updateFromCommand(kid, updateKidCommand);
        verify(kidRepository).save(kidCaptor.capture());
        verify(kidMapper).mapToDto(kid);
        Kid captured = kidCaptor.getValue();
        assertThat(captured).isSameAs(kid);
    }

    @Test
    void update_WhenKidNotFound_ShouldThrowException() {
        int nonExistingId = 99;
        String expectedMsg = MessageFormat.format("Kid with id={0} not found", nonExistingId);
        when(kidRepository.findWithLockingById(nonExistingId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(KidNotFoundException.class)
                .isThrownBy(() -> kidService.update(nonExistingId, updateKidCommand))
                .withMessage(expectedMsg);

        verify(kidRepository).findWithLockingById(nonExistingId);
        verify(kidMapper, never()).updateFromCommand(any(Kid.class), any(UpdateKidCommand.class));
        verify(kidRepository, never()).save(any(Kid.class));
    }

    @Test
    void update_WhenOptimisticLockingFailure_ShouldPropagateException() {
        int kidId = kid.getId();
        when(kidRepository.findWithLockingById(kidId)).thenReturn(Optional.of(kid));
        doNothing().when(kidMapper).updateFromCommand(kid, updateKidCommand);
        when(kidRepository.save(kid)).thenThrow(new OptimisticLockingFailureException("Optimistic lock"));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> kidService.update(kidId, updateKidCommand));

        verify(kidRepository).findWithLockingById(kidId);
        verify(kidMapper).updateFromCommand(kid, updateKidCommand);
        verify(kidRepository).save(kid);
    }

    @Test
    void update_WhenUpdateKidCommandHasMissingFields_ShouldThrowException() {
        // given
        int kidId = kid.getId();
        UpdateKidCommand invalidCommand = new UpdateKidCommand(); // wszystkie pola null
        when(kidRepository.findWithLockingById(kidId)).thenReturn(Optional.of(kid));
        doThrow(new IllegalArgumentException("Invalid command"))
                .when(kidMapper).updateFromCommand(kid, invalidCommand);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> kidService.update(kidId, invalidCommand))
                .withMessage("Invalid command");

        verify(kidRepository).findWithLockingById(kidId);
        verify(kidMapper).updateFromCommand(kid, invalidCommand);
        verify(kidRepository, never()).save(any(Kid.class));
    }

    @Test
    void delete_ShouldDeleteKid() {
        int kidId = kid.getId();
        doNothing().when(giftRepository).deleteAllByKidId(kidId);
        doNothing().when(kidRepository).deleteById(kidId);

        kidService.delete(kidId);

        verify(giftRepository).deleteAllByKidId(kidId);
        verify(kidRepository).deleteById(kidId);
    }
}
