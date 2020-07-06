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