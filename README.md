# 냥장고
## Index
  - [프로젝트 소개](#프로젝트-소개)
  - [기술 스택](#기술-스택)
  - [트러블 슈팅](#트러블-슈팅)

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

재료를 기반으로 레시피를 검색하는 서비스이기에 사이즈가 큰 장문 테이터가 검색 될 일도 없고 역색인 방식의 검색을 통해 빠르게 검색을 할 수 있기 때문에 사용했습니다.   
ELK 스택을 모두 사용하는 쪽으로 기획하였으나, Logstash는 데이터를 등록하는데에 너무 제한적이기 때문에 Spring Data ElasticSearch를 사용해 데이터를 저장, 수정, 삭제 합니다.


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
- Spring Batch   

Spring Data JPA는 현재 Spring Boot의 핵심 기능이므로 NoSQL를 사용해야만 하는 명확한 이유가 없다면 Spring Data JPA를 사용해야 한다고 생각합니다.   
RDB는 무료이며 가장 유명한 MySQL을 사용했습니다.   
S3에 사용되지 않는 이미지를 삭제하기 위해 스케쥴러를 사용합니다. 하지만 Nginx를 사용하면서 두 개의 스프링 서버가 동작하기 때문에 현재 배포중인 어플리케이션만 스케쥴러를 사용해야 합니다. 이 과정에서 더 체계적인 Spring Batch를 사용했습니다.   
그 외 ElasticSearch의 일부 동적 데이터를 반영하기 위해 사용했습니다.   


### 캐싱
- redis   

동일한 데이터를 빈번하게 요청하는 토큰 인증([UserServiceImpl.java](https://github.com/ojy9612/hanghae99_team3/blob/master/src/main/java/com/hanghae99_team3/login/jwt/UserDetailsServiceImpl.java))과
게시물 정보 요청([BoardController->getOneBoard API](https://github.com/ojy9612/hanghae99_team3/blob/master/src/main/java/com/hanghae99_team3/model/board/BoardController.java))에 redis cache를 적용하여 DB부하를 줄이고
보다 빠른 응답을 기대할 수 있습니다.


### 배포 하기
- Github Actions
- Code Deploy
- Nginx
- AWS EC2
- AWS S3

GitHub Actions는 클라우드가 있어서 별도 설치가 필요 없으며 사용이 간편하므로 작은 프로젝트에 적합 합니다.
서버를 시작는 동안에는 API를 사용할 수 없으므로 서버가 멈추지 않고 실행되게 하기위해 Nginx를 사용했습니다.
로컬에서 세팅한 서비스 설정을 서버에 다시해야 하기 때문에 Docker를 사용해서 서버와 로컬의 환경을 동일하게 만들었습니다.


### 테스트, 문서화
- JUnit5
- MockMvc
- Spring Rest Docs 

Swagger는 API 동작을 테스트 하는 용도에 더 특화되어 있는 반면에, Spring Rest Docs는 깔끔하고 명료한 문서를 만들 수 있습니다.   
문서 제공용으로는 Spring Rest Docs가 낫다고 판단하였고 테스트가 성공해야 문서가 작성되는 점이 매력적이라고 느꼈습니다.   

JUnit5 환경에서 테스트 코드를 작성하였습니다.
MockMvc는 @WebMvcTest가 가능하며 Controller 계층만 테스트 하기 때문에 속도가 빠릅니다.
통합테스트의 구성을 고려하지 않고 Spring Rest Docs로 문서를 작성하기에는 MockMvc가 더 좋은 선택이라고 생각합니다.


## 트러블 슈팅

### 1. Logstash로 RDB데이터 연동
#### [문제 상황]
Logstash는 RDB에 있는 데이터를 Elasticsearch에 넣는 용도로 사용됩니다.

주기적으로 실행되며 RDB에 추가, 수정된 데이터를 Elasticsearch에 입력하지만 Colum이 List로 존재하는 데이터(One to Many)는 입력하는데에 어려움이 있었습니다.
또한, 데이터 삭제시에는 감지할 수 있는 방법이 없습니다.

#### [해결 방안]
Spring Batch와 Spring Data Elasticsearch로 해당 문제를 해결했습니다.

정적인 데이터(ex. 게시글 내용)는 Spring Data Elasticsearch로 RDB에 데이터가 입력, 수정, 삭제 될 때 Elasticsearch 서버에도 데이터를 똑같이 넣어거나 삭제 해줬습니다.

동적인 데이터(ex. 좋아요 수, 댓글 수)는 Spring Batch를 통해 변경이 감지된 게시글만 update해주는 로직을 작성했습니다.


### 2. 데이터 전송시 불필요한 과정 제거
#### [문제 상황]
검색엔진서버가 따로 있는데 조회부분 마저도 Spring Data Elasticsearch를 사용한다면 꽤나 비효율적인 방식이라고 생각됐습니다.

클라이언트 → 스프링 서버 → Elasticsearch 서버 → 스프링 서버 → 클라이언트

#### [해결 방안]
중간에 있는 스프링 서버는 존재하지 않아도 데이터를 주고받는데에 문제가 없으므로 클라이언트에서 Elasticsearch서버에 직접 데이터를 요청하도록 변경했습니다.

클라이언트 → Elasticsearch 서버 → 클라이언트

이 과정에서 Elasticsearch 서버의 cors 허용, https적용을 했습니다.

### 3. 스케쥴러의 중복 실행 문제
#### [문제 상황]
Nginx를 이용해 어플리케이션 2개가 swap하며 최신 어플리케이션을 바라보는 방식을 사용합니다. (하나의 EC2에 2개의 port를 사용)
어플리케이션이 2대가 실행중이므로 스케줄러 또한 2번 실행되게 됩니다.

#### [해결 방안]
Redis의 분산락을 사용할 예정이었으나 분산락은 서버 2대가 모두 비니지스 로직을 처리할 때 사용되는 것임을 알았습니다.
또한, 겨우 스케줄러의 동시성 문제를 해결하고자막고자 Redis서버를 띄우는 것은 비용 낭비가 심하다고 생각되어서 다른 방법을 생각하게 되었습니다.(이 당시에는 Redis의 도입이 결정되지 않았습니다.)
Nginx가 바라보고있는 Port번호는 별도의 파일에서 관리 됩니다.
해당 파일에 적혀있는 Port번호와 현재 어플리케이션의 Port 번호를 비교한 후 스케줄러를 실행하게 했습니다.
