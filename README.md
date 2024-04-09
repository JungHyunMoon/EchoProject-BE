

## 프로젝트 소개
<br>
이 프로젝트는  축구(EPL), 농구(NBL), 야구(MLB) 종목의 최신 경기 일정 및 결과를 제공하고, 스포츠 관련 상품을 핫딜 형태로 판매하는 웹서비스입니다.<br> 이 서비스는 사용자들에게 경기 정보와 함께 상호 작용의 기회를 제공하며, 대용량 데이터 처리 및 트래픽 대응에 초점을 맞추고 있습니다.

---

## 기술 스택
-  Java 17
-  Spring Boot 3.2.1
-  Spring Security 6
-  MySQL 8.2.0
-  Redis 7.2.3
- JPA
- Rapid API
- AWS S3
- AWS RDS
- AWS EC2
- AWS Route53

### DevOps
- GitHub Actions
- nGrinder

### Tools
- IntelliJ: Java 개발을 위한 통합 개발 환경(IDE)
- Git: 소스 코드 버전 관리 시스템
---
## 프로젝트 구조
### 아키텍쳐
![FigJam Basics (한국어) (Community) (1) 복사본.png](src%2Fmain%2Fresources%2Fstatic%2FFigJam%20Basics%20%28%ED%95%9C%EA%B5%AD%EC%96%B4%29%20%28Community%29%20%281%29%20%EB%B3%B5%EC%82%AC%EB%B3%B8.png)
### ERD
![Screenshot 2024-01-10 at 12.13.56 PM.png](src%2Fmain%2Fresources%2Fstatic%2FScreenshot%202024-01-10%20at%2012.13.56%20PM.png)


---

## 담당 기능

### 스포츠 경기 일정 및 결과 서비스
![일정기능](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/f4c5f6a7-026e-4b75-a16b-568d0eff60c5)
- 사용자는 축구(EPL), 농구(NBL), 야구(MLB) 종목의 최신 경기 일정 및 결과(**Rapid API**를 사용)를 확인할 수 있습니다.
    - Rapid API의 호출을 이용해서 축구, 농구, 야구 분야에서 경기 일정 및 결과 데이터를 가져와 DB에 저장합니다.
    - 요청 방식은 **HTTP Request**를 사용하였습니다
    - 경기 결과 데이터는 Scheduler를 이용해 12분에 한 번씩 업데이트를 진행합니다.
        - Timezone 최소 단위와 RepidAPI 호출 가능 토큰 갯수를 고려해 12분으로 결정했습니다.
        - 스케줄러 서버를 **scale-out**하여 외부로 Scheduler를 구현하였습니다
    - **RapidAPI 응답 json 데이터 예제 이미지**
        
        ```        
        **country**: 
        {id: 5, name: 'USA', code: 'US', flag: 'https://media.api-sports.io/flags/us.svg'}**date**: 
        "2024-01-13T00:30:00+00:00"**id**: 
        372721**league**: 
        {id: 12, name: 'NBA', type: 'League', season: '2023-2024', logo: 'https://media.api-sports.io/basketball/leagues/12.png'}**scores**: 
        {home: {…}, away: {…}}**stage**: 
        null**status**: 
        {long: 'Game Finished', short: 'FT', timer: null}**teams**: 
        {home: {…}, away: {…}}**time**: 
        "00:30"**timestamp**: 
        1705105800**timezone**: 
        "UTC"**week**: 
        null
        ```
- 각 스포츠 경기의 상세 정보에는 팀 구성, 경기 시간 및 장소가 포함됩니다.
-  스케일 아웃: 서버의 확장성을 고려하여 Scheduler를 별도의 서버로 분리하여 일정 저장 이벤트를 처리하였습니다.
  
### 핫딜 구매 동시성 처리
-  핫딜 상품 : 스포츠 유니폼, 사인볼, 기념품 등 다양한 상품을 이벤트성으로 할인하여 판매합니다.
-  비관적 락(Perssimistic Lock)을 통해 Race Condition과 같은 동시성 문제를 제어합니다.
![image](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/848679cb-74c5-478c-b24d-5bd184571267)
- JPA Lcok을 통해 DataBase레벨에 Lock을 걸어 문제 상황을 핸들하고자 하였습니다.
- 핫딜의 경우 빈번한 트랜잭션 충돌이 예상되는 상황이였기에 커밋 시점에 동시성 문제가 발생하면 그떄 대응하는 낙관적 락의 방법론 보다는 데이터 정합성을 강력하게 보장할 수 있는 비관적 락을 사용하기로 결정 하였습니다.

### 핫딜 구매 순서 보장
- JPA 락으로도 유저의 구매 순서를 완벽하게 보장할 수 없었습니다.
- 하단의 사진을 보면 5번째로 시작된 쓰레드의 구매 프로세스가 가장 먼저 끝난것을 확인 할 수 있었습니다
![image](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/41d9647e-2d46-45fd-8635-785ca022c17d)
- 이에 구매 순서를 완벽히 보장하기 위해 SortedSet 자료구조를 도입하였습니다.
![image](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/3b102800-cf8d-4baa-895a-91c4f3a886b3)
- 첫 번째로 시도한 방법은 Polling 방식입니다.
- 유저는 핫딜을 구매하기 이전에 대기열에 입장하여 redis에 저장합니다.
- 클라이언트 단에서 자신의 차례인지 지속적인 요청을 보내 가장 최신의 score값을 가지고 있는 유저 10명을 구매 페이지로 redirect합니다. 
- 이후 한정 수량의 재고를 최종적으로 확인하여 구매를 진행할 수 있습니다
- Polling 방식의 server에 지속적인 대기열 순서 확인 요청이 사용자에게 불편한 이용 경험을 가져올 것을 예상하였습니다 이에 Event 방식으로 개선하였습니다

<br>

![image](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/00c21996-67e1-4091-8bd5-3839fc288358)
- 두 번째로 시도한 방법은 Event방식 입니다.
- 이벤트 방식은 유저들이 핫딜 구매를 요청하면 대기열에 들어오게 됩니다.
- 스케줄러는 매초 실행되어 대기중인 유저들에게 대기 번호를 부여합니다.
- 상위의 n명 사용자를 구매 로직으로 입장시킵니다. 입장한 유저들에 대해 구매 이벤트를 발생시키며 이벤트 리스너에서 해당 유저의 구매 로직을 진행하고 재고 확인 후 구매에 성공하게 됩니다.
- 재고가 모두 소진되면 스케줄러에서 이벤트를 종료시킵니다.
- Event 발생 방식으로 대기열 확인 프로세스를 개선하여 사용자가 구매 대기 이후 다른 서비스를 이용할 수 있게 개선하였습니다. 

### 대용량 데이터 처리 및 트래픽 대응
- 300만건의 상품 데이터 처리를 수행하였습니다
- 상품 목록 조회하는 시간을 평균 1500ms에서 50ms로 개선하였습니다.

<br>

![최적화 이전](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/319b4bdd-6723-4fdb-9f6f-f1271e965099) &nbsp;&nbsp; ![최적화 후](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/7df9fa30-aa7a-4c0d-967b-d2dc4cadd209)

- 1+N 문제 개선
  - BatchSize 설정을 통해 연관 컬렉션을 in 쿼리로 불러올 수 있게 하였습니다.


- 상품 검색 메서드의 반환 타입 변경
  - 검색 리스트를 Page 객체로 반환시 전체 페이지 수 조회를 위한 count 쿼리가 발생하고 있습니다<br>
  - RDS의 성능이 좋지 않아 300만 건의 데이터를 감당하지 못하였고, 이로 인해 비정상적으로 높게 나오고 있습니다.
  ![image](https://github.com/JungHyunMoon/EchoProject-BE/assets/120004247/e5b277c5-a03f-474a-b7f2-c246f431ad73)
  <br>
  
  - 팀원들과 합의하여 client 단에서 무한로딩 방식을 적용하여 전체 페이지 수가 필요어짐에 따라 List로 반환 방식을 변경하였습니다. 

---

### TroubleShooting
- MapStruct
  - MapStruct은 서로 다른 타입의 객체를 서로 변환해야 할 때 사용되는 도구로 dto 와 entity간의 변환을 간편하게 지원해주고 변환의 책임을 MapStruct에 두기에 용이하여 채택하였습니다.
  - 초기 테스트 단계에서 빈번한 null값을 마주하게 되었고 이는 Lombok의 의존성 추가가 선행되어야 함을 알 수 있었습니다 왜냐하면 MapStruct는 Lombok의 getter, setter, builder를 이용해서 객체를 생성하기 때문이였습니다.
  - 두 번째 문제는 협업을 하며 일반 구매와 핫딜 구매의 메서드를 병행하는 과정에서 발생하였습니다. 핫딜 구매시에 장바구니 객체 purchase에 구매 내역을 추가해주어야 하는데 이를 MapStruct 라이브러리가 Product객체를 변환하는 과정에서 Hotdeal 그리고 purchase 객체와 혼동하여 런타임 에러를 발생시켜 트랜잭션이 롤백되는 현상이 발생한 것 입니다.
  - 이를 해결하기 위해 @Mapping의 소스와 타겟을 일제히 설정해주고 @AfterMapping을 통해 명시해준 자원끼리의 매핑이 완료 된 후에 수행해야 할 로직을 실행하도록 해결하였습니다.
- 동시성과 Atomic
  - 동시성 제어를 위해 테스트 코드를 작성하던 와중 ```local variables referenced from a lambda expression must be final or effectively final successfulPurchases++;``` 발생하였습니다. 이는 int 변수에 대해 ++ 연산을 사용하면 이 연산이 'atomic' 하지 않기 때문에 문제가 발생할 수 있고 여기서 atomic 하다는 말은 연산이 중단되지 않고 한 번에 완료 된다는 의미로 일종의 Rase Condition 이슈로 판단하였습니다.
  - 이런 문제를 방지하기 위해 설계된 AtomicInteger의 incrementAndGet() 같은 메서드를 사용하여 해당 연산이 atomic하게 수행되도록 보장하여 멀티쓰레드 환경에서 안전하게 정수 값을 증가 시켜 테스트를 수행할 수 있었습니다.
 
