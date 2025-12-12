Spring Boot app이 실행되면:
톰캣(WAS) 뜨고
스프링 컨텍스트가 모든 빈(bean)을 생성하고
@Controller, @Service, @Repository 를 스캔해서 메모리에 올리고
HTTP 요청이 들어오면  
→ 어떤 Controller가 매핑되는지 찾아서  
→ Service 호출 → Repository 호출 → DB 접근 → 결과 DTO로 반환

controller  : HTTP 입구, 요청 매핑
service     : 비즈니스 로직, 트랜잭션
repository  : Entity ↔ DB 통신 (JPA)
domain      : 데이터 모델 + 비즈니스 행동
dto         : 요청/응답 전용 객체 (캡슐화)


domain : 데이터+데이터가 해야할 행동
entity : domain 구현체(db와 매핑)
repository : JPA 인터페이스, domain(entity)와 DB 통로
= Service가 DB에 접근할 때 사용하는 계층
dto : 요청/응답 전용 객체. 캡슐화된 record.

controller : HTTP 입구
service : 트랜잭션, 비즈니스 로직

[요청 DTO]  →  Controller  →  Service  →  Repository  →  Entity(DB)
↑                                                            ↓
└──────────────────────────[응답 DTO]────────────────────────┘


##########################################################



# 📘 Simple Board Project (Spring Boot + JPA)

간단한 게시판 기능을 구현하며 **Spring Boot 기본 구조**,  
**JPA 엔티티/레포지토리 개념**, **계층 아키텍처**, **DTO 흐름**을 학습하는 프로젝트입니다.

---

# 🚀 애플리케이션 실행 흐름

Spring Boot 애플리케이션(`SimpleBoardApplication`)이 실행되면 다음 순서로 동작합니다.

1. 내장 Tomcat(WAS) 기동
2. Spring Context 생성
3. `@Controller`, `@Service`, `@Repository` 등 모든 빈(Bean)을 스캔해 메모리에 등록
4. 클라이언트가 HTTP 요청을 보내면 아래 순서로 처리
    - Controller → Service → Repository → Entity(DB)
5. 응답은 DTO로 가공되어 클라이언트로 반환됨

---

# 🧱 계층 구조 설명

프로젝트는 다음과 같은 Layered Architecture로 구성됩니다.

controller  : HTTP 입구 (요청 매핑)  
service     : 비즈니스 로직, 트랜잭션  
repository  : Entity ↔ DB 통신 (JPA)  
domain      : 핵심 데이터 모델 + 비즈니스 동작  
dto         : 요청/응답 전용 객체 (캡슐화)

---

# 🧠 핵심 개념 정리

## Domain / Entity
- Domain: “데이터 + 데이터가 해야 할 행동(비즈니스 로직)”
- Entity: Domain의 구현체. DB 테이블과 매핑되는 실제 객체
- 예: Post, Member, Order 등
- 엔티티에는 비즈니스 동작이 포함될 수 있음  
  (ex: `post.update(title, content)`)

---

## Repository
- JPA가 자동으로 구현해주는 인터페이스
- Service가 DB에 접근할 때 사용하는 계층
- Entity 저장, 조회, 수정, 삭제 담당
- SQL 없이도 CRUD 수행 가능

---

## Service
- 비즈니스 로직을 수행하는 계층
- 트랜잭션 처리
- Repository 호출하여 데이터 조작
- 예:  Post post = Post.create(req.getTitle(), req.getContent(), req.getWriter());
  postRepository.save(post);


---

## Controller
- HTTP 요청이 처음 들어오는 입구
- Request DTO를 받아 Service 호출
- Service 결과를 Response DTO로 변환 후 응답

---

## DTO (Data Transfer Object)
- 요청/응답 전용 객체
- 엔티티를 외부에 노출하지 않기 위한 **캡슐화** 역할
- 계층 간 데이터 전달을 위한 “안전한 포장지”
- 정보 은닉(Information Hiding) 실천

---

# 🔄 전체 요청 흐름

[Request JSON]  
↓ 변환  
[Request DTO]  
↓  
Controller  
↓  
Service  
↓  
Repository  
↓  
Entity(DB)  
↓  
[Response DTO]  
↓ 변환  
[Response JSON]

---

# 📁 프로젝트 구조

src  
└─ main  
└─ java  
└─ com.example.simpleBoard  
├─ controller  
├─ service  
├─ repository  
├─ domain  
└─ dto

---

# ✔ 목표

이 프로젝트를 통해 다음을 명확히 이해하는 것이 목표입니다.

- Spring Boot 앱 실행 원리
- MVC + Layered Architecture 구조
- Domain(Entity) · Repository · Service · Controller의 역할
- DTO를 사용하는 이유(캡슐화, 정보 은닉)
- JPA 기반 CRUD 데이터 흐름

---

# 📌 상태

게시글 **생성 / 조회 / 목록 / 수정 / 삭제** 기능 구현 완료.  
다음 단계는 Docker + MySQL + Render 배포까지 확장 가능합니다.


----

엔티티(Post.java) 쪽

@Entity
→ 이 클래스는 JPA가 관리하는 엔티티다 (DB 테이블과 매핑)

@Table(name = "posts")
→ 실제 DB 테이블 이름 지정

@Id
→ PK

@GeneratedValue(strategy = GenerationType.IDENTITY)
→ PK 자동 증가

@Column
→ 컬럼 제약 조건 (nullable, length 등)

@Lob
→ TEXT 같은 대용량 컬럼

@CreationTimestamp
→ insert 시 자동으로 현재 시간 세팅

@UpdateTimestamp
→ update 시 자동으로 현재 시간 세팅

DTO(PostDtos.java) 쪽

@Getter
→ getter 자동 생성 (DTO는 setter 거의 안 씀)

@NoArgsConstructor
→ 기본 생성자 (Spring/Jackson이 객체 만들 때 필요)

@Builder
→ 객체를 “필드 명시형”으로 안전하게 생성

@NotBlank
→ 요청 값 검증 (null / 빈 문자열 / 공백 불가)

@Size(max = n)
→ 요청 값 길이 제한

Service / Controller 쪽

@Service
→ 비즈니스 로직 계층 Bean

@Transactional
→ 트랜잭션 경계 설정

@Transactional(readOnly = true)
→ 조회 전용 트랜잭션 (성능 최적화)

@RestController
→ HTTP 요청/응답 담당

@RequestBody
→ JSON → 객체 변환

@Valid
→ DTO에 붙은 validation 실행