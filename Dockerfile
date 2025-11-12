FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build --no-daemon --no-configuration-cache

FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN apt-get update && \
    apt-get install wget postgresql-client --yes && \
    mkdir --parents ~/.postgresql && \
    wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
         --output-document ~/.postgresql/root.crt && \
    chmod 0655 ~/.postgresql/root.crt

RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appuser /app

USER appuser

EXPOSE 8888

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8888/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]