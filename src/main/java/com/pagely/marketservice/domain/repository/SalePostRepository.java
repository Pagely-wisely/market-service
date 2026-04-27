package com.pagely.marketservice.domain.repository;

import com.pagely.marketservice.domain.model.SalePost;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalePostRepository {
    SalePost save(SalePost salePost);

    Optional<SalePost> findById(UUID salePostId);

    Page<SalePost> findAll(Pageable pageable);
}
