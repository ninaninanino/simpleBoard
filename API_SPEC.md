# API_SPEC.md — Simple Board 게시판 API 명세

Base URL: /api
Content-Type: application/json

==================================================
공통 규칙
==================================================

- 날짜/시간 포맷
  createdAt, updatedAt 필드는 ISO-8601 문자열을 사용
  예: 2025-12-12T21:00:00

- 페이징 규칙
  page는 0부터 시작,  size는 한 페이지당 데이터 개수

- 기본 정렬
  id desc (최신 글이 먼저 조회)

==================================================
구성
==================================================
1. 게시글 생성
2. 게시글 단건 조회
3. 게시글 목록 조회(페이징)
4. 게시글 수정
5. 게시글 삭제

==================================================
1. 게시글 생성
==================================================
Method: POST
Path: /api/posts

Request Body:
{
    "title": "제목",
    "content": "내용",
    "writer": "작성자"
}

Validation:
- title : 필수, 공백 불가, 최대 200자
- content : 필수, 공백 불가
- writer : 선택, 최대 50자

Response 200:
{
    "id": 1,
    "title": "제목",
    "content": "내용",
    "writer": "작성자",
    "createdAt": "2025-12-12T21:00:00",
    "updatedAt": "2025-12-12T21:00:00"
}

Response 400 (validation 실패):
{
    "message": "validation failed",
    "errors": [
     {
         "field": "title",
         "reason": "must not be blank"
     }
    ]
}

==================================================
2. 게시글 단건 조회
==================================================

Method: GET
Path: /api/posts/{id}

Path Variable:
- id : 게시글 ID (Long)

Response 200:
{
    "id": 1,
    "title": "제목",
    "content": "내용",
    "writer": "작성자",
    "createdAt": "2025-12-12T21:00:00",
    "updatedAt": "2025-12-12T21:00:00"
}

Response 404:
{
    "message": "post not found",
    "id": 999
}

==================================================
3. 게시글 목록 조회 (페이징)
==================================================

Method: GET
Path: /api/posts

Query Parameters:
- page (default: 0)
- size (default: 10)

Response 200:
{
    "totalCount": 23,
    "posts": [
    {
        "id": 10,
        "title": "제목10",
        "writer": "작성자",
        "createdAt": "2025-12-12T21:00:00"
    },
    {
        "id": 9,
        "title": "제목9",
        "writer": "작성자",
        "createdAt": "2025-12-12T20:00:00"
    }
]
}

==================================================
4. 게시글 수정
==================================================

Method: PUT
Path: /api/posts/{id}

Request Body:
{
    "title": "수정 제목",
    "content": "수정 내용"
}

Validation:
- title : 필수, 공백 불가, 최대 200자
- content : 필수, 공백 불가

Response 200:
{
    "id": 1,
    "title": "수정 제목",
    "content": "수정 내용",
    "writer": "작성자",
    "createdAt": "2025-12-12T21:00:00",
    "updatedAt": "2025-12-12T22:00:00"
}

Response 404:
{
    "message": "post not found",
    "id": 999
}

==================================================
5. 게시글 삭제
==================================================

Method: DELETE
Path: /api/posts/{id}

Response 204:
(no content)

Response 404:
{
    "message": "post not found",
    "id": 999
}


==================================================
DTO 매핑 참고
==================================================

- 생성 요청 : PostDtos.CreateRequest
- 수정 요청 : PostDtos.UpdateRequest
- 단건 응답 : PostDtos.Response
- 목록 응답 : PostDtos.ListResponse + PostDtos.ListItem

요청 흐름:
Request JSON
-> Request DTO
-> Controller
-> Service
-> Repository
-> Entity(DB)
-> Response DTO
-> Response JSON