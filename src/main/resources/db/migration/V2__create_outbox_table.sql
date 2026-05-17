CREATE TABLE p_outbox
(
    id                   UUID         PRIMARY KEY,
    aggregate_type       VARCHAR(50)  NOT NULL,
    aggregate_id         UUID         NOT NULL,
    event_type           VARCHAR(100) NOT NULL,
    topic                VARCHAR(255) NOT NULL,
    payload              JSONB        NOT NULL,
    published            BOOLEAN      NOT NULL DEFAULT FALSE,
    publishing           BOOLEAN      NOT NULL DEFAULT FALSE,
    publishing_started_at TIMESTAMP,
    published_at         TIMESTAMP,
    failure_count        INT          NOT NULL DEFAULT 0,
    last_failed_at       TIMESTAMP,
    last_failure_message TEXT,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 미발행 건 폴링용
CREATE INDEX idx_p_outbox_unpublished
    ON p_outbox (created_at)
    WHERE published = FALSE
      AND publishing = FALSE;

-- aggregate 기준 조회용
CREATE INDEX idx_p_outbox_aggregate
    ON p_outbox (aggregate_type, aggregate_id);
