# 냥장고
## Index
  - [프로젝트 소개](https://github.com/ojy9612/hanghae99_team3/edit/master/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%86%8C%EA%B0%9C)
  - [기술 스택](https://github.com/ojy9612/hanghae99_team3/edit/master/README.md#%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)

## 프로젝트 소개

### 여러분들의 소중한 음식 재료들이 냉장고 안에서 잠자고 있나요?

- 재료들을 내 손안의 냉장고에 넣어 두고 관리하세요!
- 냥장고는 사용자의 냉장고에 들어있는 재료들을 가지고 만들 수 있는 여러 사람들이 올린 다양한 레시피를 구경해보세요!
- 귀찮음 많은 여러분들을 위한 쉽고 빠르고 정확한 검색 서비스를 제공합니다.

#### 냥장고 이동하기 ▼
[냥장고(Nyangjanggo) **오늘 뭐 먹을지 고민하지 말자!**](https://nyangjanggo.com/)<br>
#### 개발팀 노션 ▼
[팀 노션](https://www.notion.so/054c350dee7b4ad7b55c4ef878625193)



## 기술 스택

### 검색 기능
- Elastic Search

여기에 어떤식으로 했는지 써보자!<br>
이런이런식으로 이런 고민으로 이런 기술을 사용해서 이렇게 사용했습니다.

### 로그인 기능
- JWT
- OAuth2.0
- Spring Security

세션/쿠키 방식과는 달리 Stateless한 서버를 구성하면서도 사용하기 간편한 JWT토큰 인증 방식을 사용하였습니다.   
다만, JWT특성상 한번 발급되면 유효기간이 완료될 때 까지 계속 사용 가능하기에 보안상에 문제가 발생할 수 있습니다.   
이러한 문제점과 사용자의 편의성을 고려하여 Access-Token의 유효기간을 짧게 설정해주고 Refresh-Token을 도입하였습니다.   
토큰의 탈취 등 보안에 많은 고민을 하였고 저희는 Refresh-Token을 서버에서는 DB에, 클라이언트에게는 HttpOnly, Secure 옵션 등을 주어 쿠키에 저장하였습니다.
추가로, 소셜 로그인 방식을 채택하여 유저 편의성을 향상시켰습니다.

### 데이터 처리
- MySQL
- Spring Data Jpa
- Redis

여기에 어떤식으로 했는지 써보자!<br>
이런이런식으로 이런 고민으로 이런 기술을 사용해서 이렇게 사용했습니다.   


동일한 데이터를 많이 요청하는 토큰 인증([UserServiceImpl.java](https://github.com/ojy9612/hanghae99_team3/blob/master/src/main/java/com/hanghae99_team3/login/jwt/UserDetailsServiceImpl.java))과
게시물 정보 요청([BoardController->getOneBoard API](https://github.com/ojy9612/hanghae99_team3/blob/master/src/main/java/com/hanghae99_team3/model/board/BoardController.java))에 redis cache를 적용하여 DB부하를 줄이고
보다 빠른 응답을 기대할 수 있습니다.

### 배포 하기
- Github Actions
- Code Deploy
- Nginx
- AWS EC2
- AWS S3
- AWS RDS

여기에 어떤식으로 했는지 써보자!<br>
이런이런식으로 이런 고민으로 이런 기술을 사용해서 이렇게 사용했습니다.


### 테스트, 문서화
- JUnit5
- MockMvc
- Spring Rest Docs 

Swagger는 API 동작을 테스트 하는 용도에 더 특화되어 있는 반면에, Spring Rest Docs는 깔끔하고 명료한 문서를 만들 수 있습니다.   
문서 제공용으로는 Spring Rest Docs가 낫다고 판단하였고 테스트가 성공해야 문서가 작성되는 점이 매력적이라고 느꼈습니다.   

JUnit5 환경에서 테스트 코드를 작성하였습니다.
MockMvc는 @WebMvcTest가 가능하며 Controller 계층만 테스트 하기 때문에 속도가 빠릅니다.
통합테스트의 구성을 고려하지 않고 Spring Rest Docs로 문서를 작성하기에는 MockMvc가 더 좋은 선택이라고 생각합니다.





