# Топ вопросов по Spring
## Оглавление
Spring Core
- [Что такое Spring Framework и каковы его основные компоненты?](#что-такое-spring-framework-и-каковы-его-основные-компоненты)
- [Что такое Inversion of Control (IoC) и как он работает в Spring?](#что-такое-inversion-of-control-ioc-и-как-он-работает-в-spring)
- [Какие виды Dependency Injection (DI) поддерживаются в Spring?](#какие-виды-dependency-injection-di-поддерживаются-в-spring)
- [Что такое ApplicationContext и чем он отличается от BeanFactory?](#что-такое-applicationcontext-и-чем-он-отличается-от-beanfactory)
- [Какие способы определения бинов существуют в Spring?](#какие-способы-определения-бинов-существуют-в-spring)
- [Как работает аннотация @Autowired?](#как-работает-аннотация-autowired)
- [Что такое аннотация @Component и в чём разница между @Component, @Service, @Repository и @Controller?](#что-такое-аннотация-component-и-в-чём-разница-между-component-service-repository-и-controller)
- [Что такое аннотация @Scope и какие виды скоупов поддерживаются?](#что-такое-аннотация-scope-и-какие-виды-скоупов-поддерживаются)
- [Что такое AOP (Aspect-Oriented Programming) и как оно реализовано в Spring?](#что-такое-aop-aspect-oriented-programming-и-как-оно-реализовано-в-spring)
- [Как использовать Spring Profiles?](#как-использовать-spring-profiles)

Spring Boot
- [Что такое Spring Boot и какие его основные преимущества?](#что-такое-spring-boot-и-какие-его-основные-преимущества)
- [Как работает автоматическая настройка (Auto-Configuration) в Spring Boot?](#как-работает-автоматическая-настройка-auto-configuration-в-spring-boot)
- [Что такое аннотация @SpringBootApplication и что она делает?](#что-такое-аннотация-springbootapplication-и-что-она-делает)
- [Как запустить Spring Boot приложение?](#как-запустить-spring-boot-приложение)
- [Что такое Spring Boot Starter и зачем он нужен](#что-такое-spring-boot-starter-и-зачем-он-нужен)
- [Как конфигурировать Spring Boot приложение?](#как-конфигурировать-spring-boot-приложение)
- [Как реализовать обработку ошибок в Spring Boot?](#как-реализовать-обработку-ошибок-в-spring-boot)
- [Что такое actuator в Spring Boot?](#что-такое-actuator-в-spring-boot)
- [Как работает hot-reload в Spring Boot?](#как-работает-hot-reload-в-spring-boot)
- [Что такое embedded server и как его настроить в Spring Boot?](#что-такое-embedded-server-и-как-его-настроить-в-spring-boot)

Spring Data
- [Что такое Spring Data и какие задачи решает?](#что-такое-spring-data-и-какие-задачи-решает)
- [Что такое репозиторий в Spring Data?](#что-такое-репозиторий-в-spring-data)
- [Как работает автоматическая генерация запросов в Spring Data?](#как-работает-автоматическая-генерация-запросов-в-spring-data)
- [Как работают кастомные запросы в Spring Data?](#как-работают-кастомные-запросы-в-spring-data)
- [Что такое пейджинг (Pagination) и сортировка (Sorting) в Spring Data?](#что-такое-пейджинг-pagination-и-сортировка-sorting-в-spring-data)
- [Что такое транзакции в Spring Data?](#что-такое-транзакции-в-spring-data)
- [Как Spring Data JPA работает с сущностями и как управляется их жизненный цикл?](#как-spring-data-jpa-работает-с-сущностями-и-как-управляется-их-жизненный-цикл)
- [Как Spring Data интегрируется с различными БД?](#как-spring-data-интегрируется-с-различными-бд)
- [Как работают проекции (Projections) в Spring Data?](#как-работают-проекции-projections-в-spring-data)
- [Как работают методы обновления в Spring Data?](#как-работают-методы-обновления-в-spring-data)

Spring Security
- [Что такое Spring Security и для чего он используется?](#что-такое-spring-security-и-для-чего-он-используется)
- [Как работает аутентификация в Spring Security?](#как-работает-аутентификация-в-spring-security)
- [Как настроить авторизацию в Spring Security?](#как-настроить-авторизацию-в-spring-security)
- [Что такое фильтры в Spring Security?](#что-такое-фильтры-в-spring-security)
- [Что такое CSRF и как его предотвратить в Spring Security?](#что-такое-csrf-и-как-его-предотвратить-в-spring-security)
- [Как настроить форму входа в Spring Security?](#как-настроить-форму-входа-в-spring-security)
- [Что такое Basic Authentication и как её настроить?](#что-такое-basic-authentication-и-как-её-настроить)
- [Как интегрировать Spring Security с OAuth2?](#как-интегрировать-spring-security-с-oauth2)
- [Как настроить защиту API через Spring Security?](#как-настроить-защиту-api-через-spring-security)
- [Как работает двухфакторная аутентификация (2FA) в Spring Security?](#как-работает-двухфакторная-аутентификация-2fa-в-spring-security)
# Spring Core
## Что такое Spring Framework и каковы его основные компоненты?
Spring — это фреймворк для создания Java-приложений. Его основные компоненты: Inversion of Control (IoC) контейнер, AOP 
(аскетно-ориентированное программирование), Transaction Management, Spring Data, Spring MVC, Spring Security и др.
## Что такое Inversion of Control (IoC) и как он работает в Spring?
IoC — это принцип, согласно которому управление созданием объектов передается внешнему контейнеру. В контексте Spring
это означает, что контейнер сам управляет тем, какие объекты создавать, как они должны быть связаны друг с другом и
когда они должны быть уничтожены. В Spring IoC реализован через механизм Dependency Injection (DI). В DI зависимости
объектов (другие объекты, от которых они зависят) передаются контейнером Spring. Это может происходить через
конструкторы, сеттеры или поля, и позволяет сделать код более гибким и легким в поддержке.
## Какие виды Dependency Injection (DI) поддерживаются в Spring?
Spring поддерживает три основных вида внедрения зависимостей:
- Внедрение через конструктор: Зависимости передаются при создании объекта через конструктор. Это наиболее 
предпочтительный метод, так как он позволяет сделать объекты неизменяемыми и сразу готовыми к использованию.
- Внедрение через сеттеры: Зависимости передаются через сеттеры (методы для установки значений). Этот метод более 
гибкий, так как позволяет изменять зависимости после создания объекта.
- Внедрение через поля: Используя аннотацию @Autowired, Spring может напрямую внедрять зависимости в поля класса,
минуя конструкторы и сеттеры. Это удобный способ, но менее гибкий по сравнению с первыми двумя.
## Что такое ApplicationContext и чем он отличается от BeanFactory?
ApplicationContext и BeanFactory — это два основных контейнера в Spring. BeanFactory — это минималистичный контейнер,
который управляет созданием и жизненным циклом бинов. Он прост и может использоваться в небольших приложениях. 
ApplicationContext расширяет возможности BeanFactory и предоставляет такие функции, как:
- Управление сканированием компонентов.
- Поддержка сквозной логики (AOP).
- Работа с событиями (event handling).
- Интеграция с внешними ресурсами (например, файл конфигурации messages.properties для i18n).
## Какие способы определения бинов существуют в Spring?
Через XML-конфигурацию, аннотации (@Component, @Service, @Repository, @Controller), а также с помощью Java-based 
конфигурации (@Configuration и @Bean).
## Как работает аннотация @Autowired?
Аннотация @Autowired в Spring автоматически внедряет зависимости по типу. Spring пытается найти подходящий бин в
контейнере, который соответствует типу переменной или параметра, и передает его. Пример:
```java
@Autowired
private MyService myService;
```
Spring находит бин типа MyService и внедряет его в поле myService. Если бин не найден или бинов по интерфейсу несколько,
то может возникнуть ошибка, но можно использовать аннотацию @Qualifier для уточнения.
## Что такое аннотация @Component и в чём разница между @Component, @Service, @Repository и @Controller?
Аннотация @Component говорит Spring о том, что этот класс нужно зарегистрировать как бин в контексте. Однако у нас
также есть специализированные аннотации:
- @Service: используется для классов, содержащих бизнес-логику. Это всего лишь специализированная форма @Component,
которая делает код понятнее.
- @Repository: применяется для классов, работающих с базой данных. Она также автоматически обрабатывает исключения в
Spring Data.
- @Controller: применяется для веб-контроллеров в Spring MVC. Это специальная форма @Component, которая указывает
Spring MVC, что данный класс отвечает за обработку HTTP-запросов.
## Что такое аннотация @Scope и какие виды скоупов поддерживаются?
@Scope указывает на область видимости бина. Поддерживаются скоупы: singleton, prototype, request, session, 
globalSession. [Подробнее про скоупы](../spring/bean_info.md)
## Что такое AOP (Aspect-Oriented Programming) и как оно реализовано в Spring?
AOP (аскетно-ориентированное программирование) позволяет отделить сквозную логику от бизнес-логики. Это удобно, когда
необходимо, например, добавить логирование или обработку исключений в несколько методов, не засоряя основной код. 
В Spring AOP реализуется через прокси-объекты, которые оборачивают исходные бины и добавляют необходимую 
функциональность. Для работы с AOP используются аннотации, такие как @Aspect, @Before, @After, @Around, и другие.
## Как использовать Spring Profiles?
Профили позволяют группировать разные конфигурации для различных сред (например, разработка, тестирование, продакшен). 
Чтобы использовать профили, достаточно пометить конфигурационный класс или метод аннотацией @Profile("dev").
Активировать профиль можно через параметр командной строки: -Dspring.profiles.active=dev, либо указать его в 
конфигурационном файле.
# Spring Boot
## Что такое Spring Boot и какие его основные преимущества?
Spring Boot — это фреймворк для упрощения разработки на Spring. Основные преимущества: автоматическая настройка
(Auto-configuration), встроенные сервлет-контейнеры (Tomcat, Jetty), поддержка Spring Data, Spring Security и
мониторинга через соответствующие стартеры.
## Как работает автоматическая настройка (Auto-Configuration) в Spring Boot?
Автоматическая конфигурация в Spring Boot позволяет избежать написания явных конфигураций. Когда Spring Boot 
обнаруживает библиотеки в classpath, он автоматически настраивает нужные компоненты. Например, если Spring Boot
обнаружит spring-boot-starter-data-jpa, он сконфигурирует DataSource и EntityManager для работы с JPA.
## Что такое аннотация @SpringBootApplication и что она делает?
@SpringBootApplication — это мета-аннотация, которая включает три основные аннотации:
- @Configuration — указывает, что класс содержит определения бинов.
- @EnableAutoConfiguration — включает механизм автоматической настройки Spring Boot.
- @ComponentScan — автоматически сканирует пакеты для поиска компонентов, конфигураций и сервисов.
## Как запустить Spring Boot приложение?
Spring Boot-приложение можно запустить с помощью встроенного сервлета-контейнера (например, Tomcat) через команду 
java -jar <app>.jar, или с помощью mvn spring-boot:run.
## Что такое Spring Boot Starter и зачем он нужен
Стартеры — это наборы предопределённых зависимостей, которые упрощают подключение функционала (например,
spring-boot-starter-web для веб-приложений или spring-boot-starter-data-jpa для работы с базами данных).
## Как конфигурировать Spring Boot приложение?
Конфигурация Spring Boot выполняется через файл application.properties или application.yml, где можно указать параметры
базы данных, настройки сервера и прочие параметры приложения.
## Как реализовать обработку ошибок в Spring Boot?
Обработку ошибок можно настроить через аннотацию @ControllerAdvice, либо через специальные страницы ошибок
(error pages). Spring Boot автоматически предоставляет базовые страницы ошибок.
## Что такое actuator в Spring Boot?
Spring Actuator — это модуль Spring Boot, который предоставляет мониторинг и управление приложением. Он добавляет
готовые эндпоинты для отслеживания состояния приложения (например, /health, /info, /metrics). Эти эндпоинты можно
использовать для получения информации о ресурсах, состоянии памяти, запущенных потоках и многом другом.

Для добавления пользовательских метрик в Actuator, можно использовать аннотацию @Timed, @Gauge, или вручную
зарегистрировать свои метрики через интерфейс MeterRegistry. Например:
```java
@Autowired
private MeterRegistry meterRegistry;

@PostConstruct
public void initMetrics() {
    meterRegistry.gauge("custom.metric", this, CustomClass::calculateValue);
}
```
## Как работает hot-reload в Spring Boot?
Spring Boot DevTools — это модуль, который ускоряет разработку. Он автоматически перезагружает приложение при изменении
кода, упрощая процесс разработки. DevTools также добавляет поддержку live reload (автоматической перезагрузки браузера)
и отключает кеширование шаблонов для более удобной отладки.
## Что такое embedded server и как его настроить в Spring Boot?
Встроенный сервер (например, Tomcat) позволяет запускать Spring Boot-приложение без внешнего сервера. Настраивается
через application.properties (например, изменение порта: server.port=8081).
# Spring Data
## Что такое Spring Data и какие задачи решает?
Spring Data упрощает взаимодействие с базами данных, предоставляя обобщённый API для разных типов хранилищ данных (SQL, 
NoSQL). Основные задачи: создание репозиториев, автоматическая генерация запросов, поддержка транзакций.
## Что такое репозиторий в Spring Data?
Репозиторий — это интерфейс для работы с сущностями в базе данных. Spring Data предоставляет несколько базовых
интерфейсов, таких как CrudRepository, JpaRepository.
## Как работает автоматическая генерация запросов в Spring Data?
Spring Data позволяет автоматически создавать SQL-запросы на основе именования методов репозитория, например,
findByName сгенерирует запрос для поиска по полю name.
## Как работают кастомные запросы в Spring Data?
Для создания кастомных запросов можно использовать аннотацию @Query и писать SQL или JPQL запросы вручную.
## Что такое пейджинг (Pagination) и сортировка (Sorting) в Spring Data?
Spring Data поддерживает пейджинг и сортировку через интерфейс Pageable. Методы, возвращающие результаты с пейджингом,
должны принимать объект Pageable.
## Что такое транзакции в Spring Data?
Транзакции управляются через аннотацию @Transactional. Она позволяет определить, в каких методах нужно начинать, 
завершать или откатывать транзакции. [Подробнее про @Transactional](../spring/transactional.md)
## Как Spring Data JPA работает с сущностями и как управляется их жизненный цикл?
Сущности управляются EntityManager'ом. Жизненный цикл сущности включает состояния: Transient, Persistent и Detached.
## Как Spring Data интегрируется с различными БД?
Spring Data поддерживает множество баз данных, включая реляционные (через Spring Data JPA) и NoSQL
(через Spring Data MongoDB, Redis и т.д.).
## Как работают проекции (Projections) в Spring Data?
Проекции позволяют выбрать часть полей из сущности и вернуть её в виде другого объекта (DTO). Проекции могут быть как 
интерфейсными, так и классовыми.
## Как работают методы обновления в Spring Data?
Для выполнения операций обновления и удаления в Spring Data используется аннотация @Modifying совместно с @Query,
чтобы вручную указать нужный SQL-запрос.
# Spring Security
## Что такое Spring Security и для чего он используется?
Spring Security — это мощный и гибкий фреймворк для обеспечения безопасности Java-приложений. Он предоставляет множество
функций для аутентификации, авторизации, защиты от атак, таких как CSRF и XSS, и управления доступом к ресурсам. 
Основные компоненты:
- Аутентификация: процесс проверки подлинности пользователя. Это может быть выполнено с помощью логина и пароля,
OAuth2, JWT и т.д.
- Авторизация: определяет, какие ресурсы доступны пользователю на основе его ролей или привилегий.
- Защита от атак: включает встроенные механизмы для предотвращения атак, таких как CSRF (подмена межсайтовых запросов),
XSS (межсайтовый скриптинг).
## Как работает аутентификация в Spring Security?
Аутентификация — это процесс подтверждения личности пользователя. В Spring Security она настраивается через цепочку
фильтров, а также через конфигурацию в классе, расширяющем WebSecurityConfigurerAdapter. По умолчанию Spring использует
форму логина или базовую аутентификацию (Basic Authentication), но можно настроить аутентификацию с использованием
OAuth2, JWT, LDAP и других методов. Пример:
```java
http
    .authorizeRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .and()
    .formLogin()
    .loginPage("/login")
    .permitAll();
```
## Как настроить авторизацию в Spring Security?
Авторизация управляет доступом к ресурсам на основе ролей или привилегий пользователя. В Spring Security авторизация
настраивается с помощью метода authorizeRequests(), где можно указать, какие роли или права необходимы для доступа к 
различным URL-адресам. Пример:
```java
http
    .authorizeRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
    .and()
    .formLogin();
```
## Что такое фильтры в Spring Security?
Фильтры в Spring Security обрабатывают запросы до их передачи в контроллер. Основные фильтры:
- Фильтр аутентификации проверяет, аутентифицирован ли пользователь, и при необходимости перенаправляет на страницу входа.
- Фильтр защиты от CSRF предотвращает атаки CSRF, проверяя валидность CSRF-токенов. Каждый фильтр обрабатывает
определенные аспекты безопасности. Например, фильтр UsernamePasswordAuthenticationFilter отвечает за аутентификацию
через логин и пароль.
## Что такое CSRF и как его предотвратить в Spring Security?
CSRF (Cross-Site Request Forgery) — это атака, при которой злоумышленник заставляет пользователя выполнить нежелательные
действия. В Spring Security CSRF защита включена по умолчанию, но её можно отключить или настроить.
## Как настроить форму входа в Spring Security?
В Spring Security форму входа можно настроить с помощью метода formLogin(). Этот метод позволяет указать URL для 
страницы входа, страницы ошибок и успешного выхода из системы. Пример:
```java
http
    .formLogin()
    .loginPage("/login")
    .defaultSuccessUrl("/home")
    .failureUrl("/login?error=true")
    .permitAll();
```
## Что такое Basic Authentication и как её настроить?
Basic Authentication — это простой способ аутентификации, при котором логин и пароль передаются в HTTP-заголовке
запроса. В Spring Security Basic Authentication настраивается с помощью метода httpBasic(). Пример:
```java
http
    .authorizeRequests()
    .anyRequest().authenticated()
    .and()
    .httpBasic();
```
## Как интегрировать Spring Security с OAuth2?
OAuth2 — это протокол для авторизации через сторонние сервисы, такие как Google, Facebook и GitHub. Spring Security
предоставляет встроенную поддержку OAuth2, и интеграция может быть выполнена через аннотации и настройки конфигурации.
Пример:
```java
http
    .oauth2Login()
    .loginPage("/login")
    .defaultSuccessUrl("/home", true);
```
## Как настроить защиту API через Spring Security?
API можно защитить с помощью токенов (например, JWT). Для этого нужно настроить фильтр, который будет проверять токены 
на валидность при каждом запросе.
## Как работает двухфакторная аутентификация (2FA) в Spring Security?
2FA добавляет второй уровень проверки (например, через SMS или приложение). Реализация требует настройки дополнительных
фильтров или интеграции с внешними сервисами (например, Google Authenticator).
