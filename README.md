![header](https://capsule-render.vercel.app/api?type=waving&color=timeGradient&text=SportsEcho!!&animation=twinkling&fontSize=45&fontAlignY=40&fontAlign=50&height=250)
## 팀소개
| 이름  | 역할  |Github|블로그|
|-----|-----|---|---|
| 진유록 | 팀장  |<a href="https://github.com/jinyr1128">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@jinyr1128/
| 정지성 | 부팀장 |<a href="https://github.com/zzzzseong">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://zzzzseong.tistory.com/
| 김지현 | 팀원|<a href="https://github.com/zomeong">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@zo_meong/posts
| 문정현 | 팀원|<a href="https://github.com/JungHyunMoon">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a>|https://velog.io/@foqlzm12345/posts


---

## 프로젝트 소개
<br>
이 프로젝트는  축구(EPL), 농구(NBL), 야구(MLB) 종목의 최신 경기 일정 및 결과를 제공하고, 스포츠 관련 상품을 핫딜 형태로 판매하는 웹서비스입니다.<br> 이 서비스는 사용자들에게 경기 정보와 함께 상호 작용의 기회를 제공하며, 대용량 데이터 처리 및 트래픽 대응에 초점을 맞추고 있습니다.

---

## 기술 스택
### Frontend
- React: 사용자 인터페이스 구축을 위한 JavaScript 라이브러리
- JavaScript: 동적 웹 페이지 기능을 구현하는 스크립트 언어
- HTML/CSS: 웹 페이지의 구조와 스타일을 정의
- WebSocket: 실시간 양방향 데이터 전송을 위한 프로토콜
- STOMP: WebSocket을 통해 메시지를 전달하기 위한 프로토콜
- AWS S3: 클라우드 기반 객체 스토리지 서비스
- AWS RDS: 클라우드 기반 관계형 데이터베이스 서비스
- AWS EC2: 클라우드 기반 가상 서버 서비스
- AWS Route53: 클라우드 기반 DNS 서비스

### Backend
-  Java 17: 객체 지향 프로그래밍 언어, 안정적이고 확장 가능한 백엔드 개발을 위함
-  Spring Boot 3.2.1: 빠른 마이크로서비스 개발을 위한 Java 기반 프레임워크
-  Spring Security 6: 애플리케이션 보안 (인증 및 권한 부여)을 위한 프레임워크
-  MySQL 8.2.0: 관계형 데이터베이스 관리 시스템
-  Redis 7.2.3: 고성능 키-값 저장소
- JPA: 자바 ORM 기술로, 객체와 관계형 데이터베이스의 매핑을 위한 프레임워크
- QueryDSL: SQL 쿼리를 자바 코드로 작성할 수 있도록 도와주는 프레임워크
- Lombok: 자바 코드를 간결하게 작성할 수 있도록 도와주는 라이브러리
- JWT: JSON 포맷을 이용하여 사용자에 대한 속성을 저장하는 웹 토큰
- OAuth2: 인증 및 권한 부여를 위한 프로토콜
- Rapid API: 실시간 스포츠 데이터를 제공하는 API
- WebSocket: 실시간 양방향 데이터 전송을 위한 프로토콜
- AWS S3: 클라우드 기반 객체 스토리지 서비스
- AWS RDS: 클라우드 기반 관계형 데이터베이스 서비스
- AWS EC2: 클라우드 기반 가상 서버 서비스
- AWS Route53: 클라우드 기반 DNS 서비스

### DevOps
- Docker 24.0.7: 애플리케이션을 컨테이너화하여 쉽게 배포, 실행할 수 있도록 함
- nginx 1.24.0: 고성능 웹 서버 및 리버스 프록시 서버
- AWS: 클라우드 기반 인프라 서비스
- GitHub Actions: 지속적 통합 및 배포를 위한 워크플로우 자동화 도구
- nGrinder/Jmeter: 성능 테스트를 위한 오픈소스 로드 테스트 도구

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

## 디렉토리 구조

도메인 - controller, dto, service, repository, entity,
Global,
Common

##### RequestDto 작성 양식
```java
@Getter
@NoArgsConstructor
public RequestDto {
  //...
}
```
###### ResponseDto 작성 양식
```java
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public ResponseDto {
    //...
    }
```

---

## 주요 기능

### 스포츠 경기 일정 및 결과 서비스
-  기능 설명: 사용자는 축구, 농구, 야구 경기의 일정과 결과를 확인할 수 있습니다. 이 데이터는 Rapid API를 통해 가져오며, DB에 저장됩니다.
-   데이터 업데이트: Scheduler를 활용해 15분마다 경기 결과 데이터를 업데이트합니다. 이는 Timezone 및 API 호출 제한을 고려한 결정입니다.
-   스케일 아웃: 서버의 확장성을 고려하여, 필요시 Scheduler를 별도의 서비스로 분리하여 이벤트를 처리할 수 있도록 계획하였습니다.
-   사용자 상호 작용: 각 경기 일정에 대해 사용자가 댓글을 달 수 있는 기능을 제공합니다.
### 핫딜 및 상품 구매 서비스
-  상품 제공: 스포츠 유니폼, 사인볼, 기념품 등 다양한 상품을 판매합니다.
-   기능: 상품 검색, 리뷰 확인, 장바구니 기능을 포함합니다.
- 특별 행사 및 할인: 사용자에게 최상의 거래를 제공하기 위해 특별 행사 및 할인 정보를 제공합니다.
-  성능 최적화: Redis Sorted Set을 사용해 대용량 트래픽 처리를 계획하고 있으며, 동시성 문제는 비관적 락을 통해 해결할 예정입니다.
### 응원 댓글 작성 서비스

WebSocket방식 : 사용자는 각 경기에 대해 응원 댓글을 작성할 수 있으며, 댓글은 WebSocket 방식을 통해 실시간으로 갱신됩니다.

### 대용량 데이터 처리 및 트래픽 대응
- 데이터베이스 설계: 경기 일정, 결과, 사용자 댓글 등의 대용량 데이터를 효율적으로 처리하기 위해 최적화된 데이터베이스 설계를 적용합니다.
- 최적화 전략: 데이터베이스 쿼리 최적화, 캐싱 전략, 데이터 파티셔닝을 통해 빠른 데이터 처리 속도를 보장합니다.
- 스케일링 및 로드 밸런싱: 스포츠 이벤트 중 발생하는 급격한 트래픽 증가에 대응하기 위해 확장 가능한 서버 구조를 설계하고, 로드 밸런싱 및 오토 스케일링 전략을 채택합니다.
- 성능 테스트: nGrinder를 이용한 부하테스트 및 최적화를 통해 100~300만 건의 댓글 데이터 처리를 위한 성능을 확보합니다.
---

## 성능 최적화 및 부하 테스트
경기 일정 및 결과 데이터에 대한 데이터베이스 쿼리 최적화<br>
nGrinder를 이용한 성능 테스트 및 최적화 수행<br>
Redis와 JPA를 사용하여 대용량 트래픽에 대한 정합성 및 동시성 처리

---

