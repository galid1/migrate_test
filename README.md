## Setting
#### aws 설정 (s3)
1. 사용자의 home에 `.aws` 디렉토리 생성
2. `.aws` 디렉토리 하위에 credentials 파일 생성
3. s3에 접근 가능한 IAM User를 만들어, 해당 정보를 아래와 같이 입력
```
[default]
aws_access_key_id = ...
aws_secret_access_key = ...
```


## tourcash 설정(database, jwtConfig, firebase)
1. 사용자의 home에 `.refund` 디렉토리 생성

#### database 설정
2. `.refund` 디렉토리 하위에 databaseAccount.txt 파일 생성
3. 파일 내부에 database 유저정보에 맞게 아래내용 기입
```
username= ...
password= ...
```

#### firebase 설정
2. firebase에서 생성한 project key를 `.refund` 디렉토리 하위로 이동


#### jwt 설정
2. `.refund` 디렉토리 하위에 jwtConfig.txt 파일 생성
3. 파일 내부에 아래 내용 기입
```
secret= // access token secret key
expiration= // access token 만료기간 (초단위)
```