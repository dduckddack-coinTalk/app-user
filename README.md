# CoinTalk 유저 서비스
![java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white) ![Mysql](https://img.shields.io/badge/-MySQL-4479A1?&style=for-the-badge&logo=MySQL&logoColor=white) ![EC2](https://img.shields.io/badge/-EC2-232F3E?&style=for-the-badge&logo=Amazon-AWS&logoColor=white) ![GitHub](https://img.shields.io/badge/-Github-181717?&style=for-the-badge&logo=Github&logoColor=white) ![Slack](https://img.shields.io/badge/-Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)



## Service Architecture
![image](https://user-images.githubusercontent.com/57323359/169326451-cfad47d4-d006-4093-93ba-a7855ef84b84.png)







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

