package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.SalePost;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaSalePostRepository extends JpaRepository<SalePost, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SalePost s WHERE s.id = :id")
    Optional<SalePost> findByIdForUpdate(@Param("id") UUID id);
}
