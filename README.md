# ⭐ A-ZA Board
> 웹의 기본 소양이 되는 CRUD 게시판을 구현했습니다.  
> [azaboard.ga](http://azaboard.ga)
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

<br>
  
## 6.기타 트러블 슈팅
<details>
<summary>로컬에서 개발 시 MySQL Timezone 문제</summary>

로컬에서 MySQL을 통해 Database를 개발하게 되면 데이터 저장 시 시간이 `Asia/Seoul`로 되지 않고  
`System`으로 되어 있어 시간이 한국의 현재 시간과 일치하지 않는 문제가 발생했습니다.  
  
이 Timezone을 `Asia/Seoul`로 변경하기 위해 [링크](https://dev.mysql.com/downloads/timezones.html)에 접속하여 운영체제와 MySQL 버전에 맞게 다운합니다.  
다운받은 파일을 `MySQL Workbench`에서 쿼리를 실행합니다.  
쿼리가 성공적으로 실행이 됐다면 아래의 코드를 입력하여 Timezone을 `Asia/Seoul`로 설정합니다.  
```
SET global time_zone = 'Asia/Seoul';
SET time_zone = 'Asia/Seoul';
```
</details>
  
<details>
<summary>네이버 이메일 전송 에러</summary>

회원가입 후 이메일 인증을 하기 위해 사용자에게 이메일을 보내는 데 STMP 전송 오류가 발생했습니다.  

오류가 발생한 이유는 제 네이버 계정에 2차 인증이 걸려있었기 때문에 제 이메일을 통해 전송할 수 없었습니다. `application.properties` 파일에 네이버 계정의 비밀번호를 입력하는 것이 아닌 애플리케이션 비밀번호를 저장해야 합니다.  
애플리케이션 비밀번호를 설정하기 위해 네이버에서 `네이버ID > 보안설정 > 기본보안설정 > 2단계 인증 > 관리`에 이동하여 `애플리케이션 비밀번호 관리`에서 생성해야 합니다.  
그 후 생성된 비밀번호를 설정 파일에 저장해야 합니다.    
</details>

<details>
<summary>이메일 재전송 시간 문제</summary>

회원가입을 하게되면 본인의 이메일로 회원가입 인증메일이 전송되게 됩니다.  
이때, 받지 못한 사용자들에게 다시 재전송할 수 있는 기능이 존재하는데, 이게 `재전송 버튼`을 계속 누르게 되면  
계속 전송할 수 있게 되어 이메일이 무분별하게 쌓일 수 있게 되는 문제가 발생했습니다.  
  
이것을 해결하기 위해 이메일을 전송한 지 5분이 지났는지 확인 후 보낼 수 있게 코드를 구현했습니다.  
```java
public class Member extends BaseEntity {
  ...생략

    // 이메일을 보낸 지 5분이 지났는지 확인
    public boolean enableToSendEmail(){
        return this.emailTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }
}
```
</details>
  
<details>
<summary>build.gradle에서 QueryDSL 설정 에러</summary>
QueryDSL을 사용하기 위해 build.gradle에서 컴파일하여 설정을 하는데 에러가 발생하여 컴파일이 되지 않는 에러가 발생했습니다.    

이는 Spring Boot 2.6이상 Gradle 5.0이상 설정 방법이 약간 다르기 때문에 발생한 문제였습니다.  
build.gradle에서 코드는 다음과 같습니다.  
```
 buildscript {
	ext {
		queryDslVersion = "5.0.0"
	}
}

...생략

def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}
sourceSets {
	main.java.srcDir querydslDir
}
compileQuerydsl{
	options.annotationProcessorPath = configurations.querydsl
}
configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
	querydsl.extendsFrom compileClasspath
}
```
</details>
  
<details>
<summary>CKEditor4 Refused to display in a frame because it set 'X-Frame-Options' to 'DENY' 에러</summary>
게시글에 글을 작성하는 데 저는 텍스트 편집기인 `CKEditor4`를 사용하였습니다.  
하지만 `CKEditor4 Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'` 에러가 발생하였습니다.  
  
이것은 Spring Security를 적용하면 기본적으로 X-Frame-Options Click jacking 공격 막기 설정이 되어있기 때문이였습니다.  
대략 사용자가 어떤 웹 페이지에서 링크를 눌렀을 때 의도했던 것과는 다른 동작을 하게 한다해서 이를 `Click jacking`이라고 합니다.  
이를 해결해 줄 수 있는 방법이 `X-Frame-Options Header`입니다.  

이는 Spring Security 설정 파일에서 아래와 같이 `sameOrigin` 옵션을 사용해서 해결합니다.  
```java
   @Override
   protected void configure(HttpSecurity http) throws Exception {
        ...생략
  
        // X-Frame-Options Click jacking 공격 막기 설정
        // 해당 페이지와 동일한 origin에 해당하는 frame만 
        http.headers()
                .frameOptions().sameOrigin();
  }
```
</details>

<details>
<summary>CKEditor4 이미지 업로드 403 Forbidden Error </summary>

CKEditor4를 통해 이미지를 업로드하는데 `403 Forbidden Error`가 발생하였습니다.  

다른 도메인과 resource를 공유하는 `cors`와 요청을 위조하여 사용자의 권한을 이용해 서버에 대한 악성공격을 하는 `csrf`를 disable 하는 방식으로  
해결하였습니다.  
```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      ...
        // 'ckeditor4' 이미지 업로드 시 403 Forbidden Error로 인해 설정
        http.cors().and()
                .csrf().disable();
    }
```
</details>

<details>
<summary>form method put/delete 에러</summary>

HTML에서 form 태그를 구성할 때 method를 `PUT/DELETE`를 구성하여 submit할 때, 에러가 발생했습니다.  

이를 해결하기 위해 HTML form 태그 하위에 `<input type="hidden" name="_method" value="put"/>`와 같이 Hidden 타입으로  
HTTP Method를 전달합니다.  

하지만 Spring Boot 2.2 이상 버전에서는 자동으로 구성되지 않아 `application.properties`에 아래와 같이 추가 설정 값이 필요합니다.  
```
# put, delete method
spring.mvc.hiddenmethod.filter.enabled = true
```
</details>

<details>
<summary>EC2 Instance Metadata Service is disabled 에러</summary>

AWS와 Spring Boot를 연동할 때 `spring-cloud-start-aws` 패키지를 사용할 때, 나온 에러 메시지입니다.  
EC2의 메타데이터를 읽다가 발생하는 에러입니다.  

이를 해결하기 위해 로컬에서 실행할 때 IntelliJ에서 `Run -> Edit Configurations -> Modify Options -> add VM Options` 순으로 선택합니다.  
여기서 설정값으로 `-Dcom.amazonaws.sdk.disableEc2Metadata=true`으로 주고 해결합니다.  

하지만 EC2 서버에서 실행할 때도 똑같은 에러 메시지가 발생했습니다.  
이는 Application.java에서 추가적으로 아래와 같이 설정할 필요가 있었습니다.  
```java
  ublic class AzaApplication {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	public static void main(String[] args) {
		SpringApplication.run(AzaApplication.class, args);
	}
}
```
또한 추가적으로 에러 로그를 단순히 보이고 싶지 않을 때 아래와 같이 `application.properties`에 추가 설정을 하여 해결했습니다.   
```
  logging.level.com.amazonaws.util.EC2MetadataUtils: error
```
</details>
  
<details>
<summary>AWS S3 이미지 업로드 에러(Permission denied)</summary>

로컬에서 정상적으로 AWS S3에 이미지가 업로드된 것을 확인 후, EC2 서버에서 이미지 업로드를 시도했지만  
`File에 대한 IOExceiption : permission denied`에러가 발생했습니다.  

왜 발생했는지 생각하면, 초기 코드에 이미지가 요청되면 `File.createNewFile()` 메서드로 프로젝트 내에 이미지를 먼저 업로드하고 AWS S3 버킷 안에 이미지가 업로드되고 공간 낭비를 해소하기 위해 프로젝트 내에 이미지를 삭제하는 절차를 가졌습니다.  
  
프로젝트 내에 이미지를 저장하는데 EC2 서버 내에서는 접근이 거부되서 아예 MultipartFile 형식을 File 형식으로 변환할 수 없고 그렇게 되면 업로드가 되지 않아 발생하는 문제였습니다.  
  
이를 해결하기 위해 바로 AWS S3으로 전달하여 업로드하도록 코드를 수정할 필요가 있었습니다.  

아래는 초기에 작성했던 코드입니다.  
```java
    private String putS3(File uploadFile, String saveName) {
        amazonS3.putObject(new PutObjectRequest(bucket, saveName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, saveName).toString();
    }
```
이번에는 수정한 코드입니다.  
```java
      private String putS3(MultipartFile uploadFile, String saveName, ObjectMetadata metadata) throws IOException {
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, saveName,
                    uploadFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패");
        }
        return amazonS3.getUrl(bucket, saveName).toString();
    }
```
수정된 곳은 S3에 업로드하는 `putS3()` 메서드에 `ObjectMetadata` 객체를 추가하였으며,  
그리고 MultipartFile 형식의 파일을 읽어오는 `getInputStream()` 메서드를 사용하였습니다.  

이렇게 해서 프로젝트 내에 이미지를 업로드하고 삭제하는 절차를 가지지 않고 바로 S3에 이미지를 업로드할 수 있게 해결했습니다.  
</details>

<br>

## 7. 느낀점 
지금까지 대학교 다니면서 과제로 프로젝트를 몇 개 했지만 솔직히 퀄리티도 낮았고 무엇보다 도중에 그만둔게 많았습니다.  
그래서 이번에 혼자 프로젝트할 때 목표는 간단해도 좋으니 일단 완성해보자 였습니다.  
	
완성이라는 목표를 가지고 웹의 기본 소양인 CRUD 게시판을 초기에는 정말 단순한 기능 구현만 설계했습니다.  
하지만 직접 생각하고 구현한 코드와 구글링을 통해 어떤 기능을 구현했을 때, 정말 너무 뿌듯하고 되게 재밌었습니다.  
그러다보니 제가 생각한 기능보다 더 추가하게 되었습니다.  

물론 책이나 영상을 보면서 기능을 구현할 때와 다르게 혼자 생각하고 구글링을 통해 구현할 때는 많은 문제가 있었습니다.  
이 문제들을 해결하면서 내가 아직 이해하지 못하고 사용했다라는 것을 느낀 반면에 그래도 이걸 해결했네라는 뿌듯함, 신기함, 재미 또한 느꼈습니다.  

확실히 책이나 영상을 보며 공부했을 때와 혼자 고민하고 코드를 구현할 때는 확실히 다르다는 것을 깨달았습니다.  
그리고 앞으로 더 어려운 프로젝트를 하게 된다면 이보다 더 어렵고 많은 문제들을 직면할 수 있겠지만  
그래도 이번 기회를 통해 앞으로 마주할 문제들을 해결할 수 있을 것이라는 자신감이 생겼습니다.  

감사합니다!



