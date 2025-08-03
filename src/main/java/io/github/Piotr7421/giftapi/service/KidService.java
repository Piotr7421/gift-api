package io.github.Piotr7421.giftapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.github.Piotr7421.giftapi.exceptions.KidNotFoundException;
import io.github.Piotr7421.giftapi.exceptions.KidOptimisticLockingException;
import io.github.Piotr7421.giftapi.mapper.KidMapper;
import io.github.Piotr7421.giftapi.model.Kid;
import io.github.Piotr7421.giftapi.model.command.CreateKidCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateKidCommand;
import io.github.Piotr7421.giftapi.model.dto.KidDto;
import io.github.Piotr7421.giftapi.repository.GiftRepository;
import io.github.Piotr7421.giftapi.repository.KidRepository;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class KidService {

    private final KidRepository kidRepository;
    private final KidMapper kidMapper;
    private final GiftRepository giftRepository;

    public Page<KidDto> findAll(Pageable pageable) {
        return kidRepository.findAll(pageable)
                .map(kidMapper::mapToDto);
    }

    public KidDto findById(int id) {
        return kidRepository.findById(id)
                .map(kidMapper::mapToDto)
                .orElseThrow(() -> new KidNotFoundException(MessageFormat
                        .format("Kid with id={0} not found", id)));
    }

    @Transactional
    public KidDto save(CreateKidCommand command) {
        Kid toSave = kidMapper.mapFromCommand(command);
        return kidMapper.mapToDto(kidRepository.save(toSave));
    }

    @Transactional
    public KidDto update(int id, UpdateKidCommand command) {
        Kid kidToUpdate = kidRepository.findWithLockingById(id).orElseThrow(() -> new KidNotFoundException(MessageFormat
                .format("Kid with id={0} not found", id)));
        kidMapper.updateFromCommand(kidToUpdate, command);
        try {
            return kidMapper.mapToDto(kidRepository.save(kidToUpdate));
        } catch (OptimisticLockingFailureException e) {
            throw new KidOptimisticLockingException(MessageFormat
                    .format("Kid optimistic locking exception while updating kid with id={0}.", id));
        }
    }

    @Transactional
    public void delete(int id) {
        giftRepository.deleteAllByKidId(id);
        kidRepository.deleteById(id);
    }
}
