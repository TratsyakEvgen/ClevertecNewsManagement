# News management system

Проект реализует систему управления новостей и их пользователе.
Проект состоит из двух сервисов: News Service и User service

### 1. News service
#### 1.1 Описание

Поддержка CRUD для новостей и комментариев к ним, включая пагинацию и полнотекстовый поиск.

#### 1.2 Конечные точки api

С интерфейсом приложение можно ознакомится по ссылке: [News API](https://tratsyakevgen.github.io/ClevertecNewsManagement/news.html), 
либо после запуска приложения по URI `/swagger-ui/index.html`.

#### 1.3 Кэширование

Приложение поддерживает два режима кэширование: на основе кэш провайдер Redis и реализации LFU/LRU кэша;

#### 1.3.1 Кэширование Redis

Для запуска приложения с поддержкой Redis, приложение необходимо запустить под профилем `prod-redis`

#### 1.3.2 LFU/LRU

Для запуска приложения с поддержкой LFU/LRU кэша, приложение необходимо запустить под профилем `prod`.
Для выбора алгоритма кэширования укажите в `application-prod.yaml` в свойстве 
`cache.algorithm:` соответсвующий алгоритм (LFU или LRU). Свойство `cache.capacity:` 
служит для указания максимального размера кэша.

#### 1.4 Безопасность

Для обращения к защищенным конечным точкам необходим JWT токен. Для его получения обратитесь к конечной точке
[/tokens](https://tratsyakevgen.github.io/ClevertecNewsManagement/news.html#/tokens/createToken),
указав в теле запроса имя пользователя и его пароль. После этого News service запросит информацию о роли у User service 
и сгенерирует токен. За генерацию и верификацию токена отвечает News service. Для указания времени жизни токена укажите в
`application.yaml` в свойстве `jwt.expiry-time:` соответствующие значение в миллисекундах.

### 2. User service
#### 2.1 Описание

Отвечает за регистрацию пользователей и предоставлении информации о роли пользователя. Поддерживаемые роли:
admin, journalist, and subscriber.

#### 2.2 Конечные точки api

С интерфейсом приложение можно ознакомится по ссылке: [User API](https://tratsyakevgen.github.io/ClevertecNewsManagement/user.html),
либо после запуска приложения по URI `/swagger-ui/index.html`.

### 3. Запуск приложения

1. Клонируйте проект с удаленного репозитория:
    * `git clone -b feature/news https://github.com/TratsyakEvgen/ClevertecNewsManagement.git`   

2. Перейдите в каталог:
    * `cd ClevertecNewsManagement`

3. Соберите проект:
    * `gradlew build`

4. Разверните контейнеры c одним из профилей (algorithm-cache - для LFU/LRU кэша или redis-cache для Redis кэша):
    * `docker-compose --profile algorithm-cache up -d`
    * `docker-compose --profile redis-cache up -d`
