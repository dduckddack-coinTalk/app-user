# CoinTalk 유저 서비스
![java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white)![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)![Mysql](https://img.shields.io/badge/-MySQL-4479A1?&style=for-the-badge&logo=MySQL&logoColor=white)![EC2](https://img.shields.io/badge/-EC2-232F3E?&style=for-the-badge&logo=Amazon-AWS&logoColor=white)![GitHub](https://img.shields.io/badge/-Github-181717?&style=for-the-badge&logo=Github&logoColor=white)![GithubAction](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)![Slack](https://img.shields.io/badge/-Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white) 

​        

## Service Architecture
![image](https://user-images.githubusercontent.com/57323359/169326451-cfad47d4-d006-4093-93ba-a7855ef84b84.png)



## How to Setting

### mysql 설정

1. mysql을 설치해 줍니다. 
2. 원하는 database name을 설정합니다.
3. `application.properties` 에 해당 정보들을 설정합니다.

​                 

### 인증 메일 설정

1. 인증 메일을 발송할 계정을 하나 생성합니다.
2. 2단계 인증을 합니다.
3. 앱 비밀번호를 생성해서 기록해 둡니다. [[참고 도움말]](https://support.google.com/mail/answer/185833)
4. `application.properties의` ` email` 항목에 입력합니다.

​             

### EC2, S3 설정

- EC2와 S3 관련 설정을 끝내고 관련 정보를 `application.properties`에 입력합니다. 

​                 


### application.properties 설정

1. `.gitignore` 에 등록되어 있기 때문에 `application.properties` 가 포함되지 않습니다.
2. `application.properties.sample` 을 복사해서 `application.proeprties` 를 생성해서 사용합니다.

```properties
// application.properties

spring.r2dbc.url=r2dbc:mysql://localhost:3306/databaseName?characterEncoding=utf8
spring.r2dbc.username=mysql username
spring.r2dbc.password=mysql password
secret=jwt에 대한 secret입니다. 
accessExpire=7200000와 같은 숫자값
refreshExpire=7200000와 같은 숫자값

# email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# aws s3 setting
cloud.aws.s3.bucket=
cloud.aws.credentials.access-key=
cloud.aws.credentials.secret-key=
cloud.aws.region.static=
cloud.aws.region.auto=false
cloud.aws.stack.auto=false
cloud.aws.s3.bucket.url=
cloud.aws.s3.bucket.my.download.url=


# thymleaf setting
# 로컬에서 front클라를 간단하게 띄워서 사용할 경우에 추가 합니다. 아니면 없어도 됩니다.
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

​              

### Github Action Secret 설정

1. `.github` 디렉토리 안의 `deploy.yml`, `pullrequest.yml` 에 설정값들이 존재합니다.
2. `https://github.com/{your-id}/{your-repository}/settings/secrets/actions` 경로에서 아래의 값들을  `New repository secret` 로 설정해 줘야 합니다. 해당 값들은 application.properties의 값들과 동일합니다. 
3. 2개의 yml에 보면 github action 빌드 과정에서 application.proeprties를 동적으로 생성할 때 아래의 값들이 `application.properties` 에서 어떤 값으로 들어가는지 확인이 가능합니다. 
4. **참고로 `.github` 안의 디렉토리 안의 `deploy.yml`, `pullrequest.yml` 를 삭제하면 아래의 값들은 설정할 필요가 없습니다.**

```yaml
// github action secret setting

MYSQL_DATABASE
MYSQL_USER
MYSQL_PASSWORD

TOKEN_SECRET
TOKEN_ACCESSEXPIRE
TOKEN_REFRESHEXPIRE

MAIL_USERNAME
MAIL_PASSWORD

# cloud.aws.credentials.access-key
CREDENTIALS_ACCESS_KEY
# cloud.aws.credentials.secret-key
CREDENTIALS_SECRET_KEY

AWS_ACCESS_KEY
AWS_SECRET_KEY
```

​          

### Husky 설정

- commit lint를 좀더 쉽게 설정하기 위해서 husky를 사용했습니다.
- commitlint.config.js를 통해서 설정이 가능합니다.

```shell
# node.js와 yarn 이미 설치된 상황에서 진행합니다. 
$ git clone https://github.com/dduckddack-coinTalk/app-user.git
$ cd app-user
$ yarn
```

​    

### 개발환경 설정

1. 저장소를 클론해서 받습니다.

```shell
$ git clone https://github.com/dduckddack-coinTalk/app-user.git
```

2. intellij로 해당 프로젝트를 실행합니다.
3. 우측 Gradle탭을 열어서 Reload All Gradle Projects를 한다음 그 위의 있는 ▶︎ 버튼을 클릭합니다.

<img width="406" alt="image" src="https://user-images.githubusercontent.com/57323359/169352956-8a3f0d80-cdf5-4047-bb47-3edc0e006408.png">










# REST API

### URI : `POST` /user/account
유저를 생성하는 API

#### Parameters

| name     |  type  | Description | Default |
| :------- | :----: | :---------: | :-----: |
| email    | String | 유저 이메일 |         |
| password | String |  패스워드   |         |
| nickName | String | 유저 닉네임 |         |



#### Responses
```json
{
    "status": "ok",
    "message": "유저 생성 성공"
}
```



---


### URI : `POST` /user/login
유저 로그인 하는 API

#### Parameters

| name     |  type  | Description | Default |
| :------- | :----: | :---------: | :-----: |
| email    | String | 유저 이메일 |         |
| password | String |  패스워드   |         |



#### Responses
```json
{
    "status": "ok",
    "message": "유저 로그인 성공",
    "accessToken": "",
    "refreshToken": "",
    "userInfo": {
        "id": 1,
        "email": "test1@test.com",
        "nickName": "test1",
        "imagePath": "https://..."
    }
}
```



---


### URI : `PUT` /user/account
유저 정보를 수정하는 API

#### Parameters

| name     |  type  |    Description     | Default |
| :------- | :----: | :----------------: | :-----: |
| email    | String |    유저 이메일     |         |
| password | String |      패스워드      |         |
| nickName | String |    유저 닉네임     |         |
| file     |  File  | 유저 프로필 이미지 |         |



#### Responses
```json
{
    "status": "ok",
    "message": "유저 변경 성공"
}
```



---


### URI : `POST` /user/email/{email}/authentication
가입하려는 유저의 이메일에 대한 인증 요청하는 API

 


#### Responses
```json
{
    "status": "ok",
    "message": "유저 인증 메일 발송 성공"
}
```



---

### URI : `GET` /user/email/{email}/authentication

가입하려는 유저의 이메일에 대한 인증 결과를 확인하는 API

 

#### Responses
```json
{
    "status": "ok",
    "message": "유저 생성 성공"
}
```

