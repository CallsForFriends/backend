# ITMO Calls Backend

Backend-приложение для работы с пользователями и избранными пользователями, построенное на Spring Boot и Kotlin.

## Технологический стек

- **Kotlin** 1.9.25
- **Spring Boot** 3.5.7
- **Java** 21
- **PostgreSQL** (база данных)
- **Liquibase** (миграции базы данных)
- **Gradle** (система сборки)
- **Docker** и **Docker Compose** (контейнеризация)

## Требования

- **JDK 21** или выше
- **Gradle** 8.5+ (или используйте Gradle Wrapper: `./gradlew`)
- **PostgreSQL** (для локальной разработки)
- **Docker** и **Docker Compose** (для запуска через контейнеры)

## Сборка проекта

### Локальная сборка

1. **Клонируйте репозиторий** (если еще не сделано):
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Убедитесь, что у вас установлен JDK 21**:
   ```bash
   java -version
   ```

3. **Соберите проект с помощью Gradle Wrapper**:
   ```bash
   ./gradlew clean build
   ```
   
   Или на Windows:
   ```bash
   gradlew.bat clean build
   ```

4. **Запустите тесты** (опционально):
   ```bash
   ./gradlew test
   ```

5. **Создайте JAR-файл**:
   ```bash
   ./gradlew bootJar
   ```
   
   JAR-файл будет создан в `build/libs/calls-0.0.1-SNAPSHOT.jar`

### Запуск приложения локально

1. **Настройте базу данных PostgreSQL**:
   - Создайте базу данных `itmo_db`
   - Создайте пользователя `itmo` с паролем `password`
   - Или измените настройки в `src/main/resources/application.yml`

2. **Запустите приложение**:
   ```bash
   ./gradlew bootRun
   ```
   
   Или запустите JAR-файл напрямую:
   ```bash
   java -jar build/libs/calls-0.0.1-SNAPSHOT.jar
   ```

3. Приложение будет доступно по адресу: `http://localhost:8080`

## Сборка и запуск через Docker

### Сборка Docker-образа

1. **Соберите Docker-образ**:
   ```bash
   docker build -t itmo-calls-backend .
   ```

### Запуск с Docker Compose

1. **Запустите приложение с помощью Docker Compose**:
   ```bash
   docker-compose up -d
   ```

2. Приложение будет доступно по адресу: `http://localhost:8888`

3. **Остановите приложение**:
   ```bash
   docker-compose down
   ```

4. **Просмотр логов**:
   ```bash
   docker-compose logs -f signalling-server
   ```

## Конфигурация

Основные настройки приложения находятся в файле `src/main/resources/application.yml`:

- **Порт сервера**: по умолчанию `8080` (в Docker - `8888`)
- **База данных**: PostgreSQL
  - URL: `jdbc:postgresql://localhost:5431/itmo_db`
  - Username: `itmo`
  - Password: `password`
- **Liquibase**: автоматически применяет миграции при запуске

### Изменение настроек

Для изменения настроек базы данных или порта отредактируйте файл `application.yml` или используйте переменные окружения:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/itmo_db
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export SERVER_PORT=8080
```

## Структура проекта

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/ru/itmo/calls/
│   │   │   ├── adapter/          # Адаптеры для внешних сервисов
│   │   │   ├── config/            # Конфигурация Spring
│   │   │   ├── controller/        # REST контроллеры
│   │   │   ├── domain/            # Доменная модель
│   │   │   ├── port/              # Порты (интерфейсы)
│   │   │   ├── service/           # Сервисы
│   │   │   └── usecase/           # Use cases
│   │   └── resources/
│   │       ├── application.yml    # Конфигурация приложения
│   │       └── db/changelog/      # Миграции Liquibase
│   └── test/                      # Тесты
├── build.gradle.kts               # Конфигурация Gradle
├── docker-compose.yml             # Docker Compose конфигурация
├── Dockerfile                     # Docker образ
└── README.md                      # Этот файл
```

## API Endpoints

Приложение предоставляет следующие основные endpoints:

- `/api/auth/login` - Авторизация
- `/api/auth/logout` - Выход
- `/api/users/current` - Получение текущего пользователя
- `/api/users/{id}` - Получение пользователя по ID
- `/api/users` - Поиск пользователей по фильтру
- `/api/favourites` - Работа с избранными пользователями

## Разработка

### Запуск в режиме разработки

1. Убедитесь, что PostgreSQL запущен и доступен
2. Запустите приложение:
   ```bash
   ./gradlew bootRun
   ```

### Запуск тестов

```bash
./gradlew test
```
