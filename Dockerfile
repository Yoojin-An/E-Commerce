# 베이스 이미지 설정
FROM openjdk:21-jdk

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 Docker 이미지에 추가
COPY build/libs/*.jar app.jar

# 포트 설정
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
