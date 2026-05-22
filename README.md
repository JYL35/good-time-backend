# 좋은 시간 (Have a Good Time)

우테코 크루들의 익명 랜덤 모임 매칭 서비스

---

## 기술 스택

- Java 21
- Spring Boot 4.0.6
- Spring Security + OAuth2 Client (GitHub 소셜 로그인)
- Spring Data JPA
- H2 (인메모리 DB)
- Lombok

---

## 실행 방법

### 1. GitHub OAuth App 생성

[GitHub Developer Settings](https://github.com/settings/developers) > OAuth Apps > New OAuth App

| 항목 | 값 |
|---|---|
| Application name | 좋은 시간 |
| Homepage URL | `http://localhost:8080` |
| Authorization callback URL | `http://localhost:8080/login/oauth2/code/github` |

### 2. 환경변수 설정

```bash
export GITHUB_CLIENT_ID=your_client_id
export GITHUB_CLIENT_SECRET=your_client_secret
```

### 3. 실행

```bash
./gradlew bootRun
```

- H2 콘솔: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:haveagoodtime`)

---

## 인증

| 동작 | URL |
|---|---|
| 로그인 (GitHub 리다이렉트) | `GET /oauth2/authorization/github` |
| 로그아웃 | `POST /api/logout` |

- 첫 로그인 시 GitHub 계정으로 자동 회원가입
- 세션 기반 인증 (Spring Security)
- 인증 불필요 엔드포인트: `GET /api/gatherings/**`

---

## API

### 모임

| 기능 | Method | URL | 인증 |
|---|---|---|---|
| 모임 목록 조회 | GET | `/api/gatherings` | X |
| 모임 상세 조회 | GET | `/api/gatherings/{id}` | X |
| 모임 생성 | POST | `/api/gatherings` | O |
| 모임 수정 | PUT | `/api/gatherings/{id}` | O (방장만) |
| 모임 삭제 | DELETE | `/api/gatherings/{id}` | O (방장만) |
| 모임 참여 | POST | `/api/gatherings/{id}/participate` | O |
| 모임 참여 해제 | DELETE | `/api/gatherings/{id}/participate` | O |
| 참여자 목록 조회 | GET | `/api/gatherings/{id}/participate` | X |

### 모임 생성/수정 요청 바디

```json
{
  "name": "치킨 같이 시킬 분",
  "headCount": 4,
  "gatheringDatetime": "2026-05-22T18:00:00",
  "dueDate": "2026-05-22T16:00:00",
  "description": "교육장 근처에서 치킨 같이 먹어요"
}
```

---

## 도메인 모델

### 모임 상태 흐름

```
RECRUITING (모집중) → MATCHED (매칭완료) → EXPIRED (시간만료)
                                         → CANCELLED (취소)
```

### ERD

```
Member
- id (PK)
- githubUserId
- crewNickname
- githubProfileImageUrl

Gathering
- id (PK)
- member_id (FK → Member)  ← 방장
- name
- headCount
- gatheringDatetime
- dueDate
- status
- description

Participant
- id (PK)
- gathering_id (FK → Gathering)
- users_id (FK → Member)
- joinTime
```

---

## 패키지 구조

```
com.wooteco.haveagoodtime
├── domain/          # 엔티티 (Gathering, Member, Participant, GatheringStatus)
├── repository/      # JPA Repository
├── service/         # 비즈니스 로직 (GatheringService)
├── controller/      # REST API (GatheringController)
├── security/        # GitHub OAuth2 설정 (SecurityConfig, CustomOAuth2UserService)
├── dto/
│   ├── request/     # GatheringCreateRequest, GatheringUpdateRequest
│   └── response/    # GatheringSummaryResponse, GatheringDetailResponse, ParticipantResponse
└── exception/       # 전역 예외 처리 (GlobalExceptionHandler)
```
