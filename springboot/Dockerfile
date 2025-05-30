# 使用官方 OpenJDK 基礎鏡像
#FROM openjdk:21-jdk-slim
FROM eclipse-temurin:21-jdk
# 設置工作目錄
WORKDIR /app

# 將構建的 JAR 文件複製到容器中
COPY target/springboot-0.0.1-SNAPSHOT.jar app.jar
# 暴露 Spring Boot 默認端口
EXPOSE 9090

# 啟動應用
ENTRYPOINT ["java", "-jar", "app.jar"]