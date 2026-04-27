package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.SalePost;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSalePostRepository extends JpaRepository<SalePost, UUID> {
}
