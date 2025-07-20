package pl.spring.giftapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class GiftService {

    public final GiftRepository giftRepository;
    public final KidRepository kidRepository;
    public final GiftMapper giftMapper;

    public Page<GiftDto> findAll(int kidId, Pageable pageable) {
        return giftRepository.findAllByKidId(kidId, pageable)
                .map(giftMapper::mapToDto);
    }

    public GiftDto findById(int kidId, int giftId) {
        return giftRepository.findByIdAndKidId(giftId, kidId)
                .map(giftMapper::mapToDto)
                .orElseThrow(() -> new GiftNotFoundException(MessageFormat
                        .format("Gift with id={0} and kidId={1} not found", giftId, kidId)));
    }

    @Transactional
    public GiftDto save(int kidId, CreateGiftCommand command) {
        Kid kid;
        try {
            kid = kidRepository.findWithPessimisticLockingById(kidId)
                    .orElseThrow(() -> new KidNotFoundException(MessageFormat
                            .format("Kid with id={0} not found", kidId)));
        } catch (PessimisticLockingFailureException e) {
            throw new KidLockTimeoutException("Could not acquire lock on kid - operation timed out");
        }
        long currentGiftsCount = giftRepository.countByKidId(kidId);
        if (currentGiftsCount > 2) {
            throw new ToManyGiftsException(MessageFormat
                    .format("To many gifts for kid with id={0}", kidId));
        }
        Gift toSave = giftMapper.mapFromCommand(command);
        toSave.setKid(kid);
        return giftMapper.mapToDto(giftRepository.save(toSave));
    }

    @Transactional
    public GiftDto update(int kidId, int giftId, UpdateGiftCommand command) {
        Gift gift = giftRepository.findWithLockingByIdAndKidId(giftId, kidId)
                .orElseThrow(() -> new GiftNotFoundException(MessageFormat
                        .format("Gift with id={0} and kidId={1} not found", giftId, kidId)));
        giftMapper.updateFromCommand(gift, command);
        try {
            return giftMapper.mapToDto(giftRepository.save(gift));
        } catch (OptimisticLockingFailureException e) {
            throw new GiftOptimisticLockingException(MessageFormat
                    .format("Gift optimistic locking exception while updating gift with id={0}", giftId));
        }
    }

    @Transactional
    public void delete(int kidId, int id) {
        giftRepository.deleteByIdAndKidId(id, kidId);
    }
}
