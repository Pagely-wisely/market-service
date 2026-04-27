package com.pagely.marketservice.domain.model;

import com.pagely.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_sale_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 판매자 ID
    @Column(name = "seller_id", nullable = false, columnDefinition = "uuid")
    private UUID sellerId;

    // 도서 ID
    @Column(name = "book_id", nullable = false, length = 20)
    private String bookId;

    // 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 본문
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // 판매 가격
    @Column(nullable = false)
    private int price;

    // 판매글 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SalePostStatus status;

    // 책 상태
    @Column(nullable = false, length = 20)
    private String condition;

    public static SalePost create(
            UUID sellerId,
            String bookId,
            String title,
            String description,
            int price,
            String condition
    ) {
        if (price <= 0) {
            throw new IllegalArgumentException("판매 가격은 0보다 커야합니다.");
        }

        SalePost salePost = new SalePost();
        salePost.sellerId = sellerId;
        salePost.bookId = bookId;
        salePost.title = title;
        salePost.description = description;
        salePost.price = price;
        salePost.condition = condition;
        salePost.status = SalePostStatus.AVAILABLE;

        return salePost;
    }
}
