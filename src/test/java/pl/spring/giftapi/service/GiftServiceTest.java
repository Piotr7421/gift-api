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
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.spring.giftapi.exceptions.GiftNotFoundException;
import pl.spring.giftapi.exceptions.GiftOptimisticLockingException;
import pl.spring.giftapi.exceptions.KidLockTimeoutException;
import pl.spring.giftapi.exceptions.KidNotFoundException;
import pl.spring.giftapi.exceptions.ToManyGiftsException;
import pl.spring.giftapi.mapper.GiftMapper;
import pl.spring.giftapi.model.Gift;
import pl.spring.giftapi.model.Kid;
import pl.spring.giftapi.model.command.CreateGiftCommand;
import pl.spring.giftapi.model.command.UpdateGiftCommand;
import pl.spring.giftapi.model.dto.GiftDto;
import pl.spring.giftapi.repository.GiftRepository;
import pl.spring.giftapi.repository.KidRepository;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;
    @Mock
    private KidRepository kidRepository;
    @Mock
    private GiftMapper giftMapper;

    @InjectMocks
    private GiftService giftService;

    @Captor
    private ArgumentCaptor<Gift> giftCaptor;

    private Kid kid;
    private Gift gift;
    private GiftDto giftDto;
    private Gift giftToSave;
    private CreateGiftCommand createGiftCommand;
    private UpdateGiftCommand updateGiftCommand;

    @BeforeEach
    void setUp() {
        int kidId = 1;
        int giftId = 10;

        kid = Kid.builder()
                .id(kidId)
                .firstName("Jaś")
                .lastName("Fasola")
                .build();

        createGiftCommand = new CreateGiftCommand()
                .setName("Lego")
                .setPrice(299.99);

        giftToSave = Gift.builder()
                .name(createGiftCommand.getName())
                .price(createGiftCommand.getPrice())
                .build();

        gift = Gift.builder()
                .id(giftId)
                .name(createGiftCommand.getName())
                .price(createGiftCommand.getPrice())
                .kid(kid)
                .build();

        giftDto = GiftDto.builder()
                .id(giftId)
                .name(gift.getName())
                .price(gift.getPrice())
                .build();

        updateGiftCommand = new UpdateGiftCommand()
                .setName("Nintendo Switch")
                .setPrice(1399.00);
    }

    @Test
    void findAll_ShouldReturnPageOfGifts() {
        int kidId = kid.getId();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Gift> pageOfGifts = new PageImpl<>(List.of(gift), pageable, 1);

        when(giftRepository.findAllByKidId(kidId, pageable)).thenReturn(pageOfGifts);
        when(giftMapper.mapToDto(gift)).thenReturn(giftDto);

        Page<GiftDto> result = giftService.findAll(kidId, pageable);

        assertThat(result).isEqualTo(pageOfGifts.map(g -> giftDto));
        verify(giftRepository).findAllByKidId(kidId, pageable);
        verify(giftMapper).mapToDto(gift);
    }

    @Test
    void findAll_NoGifts_ShouldReturnEmptyPage() {
        int kidId = kid.getId();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Gift> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(giftRepository.findAllByKidId(kidId, pageable)).thenReturn(emptyPage);

        Page<GiftDto> result = giftService.findAll(kidId, pageable);

        assertThat(result).isEmpty();
        verify(giftRepository).findAllByKidId(kidId, pageable);
        verify(giftMapper, never()).mapToDto(any(Gift.class));
    }

    @Test
    void findById_ShouldReturnGiftDto() {
        int kidId = kid.getId();
        int giftId = gift.getId();

        when(giftRepository.findByIdAndKidId(giftId, kidId)).thenReturn(Optional.of(gift));
        when(giftMapper.mapToDto(gift)).thenReturn(giftDto);

        GiftDto result = giftService.findById(kidId, giftId);

        assertThat(result).isEqualTo(giftDto);
        verify(giftRepository).findByIdAndKidId(giftId, kidId);
        verify(giftMapper).mapToDto(gift);
    }

    @Test
    void findById_WhenGiftNotFound_ShouldThrowException() {
        int kidId = kid.getId();
        int nonExistingGift = 999;
        String expectedMsg = MessageFormat.format(
                "Gift with id={0} and kidId={1} not found", nonExistingGift, kidId);

        when(giftRepository.findByIdAndKidId(nonExistingGift, kidId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GiftNotFoundException.class)
                .isThrownBy(() -> giftService.findById(kidId, nonExistingGift))
                .withMessage(expectedMsg);

        verify(giftRepository).findByIdAndKidId(nonExistingGift, kidId);
        verify(giftMapper, never()).mapToDto(any(Gift.class));
    }

    @Test
    void save_HappyPath_ShouldCreateGift() {
        int kidId = kid.getId();

        when(kidRepository.findWithPessimisticLockingById(kidId)).thenReturn(Optional.of(kid));
        when(giftRepository.countByKidId(kidId)).thenReturn(1L);
        when(giftMapper.mapFromCommand(createGiftCommand)).thenReturn(giftToSave);
        when(giftRepository.save(giftToSave)).thenReturn(gift);
        when(giftMapper.mapToDto(gift)).thenReturn(giftDto);

        GiftDto result = giftService.save(kidId, createGiftCommand);

        assertThat(result).isEqualTo(giftDto);

        verify(kidRepository).findWithPessimisticLockingById(kidId);
        verify(giftRepository).countByKidId(kidId);
        verify(giftMapper).mapFromCommand(createGiftCommand);
        verify(giftRepository).save(giftCaptor.capture());
        verify(giftMapper).mapToDto(gift);

        Gift captured = giftCaptor.getValue();
        assertThat(captured.getKid()).isEqualTo(kid);
        assertThat(captured.getName()).isEqualTo(createGiftCommand.getName());
        assertThat(captured.getPrice()).isEqualTo(createGiftCommand.getPrice());
    }

    @Test
    void save_WhenKidNotFound_ShouldThrowKidNotFoundException() {
        int kidId = kid.getId();
        String expectedMsg = MessageFormat.format("Kid with id={0} not found", kidId);

        when(kidRepository.findWithPessimisticLockingById(kidId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(KidNotFoundException.class)
                .isThrownBy(() -> giftService.save(kidId, createGiftCommand))
                .withMessage(expectedMsg);

        verify(kidRepository).findWithPessimisticLockingById(kidId);
        verify(giftRepository, never()).save(any(Gift.class));
    }

    @Test
    void save_WhenPessimisticLockingFailure_ShouldThrowKidLockTimeoutException() {
        int kidId = kid.getId();

        when(kidRepository.findWithPessimisticLockingById(kidId))
                .thenThrow(new PessimisticLockingFailureException("lock timeout"));

        assertThatExceptionOfType(KidLockTimeoutException.class)
                .isThrownBy(() -> giftService.save(kidId, createGiftCommand))
                .withMessage("Could not acquire lock on kid - operation timed out");

        verify(kidRepository).findWithPessimisticLockingById(kidId);
        verify(giftRepository, never()).save(any(Gift.class));
    }

    @Test
    void save_WhenTooManyGifts_ShouldThrowToManyGiftsException() {
        int kidId = kid.getId();
        when(kidRepository.findWithPessimisticLockingById(kidId)).thenReturn(Optional.of(kid));
        when(giftRepository.countByKidId(kidId)).thenReturn(3L);

        String expectedMsg = MessageFormat.format("To many gifts for kid with id={0}", kidId);

        assertThatExceptionOfType(ToManyGiftsException.class)
                .isThrownBy(() -> giftService.save(kidId, createGiftCommand))
                .withMessage(expectedMsg);

        verify(kidRepository).findWithPessimisticLockingById(kidId);
        verify(giftRepository).countByKidId(kidId);
        verify(giftRepository, never()).save(any(Gift.class));
    }

    @Test
    void update_HappyPath_ShouldUpdateGift() {
        int kidId = kid.getId();
        int giftId = gift.getId();
        GiftDto updatedDto = GiftDto.builder()
                .name(updateGiftCommand.getName())
                .price(updateGiftCommand.getPrice())
                .build();

        when(giftRepository.findWithLockingByIdAndKidId(giftId, kidId)).thenReturn(Optional.of(gift));
        doNothing().when(giftMapper).updateFromCommand(gift, updateGiftCommand);
        when(giftRepository.save(gift)).thenReturn(gift);
        when(giftMapper.mapToDto(gift)).thenReturn(updatedDto);

        GiftDto result = giftService.update(kidId, giftId, updateGiftCommand);

        assertThat(result).isEqualTo(updatedDto);

        verify(giftRepository).findWithLockingByIdAndKidId(giftId, kidId);
        verify(giftMapper).updateFromCommand(gift, updateGiftCommand);
        verify(giftRepository).save(giftCaptor.capture());
        verify(giftMapper).mapToDto(gift);

        assertThat(giftCaptor.getValue()).isSameAs(gift);
    }

    @Test
    void update_WhenGiftNotFound_ShouldThrowGiftNotFoundException() {
        int kidId = kid.getId();
        int giftId = gift.getId();
        String expectedMsg = MessageFormat.format(
                "Gift with id={0} and kidId={1} not found", giftId, kidId);

        when(giftRepository.findWithLockingByIdAndKidId(giftId, kidId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GiftNotFoundException.class)
                .isThrownBy(() -> giftService.update(kidId, giftId, updateGiftCommand))
                .withMessage(expectedMsg);

        verify(giftRepository).findWithLockingByIdAndKidId(giftId, kidId);
        verify(giftRepository, never()).save(any(Gift.class));
    }

    @Test
    void update_WhenOptimisticLockingFailure_ShouldThrowGiftOptimisticLockingException() {
        int kidId = kid.getId();
        int giftId = gift.getId();

        when(giftRepository.findWithLockingByIdAndKidId(giftId, kidId)).thenReturn(Optional.of(gift));
        doNothing().when(giftMapper).updateFromCommand(gift, updateGiftCommand);
        when(giftRepository.save(gift)).thenThrow(new OptimisticLockingFailureException("optimistic lock"));

        String expectedMsg = MessageFormat.format(
                "Gift optimistic locking exception while updating gift with id={0}", giftId);

        assertThatExceptionOfType(GiftOptimisticLockingException.class)
                .isThrownBy(() -> giftService.update(kidId, giftId, updateGiftCommand))
                .withMessage(expectedMsg);

        verify(giftRepository).findWithLockingByIdAndKidId(giftId, kidId);
        verify(giftRepository).save(gift);
    }

    @Test
    void delete_ShouldDeleteGift() {
        int kidId = kid.getId();
        int giftId = gift.getId();

        doNothing().when(giftRepository).deleteByIdAndKidId(giftId, kidId);

        giftService.delete(kidId, giftId);

        verify(giftRepository).deleteByIdAndKidId(giftId, kidId);
    }
}