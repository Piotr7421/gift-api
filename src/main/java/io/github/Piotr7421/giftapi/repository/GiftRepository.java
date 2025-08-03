package io.github.Piotr7421.giftapi.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import io.github.Piotr7421.giftapi.model.Gift;

import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Integer> {

    Page<Gift> findAllByKidId(int kidId, Pageable pageable);

    Optional<Gift> findByIdAndKidId(int giftId, int kidId);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Gift> findWithLockingByIdAndKidId(int giftId, int kidId);

    long countByKidId(int kidId);

    void deleteAllByKidId(int id);

    void deleteByIdAndKidId(int id, int kidId);
}
