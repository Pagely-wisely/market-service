package com.pagely.marketservice.application.service;

import com.pagely.marketservice.application.dto.command.CreateSalePostCommand;
import com.pagely.marketservice.application.dto.result.SalePostResult;
import com.pagely.marketservice.domain.model.SalePost;
import com.pagely.marketservice.domain.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
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
        salePostRepository.save(salePost);

        return SalePostResult.fromEntity(salePost);
    }
}
