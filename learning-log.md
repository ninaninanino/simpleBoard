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

