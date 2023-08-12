# 자산 관리 포트폴리오 웹사이트 프로젝트

노션 :
https://www.notion.so/5c6fbfd8ea5e4eda8919323cd10845a5#a8e2fdabc29144859deb6b61ebec7b8a

# 1. 기획

## 📒 무엇을 만들까? (주제)

---

> **자신이 실제 보유하고 있는 자산, 또는 임의로 자산 포트폴리오를 작성하여 실시간으로 모니터링 할 수 있는 편리한 플랫폼**
> 

> **이를 통해 언제 어디서나 자신의, 또는 자신이 작성한 포트폴리오의 자산 상태와, 투자 성과를 파악할 수 있습니다.**
> 

## 🧐 왜 만들까? (목표)

---

### 1. 프로젝트의 목표

> **자산을 효율적으로 관리하고 안정적인 수익을 창출하는 것은 모두에게 중요한 과제**
> 

> **자산 포트폴리오 사이트를 이용하여 주식, 채권, 펀드, 부동산 등 다양한 종류의 자산을 하나의 플랫폼에서 통합적으로 관리하기 위함**
> 

### 2. 기술적 목표

> **************************************************************************************스프링부트 프레임워크 적응과 DB(서버)와 클라이언트 간 데이터 통신 절차 이해**************************************************************************************
> 

> **나의 생산성을 높여주기 위한 ChatGPT 활용 극대화**
> 

> **ChatGPT를 이용하여 처음 접하는 기술(대표적으로 Vue)에 적응하기**
> 

## 🔧 뭘로 만들까? (기술 스택)

---

### 1. 기능 설계

: 전체적인 그림을 그려놓고,

[Version.1](https://www.notion.so/Version-1-aae6ecc1bff444c1b0233afa44e63cdd?pvs=21) 

 세부적인 기능을 챗GPT와 토론하며 하나 씩 정함

[ChatGPT](https://chat.openai.com/?__cf_chl_tk=gKa1Uh..8rUCX5PB5K8dU5P8Nvm.6naNE5Baa6afXzo-1679052628-0-gaNycGzNChA)

### 2. 화면 구상

: 카카오 오븐

[OvenApp.io](https://ovenapp.io/)

### 3. ERD(DB) 모형 구상

: DBdiagram

[A Free Database Designer for Developers and Analysts](https://dbdiagram.io/)

### 4. API 구상

: 기능 설계를 바탕으로 챗GPT 👍🏻와 토론

### 5. 구현

:

### 프론트엔드

- **vue3**
    
    : 간결하고 직관적인 문법
    
    프론트엔드 구성 중에도 마치 프로그래밍을 하는 것처럼 if, for등의 문법으로 알아보기 쉽다.
    
    : 컴포넌트
    
    재사용 가능성이 많은 컴포넌트를 따로 만들어 사용 가능하고 그에 따른 유지보수가 편하다.
    
    : 반응형 데이터 바인딩
    
    데이터와 UI간의 데이터 바인딩을 통해 데이터 조작이 쉽고, 페이지의 동적인 요소 구현이 쉽다.
    
    : 신기술에 대한 호기심
    
     챗GPT를 사용하여 내가 알지 못하던 기술에 얼마나 잘 적응할 수 있는지 테스트 해보기 위함.
    
    [소개 | Vue.js](https://v3-docs.vuejs-korea.org/guide/introduction.html)
    
- **scss**
    
    : css와 비교해 여러가지 장점이 있지만 그 중에서도 관심이 갔던 건
    
    Nesting(중첩), &(상위 선택자 참조), @mixin(코드 재활용)
    
    : 코드가 더 보기 쉬워지고, 불필요한 코드를 줄이고, 중첩되는 코드들을 재활용 할 수 있을 것.
    
    [Sass: Sass Basics](https://sass-lang.com/guide/#nesting)
    
    [[SCSS] SCSS 문법 정리](https://seokzin.tistory.com/entry/SCSS-SCSS-문법-정리)
    
- **bootstrap**
    
    : 디자인적으로 좀 더 깔끔하게 보이기 위해.
    
    [Bootstrap 시작하기](https://getbootstrap.kr/docs/5.3/getting-started/introduction/)
    

### 백엔드

- **언어 : Java**

- **웹 프레임워크 :** **Spring Boot**
    
    : 가장 많이 사용하는 백엔드 프레임워크
    
    : 현재 듣고있는 특강의 백엔드 프레임워크가 스프링부트
    
    : 솔직히 다른 프레임워크를 안 써봐서 스프링부트만의 장점을 아직 체감하지는 못함
    
    [Spring Boot](https://spring.io/projects/spring-boot)
    
    - 쓰면서 좋았던 점은
        - **Spring Security**
            
            : 복잡한 로그인 인증 과정과 계정 조작(정지, 만료), 보안 설정등을 쉽게 처리해줌.
            
            [Spring Security](https://spring.io/projects/spring-security)
            
        - **JPA**
            
            : DB와의 관계를 Entity를 통해 직관적으로 표현할 수 있고, DB의 CRUD작업이 굉장히 간편해짐.
            
            [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
            

- **데이터베이스 : MariaDB**
    - 툴 : DBeaver, HeidiSQL
    
    [DBeaver](https://dbeaver.io/)
    
    [HeidiSQL - MariaDB, MySQL, MSSQL, PostgreSQL and SQLite made easy](https://www.heidisql.com/)
    

- **ORM : JPA**
    
    : DB와의 관계를 Entity를 통해 직관적으로 표현할 수 있고, DB의 CRUD작업이 굉장히 간편해짐.
    
    [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
    

- **웹 서버 : 스프링부트 내장 톰캣**

- ****************************************************보안 및 인증 : Spring Security****************************************************
    
    : 복잡한 로그인 인증 과정과 계정 조작(정지, 만료), 보안 설정등을 쉽게 처리해줌.
    
    [Spring Security](https://spring.io/projects/spring-security)
    

- **API 문서화, 테스트 :**  **Swagger**
    
    : 작성한 API를 자동으로 문서화해주고 API의 테스트를 간단히 해볼 수 있다.
    
    : 특히 입력 데이터의 형태를 직관적으로 보여줘 프론트에서 넘겨야 할 데이터의 형태를 정하는 데 도움이 많이 되었다.
    
    [](https://swagger.io/)
    

- ****************************************버전 관리 : Github****************************************
    
    : 기능이나 UI 구현 시 각 기능별, UI별로 커밋하여 버전 관리(?)
    
    [Commits · dbtmdgks7897/asset-portfolio-site](https://github.com/dbtmdgks7897/asset-portfolio-site/commits/main?before=8dc697f21998b8d807d5e11823b6eb2ad2bcf543+35&branch=main&qualified_name=refs/heads/main)
    

## +포트폴리오 사진

# 설계
https://www.notion.so/5c6fbfd8ea5e4eda8919323cd10845a5?pvs=4#2a377ee9c8ed47dcb1cd4ad9346ba5d4

# 구현
https://www.notion.so/5c6fbfd8ea5e4eda8919323cd10845a5?pvs=4#fb3f2d0669774c7cab737f4db66473bf
