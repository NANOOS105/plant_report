# 🌱 Plant Report - 식물 물주기 기록 웹사이트

$식물의 물주기 일정을 관리하고 기록하는 웹 애플리케이션입니다.

## 기술 스택

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Web
- Spring Validation
- H2 Database (개발용)
- MySQL (운영용)
- Lombok
- JUnit5
- springdoc-openapi (Swagger)

### Frontend
- Next.js 14 (App Router)
- TypeScript
- TanStack Query
- Tailwind CSS
- Axios

## 프로젝트 구조

```
plant_report/
├── backend/                 # Spring Boot 백엔드
│   ├── src/main/java/
│   │   └── project/plant_report/
│   │       ├── controller/  # REST API 컨트롤러
│   │       ├── service/     # 비즈니스 로직
│   │       ├── domain/      # 엔티티 및 리포지토리
│   │       ├── dto/         # 데이터 전송 객체
│   │       └── config/      # 설정 클래스
│   └── src/main/resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application-prod.yml
└── frontend/                # Next.js 프론트엔드
    ├── src/
    │   ├── app/            # App Router 페이지
    │   ├── components/     # React 컴포넌트
    │   ├── contexts/       # React Context
    │   ├── hooks/          # 커스텀 훅
    │   ├── lib/            # 유틸리티 함수
    │   └── types/          # TypeScript 타입 정의
    └── .env.local
```

## 설치 및 실행

### Backend 실행

$1. 백엔드 디렉토리로 이동
$```bash
$cd backend
$```

$2. Gradle 빌드 및 실행
$```bash
$./gradlew bootRun
$```

$3. 백엔드 서버가 http://localhost:8080 에서 실행됩니다.

### Frontend 실행

$1. 프론트엔드 디렉토리로 이동
$```bash
$cd frontend
$```

$2. 의존성 설치
$```bash
$npm install
$```

$3. 개발 서버 실행
$```bash
$npm run dev
$```

$4. 프론트엔드가 http://localhost:3000 에서 실행됩니다.

## 주요 기능

### 식물 관리
- 식물 등록 (이름, 물주기 간격, 계절별 간격)
- 식물 목록 조회
- 식물 정보 수정
- 식물 삭제

### 물주기 관리
- 물주기 실행 (계절별 간격 적용)
- 물주기 취소 (마지막 기록 삭제)
- 물주기 기록 자동 저장
- 물주기 필요 여부 자동 계산

### 계절 설정
- 전체 계절 설정 (공통/여름/겨울)
- 계절별 물주기 간격 자동 적용
- 실시간 계절 변경

### 데이터 관리
- H2 인메모리 데이터베이스 (개발용)
- MySQL 데이터베이스 (운영용)
- JPA Auditing (생성/수정 시간 자동 기록)

## API 문서

$백엔드 서버 실행 후 http://localhost:8080/swagger-ui.html 에서 API 문서를 확인할 수 있습니다.

### 주요 API 엔드포인트

- `GET /api/plant` - 식물 목록 조회
- `POST /api/plant` - 식물 등록
- `PUT /api/plant/{id}` - 식물 정보 수정
- `DELETE /api/plant/{id}` - 식물 삭제
- `POST /api/plant/{id}/water` - 물주기 실행
- `PUT /api/plant/{id}/cancelWater` - 물주기 취소

### 페이지 구성
$• 홈페이지 - 물주기 필요한 식물 목록
$• 식물 등록 - 새 식물 추가
$• 식물 목록 - 전체 식물 관리
$• 식물 수정 - 식물 정보 변경

### 디자인 특징
$• 반응형 디자인 (Tailwind CSS)
$• 직관적인 이모티콘 버튼
$• 실시간 상태 업데이트
$• 깔끔한 네비게이션

## 배포

### 환경 변수 설정
- `NEXT_PUBLIC_API_URL` - 프론트엔드 API URL
- `dbURL` - 데이터베이스 URL (운영용)
- `dbUsername` - 데이터베이스 사용자명
- `bPassword` - 데이터베이스 비밀번호

### 프로파일 설정
- `dev` - 개발 환경 (H2 데이터베이스)
- `prod` - 운영 환경 (MySQL 데이터베이스)

## 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해주세요.

