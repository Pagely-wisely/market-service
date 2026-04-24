package com.pagely.marketservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_sale_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePost {

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
    private Integer price;

    // 판매글 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SalePostStatus status;

    // 책 상태
    @Column(nullable = false, length = 20)
    private String condition;

    // 공통 감사 컬럼
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, columnDefinition = "uuid")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", columnDefinition = "uuid")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", columnDefinition = "uuid")
    private UUID deletedBy;

}
