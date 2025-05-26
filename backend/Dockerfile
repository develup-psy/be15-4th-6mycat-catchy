## 1. 빌드 스테이지 시작
# Gradle 이미지를 가져오고, 빌드 스테이지를 'build'라는 이름으로 설정한다.
FROM gradle:jdk17 AS build

# 컨테이너 내부의 작업 디렉토리를 /app으로 설정한다.
WORKDIR /app

# 현재 디렉토리 내의 모든 파일과 폴더를 컨테이너의 /app 디렉토리로 복사한다.
COPY . .

# Gradle을 사용하여 테스트를 제외하고 빌드한다. (daemon 프로세스는 사용하지 않는다.)
RUN gradle clean build -x test --no-daemon

## 2. 실행 스테이지 시작
# OpenJDK 17 버전의 이미지를 가져와 JVM 환경을 구축한다.
FROM openjdk:17

# 빌드 스테이지에서 생성된 JAR 파일을 복사한다.
COPY --from=build /app/build/libs/*.jar ./

# plain이 아닌 JAR을 app.jar로 변경
RUN mv $(ls *.jar | grep -v plain) app.jar

# wait-for-it.sh 스크립트를 복사하고 실행 권한 부여
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# DB가 준비될 때까지 대기 후 Spring Boot 애플리케이션 실행
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]
