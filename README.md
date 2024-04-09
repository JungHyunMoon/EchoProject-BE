---

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

## 구현 기능

### 스포츠 경기 일정 및 결과 서비스
-  기능 설명: 사용자는 축구, 농구, 야구 경기의 일정과 결과를 확인할 수 있습니다. 서비스의 토대가 되는 스포츠 경기 일정 데이터는 Rapid API 플랫폼을 채택하였습니다.
, DB에 저장됩니다.
-  데이터 업데이트: Scheduler를 활용해 15분마다 경기 결과 데이터를 업데이트합니다. 이는 Timezone 및 API 호출 제한을 고려한 결정입니다.
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
- 첫 번째로 시도한 방법은 Polling방식입니다.
- 유저는 핫딜을 구매하기 이전에 대기열에 입장하여 redis에 저장합니다.
- 클라이언트 단에서 자신의 차례인지 지속적인 요청을 보내 가장 최신의 score값을 가지고 있는 유저 10명을 구매 페이지로 redirect합니다. 
- 이후 한정 수량의 재고를 최종적으로 확인하여 구매를 진행할 수 있습니다

### 대용량 데이터 처리 및 트래픽 대응
- 300만건의 상품 데이터 처리를 수행하였습니다
- 상품 목록 조회하는 시간을 평균 1500ms에서 50ms로 개선하였습니다.
- 스케일링 및 로드 밸런싱: 스포츠 이벤트 중 발생하는 급격한 트래픽 증가에 대응하기 위해 확장 가능한 서버 구조를 설계하고, 로드 밸런싱 및 오토 스케일링 전략을 채택합니다.
- 성능 테스트: nGrinder를 이용한 부하테스트 및 최적화를 통해 100~300만 건의 댓글 데이터 처리를 위한 성능을 확보합니다.
---

---

