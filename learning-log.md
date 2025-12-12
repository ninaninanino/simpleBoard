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
