package com.pagely.marketservice.infrastructure.persistence;

import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SalePostRepositoryAdapter implements SalePostRepository {

    private final JpaSalePostRepository jpaSalePostRepository;

    @Override
    public SalePost save(SalePost salePost) {
        return jpaSalePostRepository.save(salePost);
    }
}
