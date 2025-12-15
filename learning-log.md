2025.12.11
어떤 계층부터 작성하는 게 맞는가?

정해진 정답은 없지만, 가장 안정적이고 실무에서 많이 쓰는 순서는 다음과 같다.

Domain(Entity)
데이터를 어떻게 저장하고 다룰지 결정하는 가장 기초 구조다.
이것 없이 Repository, Service, Controller를 만들 수 없다.

Repository
Entity가 있어야 DB CRUD 인터페이스를 만들 수 있다.

Service
Repository를 호출해서 비즈니스 로직을 작성한다.
이 단계에서는 Entity와 Repository가 이미 준비되어 있어야 한다.

Controller
Service를 호출해 API를 제공한다.
Service가 없으면 Controller 작성 자체가 불가능하다.

Entity 없이 Service 못 만들고, Service 없이 Controller 못 만든다.
그래서 순서가 자연스럽게 본질부터 외곽으로 확장되는 구조가 된다.

정리: 
Controller 먼저 만들면 Service가 없어서 막힌다.
Service 먼저 만들면 Entity/Repository가 없어서 막힌다.
Repository는 Entity가 없으면 오류가 난다.
따라서 가장 기초인 Entity부터 만드는 게 가장 합리적이다.

***************************************************
# Learning Log

## 2025-12-12

### 오늘 한 것
- Spring Boot 게시판 프로젝트를 진행하면서 DTO, Service, static 개념을 집중적으로 정리함
- API 명세(API_SPEC.md)를 먼저 작성하고, 코드와 연결해서 이해하려는 흐름을 만듦
- 단순히 “따라 치는 코드”가 아니라, 각 코드의 **존재 이유**를 질문하고 확인함

---

### DTO에 대해 정리한 것

- DTO는 **DB(Entity)를 외부에 직접 노출하지 않기 위한 요청/응답 전용 객체**다.
- DTO는 비즈니스 로직을 가지지 않고, 데이터 전달 역할만 한다.
- PostDtos 같은 클래스는 객체가 아니라 **DTO들을 논리적으로 묶기 위한 네임스페이스 역할**이다.
- 그래서 `new PostDtos()` 같은 사용은 하지 않는다.

---

### `@NoArgsConstructor`에 대한 이해

- `@NoArgsConstructor`는  
  “이 클래스에 파라미터 없는 기본 생성자를 **자동으로 만들어달라**”는 의미다.
- 내가 직접 생성자를 쓰겠다는 선언이 아니다.
- Spring/Jackson이 JSON → 객체 변환할 때 기본 생성자가 필요해서 사용한다.
- DTO에서:
    - 기본 생성자 → 프레임워크용
    - 파라미터 생성자 / Builder → 개발자용

---

### `static`에 대해 이해한 것 (아주 낮은 레벨 기준)

- `static`의 핵심 의미는  
  **“객체를 만들지 않고 클래스 이름으로 바로 쓰기 위해서”**다.
- `static class Response`
    - PostDtos를 객체로 만들지 않고
    - `PostDtos.Response` 형태로 쓰기 위해 필요하다.
- `static Response from(Post post)`
    - Response 객체가 이미 있어야 하는 동작이 아니라
    - “Post를 Response로 변환하는 규칙”이기 때문에 static이 맞다.
- 머릿속에서는 Response.from(post)  ≒  new Response(); setXXX();
- 이렇게 번역해서 이해해도 된다.

---

### 내가 익숙한 방식 vs 요즘 방식

- 내가 익숙한 방식:
- 객체를 먼저 만들고
- setter로 하나씩 값을 채운다
- ArrayList에 add 하는 느낌
- 요즘 예제에서 많이 쓰는 방식:
- Builder / from()으로
- 객체를 한 번에 완성된 상태로 만든다
- 둘 다 자바이고, 둘 다 틀리지 않다.
- 지금 목표는 “이 방식을 좋아하는 것”이 아니라  
  **“이 방식을 읽고 이해할 수 있는 상태가 되는 것”이다.

---

### Service 클래스에 대해 깨달은 점

- Service는 Controller 메서드 모음집이 아니다.
- Service는 **비즈니스 동작(업무 단위)의 집합**이다.
- 지금은 단순 게시판이라
- Controller 메서드 수 ≈ Service 메서드 수 처럼 보이지만
- 실무에서는
- 여러 Controller가 하나의 Service 메서드를 호출하기도 한다.

---

### 느낀 점

- DTO와 Service에서 계속 막히는 이유는 실력이 부족해서가 아니라
- 기존에 익숙했던 자바 스타일과 다른 패턴을 처음 접했기 때문이라는 걸 알게 됐다.
- “왜 이렇게 쓰는지”를 이해하려고 질문하는 과정 자체가 학습이라는 걸 체감했다.


***************************************************
# Learning Log

## 2025-12-13

### 오늘 한 것
- Spring Boot 게시판 API 실행 및 전체 흐름 점검
- Gradle / JDK / IntelliJ 실행 환경 충돌 원인 파악 및 우회
- H2 Console 설정 및 접속 확인
- curl, Postman을 이용한 REST API 직접 호출
- POST → DB 저장 → GET 조회까지 데이터 흐름 검증
- DTO 검증 + Entity 제약 조건 적용 및 에러 흐름 확인

---

### 실행 환경 관련 정리

- Gradle JVM, Java toolchain, 실행 JDK는 서로 다른 역할임을 확인
  - Gradle JVM: 빌드 시 사용
  - toolchain: 컴파일 타겟 버전
  - 실행 JDK: 실제 서버 기동 시 사용
- IntelliJ에서 Build Tool을 Gradle이 아닌 IntelliJ로 변경
  - Gradle init script / daemon 문제 우회
  - 서버 정상 기동 확인
- “환경 문제와 끝까지 싸우는 것”보다  
  “동작 가능한 경로를 선택하는 것도 실력”이라는 판단 기준 정립

---

### H2 및 DB 흐름 관련 정리

- H2 Database는 서버 기동 시 정상 연결 상태였음
- H2 Console은 별도 설정 없이는 접근 불가함
- JDBC URL을 고정(`jdbc:h2:mem:testdb`)하여 디버깅 편의성 확보
- insert / select 로그를 통해 DB 접근 및 데이터 반영 여부 직접 확인
- 코드 단위가 아닌 **데이터 흐름 단위**로 동작을 인식하게 됨

---

### Spring Boot 4 / Spring 7 관련 핵심 이슈

#### @PathVariable / @RequestParam 이름 명시 필요

- 파라미터 이름을 리플렉션으로 읽을 수 없는 경우 예외 발생
- IntelliJ 빌드 환경에서는 `-parameters` 옵션 미적용 상태가 기본
- 다음과 같은 패턴이 안전함을 확인
  @PathVariable("id")
  @RequestParam(value = "page", defaultValue = "0")

- 환경 차이에 영향을 받지 않는 방식으로 컨트롤러 작성 필요성 인식

---

### REST API 호출 및 테스트 흐름 정리

- REST API는 서버 측 “규칙/약속”
- curl, Postman은 해당 API를 호출하기 위한 클라이언트 도구
- Windows PowerShell의 `curl`은 실제 curl이 아님
- IntelliJ Terminal을 Git Bash(MINGW64)로 변경
  - 리눅스 환경과 동일한 curl 사용 가능
- curl을 이용해 다음 흐름 검증
  - POST /api/posts
  - GET /api/posts
  - GET /api/posts/{id}

---

### 데이터 누락 이슈 및 원인 분석

- POST 요청 시 writer 값은 전달되었으나 응답에서 null로 확인됨
- Service 계층에서 Entity 생성 시 writer 매핑 누락 확인
- Service 매핑 수정 후 정상 반영 확인
- “요청은 오는데 저장이 안 된다” 유형의 전형적인 실무 이슈 패턴 학습

---

### 검증 로직에 대한 이해

- DTO(CreateRequest)에 @NotBlank 적용
- Entity(writer 컬럼)에 nullable = false 적용
- writer 누락 시 400 Bad Request 발생 확인
- 요청 단계 + DB 단계 이중 검증 구조 확립
- 검증 실패 시 빠르게 원인을 드러내는 구조의 중요성 인식

---

### 느낀 점

- 오늘 막힌 대부분의 문제는 코드 문법 문제가 아니라
  실행 환경, 빌드 방식, 프레임워크 버전 차이에서 발생함
- 에러 메시지를 회피하지 않고 끝까지 추적하는 경험이 큰 학습이 됨
- REST API를 “개념”이 아니라
  **터미널에서 직접 호출하고, 데이터로 확인**하면서 이해하게 됨
- 단순 게시판이지만
  실무에서 반복되는 문제 패턴을 압축적으로 경험한 하루였음

***************************************************
# Learning Log

## 2025-12-15

### 오늘 한 것
- WSL 설치 및 재부팅 후 Docker Desktop 연동 확인
- Docker 동작 확인
  - docker --version
  - docker run --rm hello-world
- MySQL 컨테이너 기동 및 접속 확인
  - docker run mysql:8
  - docker exec -it mysql8 mysql -u root -p
  - app 스키마, posts 테이블 생성 및 데이터 INSERT 확인
- Spring Boot 애플리케이션을 Docker 이미지로 빌드
  - Dockerfile 작성(멀티 스테이지 빌드)
  - .dockerignore 작성
- docker-compose로 Spring Boot + MySQL을 함께 기동
  - docker-compose.yml 작성
  - docker compose build / up 실행
  - springapp / mysql8 컨테이너 상태 확인
- 트러블슈팅(로그 기반 원인 파악 및 해결)
  - Gradle 빌드 실패(의존성 좌표 문제) 해결
  - MySQL 기동 실패(command 옵션 호환성 문제) 해결
  - JDBC 연결 실패(Public Key Retrieval) 해결
  - 없는 리소스 조회 시 500 -> 404로 개선(컨트롤러 레벨 처리)

---

## 오늘 쓴 주요 명령어 정리

### Docker 기본
- docker --version
  - Docker CLI 버전 확인

- docker info
  - Docker daemon(엔진) 상태/환경 확인

- docker ps
  - 실행 중 컨테이너 목록 확인

- docker ps -a
  - 종료된 컨테이너 포함 전체 목록 확인

- docker run --rm hello-world
  - hello-world 컨테이너를 실행해서 Docker 설치/엔진/네트워크 정상 여부 확인
  - --rm 은 실행 종료 후 컨테이너를 자동 삭제

- docker logs <컨테이너명>
  - 컨테이너 로그 확인
  - 예: docker logs springapp --tail 200

- docker exec -it <컨테이너명> <명령>
  - 실행 중 컨테이너 내부로 들어가 명령 실행
  - 예: docker exec -it mysql8 mysql -u root -p

---

## docker compose 개념 정리

### docker compose란?
- 여러 컨테이너(예: Spring, MySQL)를 "한 묶음"으로 관리하기 위한 도구
- docker-compose.yml 파일에 서비스(컨테이너), 네트워크, 볼륨, 환경변수 등을 선언해두고
  한 번의 명령으로 동시에 실행/중지/재기동/삭제를 할 수 있음
- 단일 docker run 명령을 여러 개 치는 방식보다
  환경 재현성과 협업, 실행 편의성이 크게 좋아짐

### docker-compose.yml에서 자주 보는 항목 의미
- services:
  - 실행할 컨테이너들을 나열
  - 예: app(spring), db(mysql)

- build:
  - 해당 서비스 이미지를 Dockerfile로 빌드해서 사용
  - 예: app은 build: . 로 현재 폴더의 Dockerfile을 빌드

- image:
  - Docker Hub 같은 레지스트리에서 받을 이미지 지정
  - 예: db는 image: mysql:8

- ports:
  - "호스트포트:컨테이너포트" 형태로 외부 접근 포트 매핑
  - 예: "8080:8080" 은 내 PC의 8080으로 접근하면 컨테이너 8080으로 전달

- environment:
  - 컨테이너 내부 환경 변수 설정
  - 예: SPRING_DATASOURCE_URL 같은 설정을 코드/파일 수정 없이 주입 가능

- depends_on:
  - 서비스 기동 순서/의존성 설정
  - healthcheck 조건과 같이 쓰면 db가 healthy 된 뒤 app을 띄우게 할 수 있음

- healthcheck:
  - 컨테이너가 "정상 상태"인지 판단하는 체크 로직
  - db가 healthy로 뜬 뒤 app을 띄우는 데 사용

---

## docker compose 명령어 설명

### docker compose config
- docker-compose.yml이 문법적으로 정상인지, 최종 설정이 어떻게 해석되는지 확인
- 실제 실행 전에 오류를 잡는 용도

### docker compose build
- docker-compose.yml의 build 대상 서비스 이미지를 빌드
- 예: app 서비스는 Dockerfile로 이미지가 만들어짐

### docker compose build --no-cache
- 빌드 캐시를 무시하고 완전히 새로 빌드
- 의존성/코드 변경이 캐시에 묻히거나, 이전 상태가 남았을 때 강제로 새로 만들 때 사용

### docker compose up
- compose에 정의된 서비스들을 생성하고 실행
- 기본적으로:
  - 필요한 이미지가 없으면 pull
  - 필요한 경우 build
  - 네트워크 생성
  - 컨테이너 생성 및 실행

### docker compose up -d
- -d (detached): 백그라운드로 실행하고 터미널을 반환
- 서버처럼 계속 떠 있어야 하는 경우 보통 -d를 사용

### docker compose up -d --build
- up 하기 전에 build까지 수행
- 코드 수정 후 컨테이너에 반영하려면 보통 이 옵션을 함께 사용

### docker compose up -d --build --force-recreate
- 컨테이너를 강제로 새로 생성
- "이미 실행 중인 컨테이너"가 있어도 재생성해서 변경사항이 확실히 반영되도록 함

### docker compose ps
- compose 프로젝트로 떠 있는 컨테이너 상태 확인
- 예: mysql8 healthy 여부, springapp up 여부 확인

### docker compose down
- compose로 띄운 컨테이너/네트워크 정리(중지 및 삭제)
- 이미지 자체는 기본적으로 남음

### docker compose down -v
- down + 볼륨까지 삭제
- DB 같은 서비스는 볼륨에 데이터가 남기 때문에
  완전 초기화가 필요할 때 사용
- 주의: -v 하면 데이터도 같이 삭제됨

---

## 오늘 발생한 문제와 해결 요약

### 1) Gradle 빌드 실패: MySQL 커넥터 의존성 좌표 오류
- 증상
  - Could not find mysql:mysql-connector-j:8.4.0
- 원인
  - groupId가 잘못된 의존성 좌표 사용
- 해결
  - runtimeOnly 'com.mysql:mysql-connector-j:8.4.0' 로 수정
  - docker compose build --no-cache 로 재빌드

### 2) MySQL 컨테이너 기동 실패: 잘못된 mysqld 옵션
- 증상
  - unknown variable 'default-authentication-plugin=mysql_native_password'
  - data directory unusable
- 원인
  - MySQL 8.4에서 해당 옵션이 더 이상 유효하지 않아 초기화 실패
- 해결
  - docker-compose.yml에서 해당 command 옵션 제거
  - docker compose down -v 로 볼륨까지 초기화 후 재기동

### 3) Spring -> MySQL 연결 실패: Public Key Retrieval is not allowed
- 증상
  - JDBCConnectionException / Public Key Retrieval is not allowed
- 원인
  - MySQL 기본 인증 방식(caching_sha2_password)에서 공개키 조회가 차단되어 접속 실패
- 해결
  - JDBC URL에 allowPublicKeyRetrieval=true 추가
  - docker compose up -d --force-recreate 로 재기동

### 4) 없는 ID 조회 시 500 발생
- 증상
  - GET /api/posts/{id} 에서 존재하지 않는 id 요청 시 500
- 원인
  - 서비스에서 예외를 던졌지만 HTTP 404로 변환하는 처리가 없어서 500으로 응답
- 해결
  - Global handler 없이, 컨트롤러 메서드에서 try/catch로 404 응답 처리
  - 재빌드 및 재기동 후 404 응답 확인

---

## 결과(오늘 완료 기준)
- Docker Desktop + WSL 기반 개발 환경 구축 완료
- Spring Boot + MySQL을 docker-compose로 함께 구동 완료
- DB 연결 및 테이블 생성, 데이터 입력 확인 완료
- API 엔드포인트 확인
  - /api/posts 정상 응답 확인
  - 없는 리소스 요청 시 404 응답 확인
- Week 1 목표 달성
