# TripToN 배포 가이드

우분투 VM에 이미 MySQL Docker 컨테이너가 실행 중인 환경에서 TripToN 앱을 배포하는 방법입니다.

---

## 사전 조건

- Ubuntu VM에 Docker, Docker Compose 설치됨
- MySQL 컨테이너(이름: `mysql`)가 실행 중
- Git 설치됨

---

## 1단계: MySQL 컨테이너 네트워크 확인

앱 컨테이너가 MySQL 컨테이너와 통신하려면 **같은 Docker 네트워크**에 있어야 합니다.

```bash
# MySQL 컨테이너가 어떤 네트워크에 속해 있는지 확인
docker inspect mysql --format '{{range $key, $value := .NetworkSettings.Networks}}{{$key}}{{end}}'
```

출력 예시: `bridge` 또는 `my_network` 등 → 이 값을 기억해 두세요.

---

## 2단계: MySQL에 데이터베이스 생성

```bash
# MySQL 컨테이너에 접속
docker exec -it mysql mysql -u root -p
```

```sql
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS TripToNDB
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 확인
SHOW DATABASES;

-- 나가기
EXIT;
```

---

## 3단계: 프로젝트 가져오기

```bash
# 원하는 위치에 클론
cd ~
git clone <your-repository-url> TripToN
cd TripToN
```

---

## 4단계: 환경변수 설정

```bash
# .env 파일 생성
cp .env.example .env
nano .env
```

아래 값들을 본인 환경에 맞게 수정하세요:

```env
# MySQL 접속 정보 (MySQL 컨테이너의 root 비밀번호)
DB_USERNAME=root
DB_PASSWORD=<MySQL_root_비밀번호>
DB_NAME=TripToNDB

# 앱 설정
SERVER_PORT=8080

# JPA 설정
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false

# Gemini API 설정
GEMINI_ENABLED=true
GEMINI_API_KEY=<본인의_Gemini_API_키>

# 1단계에서 확인한 네트워크 이름
DOCKER_NETWORK=bridge
```

> `DOCKER_NETWORK` 값은 **1단계에서 확인한 MySQL 컨테이너의 네트워크 이름**으로 설정합니다.

---

## 5단계: 빌드 및 실행

```bash
# 이미지 빌드 + 컨테이너 실행
docker compose up -d --build
```

빌드에 수 분이 걸릴 수 있습니다. (첫 빌드 시 Gradle 의존성 다운로드)

---

## 6단계: 동작 확인

```bash
# 컨테이너 상태 확인
docker ps

# 앱 로그 확인
docker logs tripton-app

# 로그 실시간 확인
docker logs -f tripton-app
```

로그에서 아래와 비슷한 메시지가 보이면 성공입니다:

```
Started TripToNApplication in X.XXX seconds
```

브라우저에서 접속:

```
http://<우분투_VM_IP>:8080
```

---

## 자주 쓰는 명령어

```bash
# 컨테이너 중지
docker compose down

# 컨테이너 재시작
docker compose restart

# 코드 수정 후 재빌드
docker compose up -d --build

# 앱 로그 확인 (최근 100줄)
docker logs --tail 100 tripton-app
```

---

## 트러블슈팅

### MySQL 연결 실패

```
Communications link failure
```

**원인**: 앱 컨테이너와 MySQL 컨테이너가 다른 네트워크에 있음

**해결**:
```bash
# 1. MySQL 네트워크 재확인
docker inspect mysql --format '{{range $key, $value := .NetworkSettings.Networks}}{{$key}}{{end}}'

# 2. .env의 DOCKER_NETWORK 값을 위 결과로 수정

# 3. 재시작
docker compose down && docker compose up -d
```

또는 수동으로 같은 네트워크에 연결:
```bash
# MySQL이 속한 네트워크에 앱 컨테이너를 직접 연결
docker network connect <네트워크이름> tripton-app
```

### 데이터베이스가 존재하지 않음

```
Unknown database 'TripToNDB'
```

**해결**: 2단계의 데이터베이스 생성을 다시 수행하세요.

### 포트 충돌

```
Bind for 0.0.0.0:8080 failed: port is already allocated
```

**해결**: `.env`에서 `SERVER_PORT`를 다른 포트(예: 8081)로 변경 후 재시작

### Gemini API 오류

로그에 Gemini API 관련 에러가 보이면:
1. `.env`의 `GEMINI_API_KEY`가 올바른지 확인
2. VM에서 외부 인터넷 접속이 가능한지 확인: `curl -I https://generativelanguage.googleapis.com`

---

## 업데이트 배포

코드가 업데이트되었을 때:

```bash
cd ~/TripToN
git pull
docker compose up -d --build
```
