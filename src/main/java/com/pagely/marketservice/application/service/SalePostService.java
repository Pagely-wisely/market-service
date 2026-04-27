package com.pagely.marketservice.application.service;

import com.pagely.common.exception.BusinessException;
import com.pagely.marketservice.application.dto.command.CreateSalePostCommand;
import com.pagely.marketservice.application.dto.result.SalePostResult;
import com.pagely.marketservice.domain.exception.SalePostErrorCode;
import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.repository.SalePostRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalePostService {

    private final SalePostRepository salePostRepository;

    @Transactional
    public SalePostResult createSalePost(CreateSalePostCommand command) {
        SalePost salePost = SalePost.create(
                command.sellerId(),
                command.bookId(),
                command.title(),
                command.description(),
                command.price(),
                command.condition()
        );
        SalePost saved = salePostRepository.save(salePost);

        return SalePostResult.fromEntity(saved);
    }

    public SalePostResult getSalePost(UUID salePostId) {
        SalePost salePost = salePostRepository.findById(salePostId)
                .orElseThrow(() -> new BusinessException(SalePostErrorCode.SALE_POST_NOT_FOUND));

        return SalePostResult.fromEntity(salePost);
    }

    public Page<SalePostResult> getSalePosts(Pageable pageable) {
        return salePostRepository.findAll(pageable).map(SalePostResult::fromEntity);
    }
}
