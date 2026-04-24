-- UUID 사용을 위한 extension (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================
-- 판매글
-- =========================
CREATE TABLE p_sale_post (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL,
    book_id VARCHAR(20) NOT NULL,

    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price INT NOT NULL,

    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD', 'DELETED')),
    condition VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,
    deleted_at TIMESTAMP,
    deleted_by UUID
);

-- =========================
-- 주문
-- =========================
CREATE TABLE p_order (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

     buyer_id UUID NOT NULL,
     sale_post_id UUID NOT NULL,

     status VARCHAR(20) NOT NULL CHECK (
         status IN ('PENDING', 'ACCEPTED', 'SHIPPING', 'COMPLETED', 'CANCELLED', 'FAILED')
         ),

     price INT NOT NULL,

     tracking_number VARCHAR(50),
     courier_company VARCHAR(50),
     tracking_registered_at TIMESTAMP,

     created_at TIMESTAMP NOT NULL DEFAULT NOW(),
     created_by UUID NOT NULL,
     updated_at TIMESTAMP,
     updated_by UUID,
     deleted_at TIMESTAMP,
     deleted_by UUID,

     CONSTRAINT fk_order_sale_post
         FOREIGN KEY (sale_post_id) REFERENCES p_sale_post(id)
);

-- =========================
-- 주문 이력
-- =========================
CREATE TABLE p_order_history (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

     order_id UUID NOT NULL,

     from_status VARCHAR(20) NOT NULL,
     to_status VARCHAR(20) NOT NULL,

     reason VARCHAR(200) NOT NULL,

     created_at TIMESTAMP NOT NULL DEFAULT NOW(),
     created_by UUID NOT NULL,
     updated_at TIMESTAMP,
     updated_by UUID,
     deleted_at TIMESTAMP,
     deleted_by UUID,

     CONSTRAINT fk_order_history_order
         FOREIGN KEY (order_id) REFERENCES p_order(id)
);
