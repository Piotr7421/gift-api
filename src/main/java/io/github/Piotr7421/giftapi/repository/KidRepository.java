package io.github.Piotr7421.giftapi.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import io.github.Piotr7421.giftapi.model.Kid;

import java.util.Optional;

public interface KidRepository extends JpaRepository<Kid, Integer> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Kid> findWithLockingById(int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Kid> findWithPessimisticLockingById(int id);
}
