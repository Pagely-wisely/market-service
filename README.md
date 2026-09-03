# market-service

Pagely 플랫폼의 중고책 거래 마이크로서비스입니다.
판매글(SalePost) 등록·조회와 주문(Order) 생성·처리를 담당합니다.

---

## 프로젝트 설명

Pagely는 독서 모임 커뮤니티와 읽은 도서를 거래할 수 있는 종합 독서 플랫폼입니다.
독서에 대한 관심 증가와 독서 모임 문화 확산에 맞춰, 누구나 쉽게 독서 모임에
참여하고 지속적인 독서 습관을 형성할 수 있도록 기획했습니다.

핵심적으로 다음 세 가지 문제를 해결합니다.

- 긍정적인 독서 습관 형성 — 독서 모임 및 활동 관리를 통해 자연스러운 독서 참여를
  유도하고 동기를 부여합니다.
- 독서 진입 장벽 완화 — 읽은 책에 대한 중고 거래로 독서 비용을 절감하고
  선순환 구조를 형성합니다.
- AI 기반 독서 흥미 유발 — 독후감·활동 데이터를 기반으로 개인화 도서 추천과
  독후 요약을 제공합니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| DB | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Migration | Flyway |
| Message Broker | Apache Kafka |
| Service Discovery | Spring Cloud Netflix Eureka Client |
| HTTP Client | Spring Cloud OpenFeign |
| 공통 라이브러리 | com.pagely:common:2.0.1 (GitHub Packages) |
| 테스트 | JUnit 5, Testcontainers (PostgreSQL) |
| 기타 | Lombok |

---

## 실행 방법

### 사전 요구사항

- Docker (PostgreSQL, Kafka 컨테이너 실행용)
- JDK 21
- GitHub Packages 접근 권한 (`gpr.user`, `gpr.key`)

### 1. 환경변수 설정

`src/main/resources/.env` 파일을 생성합니다.

```properties
DB_HOST=localhost
DB_PORT=5432
MARKET_DB_NAME=market_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 2. GitHub Packages 인증 설정

`~/.gradle/gradle.properties`에 아래를 추가합니다.

```properties
gpr.user=<GitHub_Username>
gpr.key=<GitHub_PAT>
```

또는 환경변수로 export합니다.

```bash
export GPR_USER=<GitHub_Username>
export GPR_KEY=<GitHub_PAT>
```

### 3. 의존 서비스 실행

PostgreSQL과 Kafka가 실행 중이어야 합니다.

```bash
# PostgreSQL
docker run -d \
  --name market-postgres \
  -e POSTGRES_DB=market_db \
  -e POSTGRES_USER=your_username \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 \
  postgres:16

# Kafka (KRaft 모드)
docker run -d \
  --name market-kafka \
  -e KAFKA_CFG_NODE_ID=1 \
  -e KAFKA_CFG_PROCESS_ROLES=broker,controller \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -p 9092:9092 \
  bitnami/kafka:latest
```

### 4. 빌드 및 실행

```bash
# 빌드
./gradlew bootJar

# 실행 (포트 19031)
./gradlew bootRun
```

### 5. 테스트

```bash
# 전체 테스트 (Testcontainers — Docker 데몬 실행 필요)
./gradlew test

# 단일 테스트 클래스
./gradlew test --tests "com.pagely.marketservice.concurrency.ConcurrentOrderTest"
```

---

## 주요 구현

### 판매글(SalePost) 상태 머신

판매글은 중고책 한 권에 대한 단일 아이템 게시글입니다. 재고 수량 대신 상태로 가용 여부를 관리합니다.

```
AVAILABLE → RESERVED → SOLD
```

- `AVAILABLE`: 구매 가능
- `RESERVED`: 주문 생성으로 예약된 상태
- `SOLD`: 거래 완료

주문 생성 시 `RESERVED`로 전환되고, 주문 취소 시 `AVAILABLE`로 복구됩니다.

### 주문(Order) 상태 머신

```
PENDING → ACCEPTED → SHIPPING → COMPLETED
                ↘
             CANCELLED
```

- `PENDING`: 주문 생성 직후 (결제 대기)
- `ACCEPTED`: `payment-completed` Kafka 이벤트 수신 후 결제 완료
- `SHIPPING`: 판매자가 운송장 번호를 등록한 상태
- `COMPLETED`: 구매자 구매 확정
- `CANCELLED`: 구매자 취소 (PENDING 또는 ACCEPTED 상태에서만 가능)

상태 전환마다 `OrderHistory`에 이력이 누적됩니다.

### API 엔드포인트

**판매글**

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| `POST` | `/api/v1/sale-posts` | 판매글 등록 | 필요 |
| `GET` | `/api/v1/sale-posts` | 판매글 목록 조회 (페이지네이션) | 불필요 |
| `GET` | `/api/v1/sale-posts/{salePostId}` | 판매글 상세 조회 | 불필요 |

**주문**

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| `POST` | `/api/v1/orders` | 주문 생성 | 필요 |
| `GET` | `/api/v1/orders/{orderId}` | 주문 상세 조회 | 필요 |
| `POST` | `/api/v1/orders/{orderId}/tracking` | 운송장 번호 등록 (판매자) | 필요 |
| `POST` | `/api/v1/orders/{orderId}/confirm` | 구매 확정 (구매자) | 필요 |
| `POST` | `/api/v1/orders/{orderId}/cancel` | 주문 취소 (구매자) | 필요 |

### Kafka 이벤트

| 방향 | 토픽 | 설명 |
|------|------|------|
| 발행 | `order-created` | 주문 생성 완료 |
| 발행 | `order-cancelled` | 주문 취소 (결제 취소 트리거) |
| 구독 | `payment-completed` | 결제 완료 수신 → 주문 `ACCEPTED` 처리 |

### Outbox 패턴 기반 이벤트 발행

DB 트랜잭션과 Kafka 발행의 원자성을 보장하기 위해 Outbox 패턴을 적용했습니다.

```
OrderService
  → OrderEvents (인터페이스)
  → SpringOrderEventPublisher (Spring ApplicationEvent 발행)
  → OrderEventHandlerAdapter (@TransactionalEventListener BEFORE_COMMIT)
      → OutboxManageService.saveOutbox()   ← 주문 INSERT와 동일 트랜잭션
          → OutboxPoller (5초마다 100건 배치)
              → KafkaEventPublisher.publish()
```

다중 인스턴스 환경에서 중복 발행을 방지하기 위해 `SELECT ... FOR UPDATE SKIP LOCKED`로 각 인스턴스가 서로 다른 행을 선점합니다.

### 비관적 락(Pessimistic Lock)을 이용한 동시 주문 제어

동시에 여러 구매자가 같은 판매글에 주문을 시도할 경우, `READ_COMMITTED` 격리 수준에서 Lost Update가 발생합니다.
`SELECT ... FOR UPDATE`로 판매글 행에 배타 락을 획득해 하나의 판매글에 주문이 정확히 1건만 생성되도록 보장합니다.

Testcontainers 기반 통합 테스트로 5개 스레드 동시 요청 → 1건 성공을 검증합니다.

---

## 트러블슈팅

### 주문 및 결제 처리 흐름
- Market Service가 주문을 생성하고, Payment Service와 Kafka 메시지로 통신하는
  이벤트 기반 구조로 결제를 처리.
- 흐름: ① 사용자가 구매 버튼 클릭 → ② Toss 결제 위젯을 통해 결제 요청 진행 →
  ③ 결제 승인 완료 처리. Market Service는 주문 생성 이벤트를 발행하고,
  Payment Service의 결제 결과 이벤트를 수신함.

### 주문 데이터 정합성 보장 및 메시지 전달 안정성 향상 — Outbox 패턴
- 문제: 기존 구조는 주문 테이블 INSERT 후 트랜잭션 커밋, 그다음 Kafka로 직접
  발행(AFTER_COMMIT)하는 방식이라 DB 트랜잭션 밖에서 발행이 일어나 원자성이
  보장되지 않음. Kafka 발행 실패 시 이벤트가 유실되어 결제 서비스가 주문 생성
  사실을 알 수 없고 정합성이 깨짐.
- 해결: Outbox 패턴 도입. 주문 테이블 INSERT와 아웃박스 테이블 INSERT를
  같은 트랜잭션(BEFORE_COMMIT)으로 묶어 원자성을 보장하고, 별도 OutboxPoller가
  Kafka로 메시지를 발행. 발행 실패 시 다음 폴링에서 재시도하여
  "DB 커밋 = 이벤트 저장 보장"을 달성.
- 구현 방식:
  - 중복 발행 방지: SELECT ... FOR UPDATE SKIP LOCKED로 다른 인스턴스가
    선점 중인 행을 건너뛰고, 조회와 동시에 publishing = true로 마킹 후 즉시 커밋해
    다른 인스턴스의 쿼리 조건에서 제외.
  - 스케줄러 기반 폴링: 5000ms마다 100건씩 미발행 이벤트를 배치 조회하여 처리.
    kafkaTemplate.send().whenComplete()로 비동기 처리해 스케줄러 스레드를
    블로킹하지 않으면서, 발행 성공 시 published = true, 실패 시 failureCount를
    증가시키고 다음 폴링에서 재시도.

---

## 데모 / 이미지

<!-- TODO: 서비스 시연 GIF 또는 스크린샷 추가 -->

<!-- TODO: API 문서(Swagger/Notion 등) 링크 추가 -->

<!-- TODO: 배포 URL 추가 -->
