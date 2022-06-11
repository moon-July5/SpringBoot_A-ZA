# ⭐ A-ZA Board
> 웹의 기본 소양이 되는 CRUD 게시판을 구현했습니다.  
> 링크
<br>  

## 1.제작기간 & 참여인원  
- 2022.03.02 ~ 2022.06.07
- 개인 프로젝트
<br>

## 2.사용한 기술
- JAVA 11
- Spring Boot 2.6.3
- Gradle 7.4
- MySQL 8.0
- Spring Data JPA
- QueryDSL
- Spring Security
- BootStrap 5.1.3
- AWS EC2, S3, RDS
<br>

## 3.ERD 설계
<details>
<summary>ERD 이미지</summary>

<img src="https://user-images.githubusercontent.com/60730405/172790041-2d38a29b-210e-4e35-b77f-08b791484a8a.png" height="650px" width="750px">
</details>  

<br>

## 4.핵심 기능
이 프로젝트는 게시판 서비스를 제공하고 있기 때문에 핵심 기능은 게시글의 **등록**, **조회**, **수정**, **삭제** 동작입니다.  

<details>
<summary>핵심 기능 설명</summary>

### 4.1 전체 흐름
전체 흐름은 다음과 같습니다.  
이 흐름 안에서 **등록**, **조회**, **수정**, **삭제** 동작이 진행됩니다.  
<img src="https://user-images.githubusercontent.com/60730405/172858424-705cd5b1-33ca-4581-9f90-89e26eb68d6a.JPG" height="200px">

<details>
<summary>게시글 등록</summary>

#### Controller
<img src="https://user-images.githubusercontent.com/60730405/172861164-168637bb-9cef-4db2-b50e-b7a508f8ba09.JPG" height="400px">  
  
- BoardController에는 View에서 넘어온 데이터를 전달받아 처리합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d1c11ae1d7b3c8dd3141cb1908a8817f510a44c0/src/main/java/com/moon/aza/controller/BoardController.java#L81)
- 여기서 tid 변수는 `임시저장된 게시글 번호`입니다. 이것을 왜 따로 요청받아 처리하냐면 임시저장된 게시글을 불러와서  
완전히 작성 후, 실제 저장하게 된다면 임시저장된 게시글은 더 이상 사용하지 않을 것이기 때문에 삭제하기 위함입니다.  
  ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d1c11ae1d7b3c8dd3141cb1908a8817f510a44c0/src/main/java/com/moon/aza/controller/BoardController.java#L84)  
- 게시판으로 리다이렉트하도록 응답합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d1c11ae1d7b3c8dd3141cb1908a8817f510a44c0/src/main/java/com/moon/aza/controller/BoardController.java#L87)   

#### Service
<img src="https://user-images.githubusercontent.com/60730405/172870015-8e133740-7e6a-4b0a-9552-8f468bd3b972.JPG" height="450px">
  
**Entity 형태로 변환** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d452c5def66ce0e2acb70a6ed7d56c31b9c5cb38/src/main/java/com/moon/aza/service/BoardService.java#L81)
- `BoardRepository`에 전달할 수 있도록 요청받은 DTO -> Entity 형태로 변환하는 메서드입니다.  

**실제 DB에 저장** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d452c5def66ce0e2acb70a6ed7d56c31b9c5cb38/src/main/java/com/moon/aza/service/BoardService.java#L74)  
- `saveBoard()` 메서드를 통해 Entity 형태로 변환된 데이터를 `BoardRepository`로 전달합니다.  

#### Repository
<img src="https://user-images.githubusercontent.com/60730405/172872998-4599320b-3368-4753-9108-a1d05de0aa89.JPG" height="400px" width="800px">  

- Entity 형태의 데이터를 `save()` 메서드를 통해 실제 Database에 전달하여 저장합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/d452c5def66ce0e2acb70a6ed7d56c31b9c5cb38/src/main/java/com/moon/aza/service/BoardService.java#L78)   
</details> 
  
<details>
<summary>게시글 조회</summary>

#### Controller
<img src="https://user-images.githubusercontent.com/60730405/172997868-e2e326b8-7a41-474b-93bd-b2560b5af39a.JPG" height="350px">

- 조회한 게시물의 내용을 가져오고 View에 전달합니다.  ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/5bc189ded69c76239ac98b0a18e0f62f8152015b/src/main/java/com/moon/aza/controller/BoardController.java#L67)  
- 사용자를 나타내는 `member`가 null이 아닐 경우, 사용자가 조회한 게시물의 추천을 눌렀는지 확인하여 반영하도록 합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/5bc189ded69c76239ac98b0a18e0f62f8152015b/src/main/java/com/moon/aza/controller/BoardController.java#L69)  
  
#### Service
<img src="https://user-images.githubusercontent.com/60730405/173000216-e20f98e0-cd69-4b51-a7f1-01a4624c8882.JPG" height="450px">
  
**실제 DB에서 조회** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/5bc189ded69c76239ac98b0a18e0f62f8152015b/src/main/java/com/moon/aza/service/BoardService.java#L93)  
- `BoardRepository`의 `getBoardWithAll()` 메서드로 조회한 게시글의 데이터들(게시글 내용, 댓글 수, 추천 수)을 모두 가져오도록 합니다.  

**DTO 형태로 변환** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/5bc189ded69c76239ac98b0a18e0f62f8152015b/src/main/java/com/moon/aza/service/BoardService.java#L160)
- 위에서 조회한 데이터들을 DTO 형태로 변환한 후에 반환하도록 합니다.  
  
#### Repository
<img src="https://user-images.githubusercontent.com/60730405/173001248-1451ef32-24a2-4609-9e0d-932366f70326.JPG" height="300px" width="800px"> 

- `@Query` 애너테이션으로 특정 게시물의 데이터를 조회하는 쿼리를 구현했습니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/5bc189ded69c76239ac98b0a18e0f62f8152015b/src/main/java/com/moon/aza/repository/BoardRepository.java#L28)
- 조회할 데이터는 게시물, 댓글 수, 추천 수이며, `left outer join`으로 기준 테이블인 `Board`와 대상 테이블인 `Comment`, `Likes`의 데이터를 누락 없이 가져오기 위함입니다.  
- `List<Object[]>` 형태로 반환하도록 합니다.  
</details>
  
<details>
<summary>게시글 수정/삭제</summary>

#### Controller
<img src="https://user-images.githubusercontent.com/60730405/173071970-d8207356-3007-49cc-8890-f39935c849de.JPG" height="420px" width="800px">

**게시글 수정**
- View에서 수정된 데이터를 받아 동작을 진행합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/controller/BoardController.java#L93)  
- 수정이 정상적으로 동작하면 수정한 게시글로 리다이렉트하도록 합니다. 이때 페이지 번호와 게시글의 번호도 같이 전달하도록 합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/controller/BoardController.java#L98)  

**게시글 삭제**
- View에서 게시글의 번호를 받아 삭제 동작을 진행합니다. 성공적으로 삭제가 되면 게시판으로 리다이렉트합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/controller/BoardController.java#L102)

#### Service
<img src="https://user-images.githubusercontent.com/60730405/173076371-bdfbf34f-dccd-48fe-8e88-525fe476f280.JPG" height="370px">

**게시글 수정**
- 수정할 게시글이 존재하는지 조회합니다. 존재하면 `Title`과 `Contents`를 수정 후 다시 `BoardRepository`로 전달하여 저장합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/service/BoardService.java#L104)

**게시글 삭제**
- 게시글의 번호를 `BoardRepository`로 전달하여 DB에 삭제 요청합니다. ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/service/BoardService.java#L118)

#### Repository
<img src="https://user-images.githubusercontent.com/60730405/173079959-c707d992-0358-4993-9aa8-24ef7b0d7e66.JPG" height="370px">

**수정할 게시글 조회** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/service/BoardService.java#L105)
- `findById()` 메서드를 통해 특정 게시글의 번호로 DB에서 `Board`를 조회합니다.  

**게시글 수정 후 저장** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/service/BoardService.java#L113)
- `save()` 메서드를 통해 수정된 데이터를 다시 DB에 저장합니다.  

**게시글 삭제** ⭐ [코드 확인](https://github.com/moon-July5/SpringBoot_A-ZA/blob/6f9a7bdbdb3df91a04bfb2c2eda25e1cbb41af2b/src/main/java/com/moon/aza/service/BoardService.java#L118)
- `deleteById()` 메서드로 특정 게시글의 번호를 통해 DB에서 삭제합니다.
</details>
</details>

<br>
  
## 5.핵심 트러블 슈팅
### 5-1 검색 처리 문제
- 게시판에서 검색 기능을 구현하기 위해 `QueryDSL`을 사용했습니다. 사용한 이유는 `QueryDSL`을 직접 사용하면서 공부하기 위한 것도 있지만  
  검색 기능을 구현하는 데 복잡한 쿼리를 해결하기 위해 `QueryDSL`을 사용해야 하는 것이 아닐까 하는 생각으로 선택했습니다.  
  
- `Repository`에서 where 문의 조건을 나타내는 `Predicate` 파라미터를 이용하여 메서드를 구현하였으며, `BooleanBuilder` 형태의 데이터를  
  전달인자로 값을 전달하여 검색 조건을 처리하였습니다.  
- 하지만 검색 기능은 동작하지않고 계속 그 틀 안에서만 해결하려고 했습니다.  
- `Predicate`는 조인이 필요한 쿼리는 결과를 가져오기 힘들다는 것을 알게 되었습니다.   

**문제 해결**

- 조인이 필요한 쿼리는 Spring Data JPA에서 제공하는 `QuerydslRepositorySupport`으로 해결할 수 있었습니다.  
- `QueryDSL`의 **Q도메인**과 `JPQL`을 이용하여 쿼리 문을 작성하였습니다.  
- `Sort`같은 경우는 `new OrderSpecifier`를 이용하여 처리하였습니다.  
<details>
<summary>코드</summary>

```java
   /**
   * 게시글 검색 기능
   */
   @Override
    public Page<Object[]> searchBoard(BooleanBuilder booleanBuilder, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QLikes qLikes = QLikes.likes;

        JPQLQuery<Board> jpqlQuery = from(qBoard);
        jpqlQuery.leftJoin(qComment).on(qComment.board.eq(qBoard));
        jpqlQuery.leftJoin(qLikes).on(qLikes.board.eq(qBoard));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qBoard, qComment.countDistinct(), qLikes.countDistinct());
        tuple.where(booleanBuilder);
        tuple.groupBy(qBoard);

        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String property = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        long count = tuple.fetchCount();

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        Page<Object[]> page = new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable, count);
        return page;
    }
```
</details>

### 5-2 회원 삭제 문제
- 회원을 삭제 하기위해 회원(Member)과 관련된 Board Entity 또한 삭제하기 위해 Member Entity의 `@OneToMany`으로 연결된 Board Entity에  
  `orphanRemoval=true` 속성을 추가하였습니다.  
- `orphanRemoval=true` 속성을 사용한 이유는 NULL이 된 자식을 Delete 해주는 속성으로 알고있기 때문에 Member를 삭제하면 연결된 board Entity가 NULL이 되기 때문에 자동으로 삭제될 줄 알았습니다.  
- 하지만 `외래키 제약 조건`에 대한 예외가 발생하여 삭제가 되지 않았습니다.  
- Board Entity에 댓글(Comment), 추천(Likes) Entity 또한 연결되어 있기 때문에 삭제가 되지 않는다는 것을 알았습니다.  
  
**문제 해결**  
  
- Board Entity와 연관된 Comment, Likes를 먼저 삭제해주는 방향으로 해결하였습니다.  
- 먼저 Member의 id 값으로 연관된 데이터를 조회한 후에 존재하면 삭제합니다.  
<details>
<summary>코드</summary>

```java
   /**
   * 회원 삭제
   */
  public void deleteMember(Member member){
        List<Long> boardIds = boardRepository.findByMemberId(member.getId());
        List<Long> likesIds = likesRepository.findByMemberId(member.getId());
        List<Long> commentIds = commentRepository.findByMemberId(member.getId());
        if(!commentIds.isEmpty()){
            commentRepository.deleteAllByIdIn(commentIds);
        }
        if(!likesIds.isEmpty()){
            likesRepository.deleteAllByIdIn(likesIds);
        }
        if(!boardIds.isEmpty()){
            boardRepository.deleteAllByIdIn(boardIds);
        }
        memberRepository.deleteById(member.getId());
    }
```
</details>

## 6.기타 트러블 슈팅

