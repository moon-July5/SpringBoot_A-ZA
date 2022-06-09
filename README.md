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

<img src="https://user-images.githubusercontent.com/60730405/172861164-168637bb-9cef-4db2-b50e-b7a508f8ba09.JPG" height="400px">  
  
- BoardController에는 View에서 넘어온 데이터를 전달받아 처리합니다.    
- 여기서 tid 변수는 `임시저장된 게시글 번호`입니다. 이것을 왜 따로 요청받아 처리하냐면 임시저장된 게시글을 불러와서  
완전히 작성 후, 실제 저장하게 된다면 임시저장된 게시글은 더 이상 사용하지 않을 것이기 때문에 삭제하기 위함입니다.  
- 게시판으로 리다이렉트하도록 응답합니다.  
</details>  
</details>


