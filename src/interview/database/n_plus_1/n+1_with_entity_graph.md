# Проблема N+1 и как её решить с помощью EntityGraph
## Оглавление
- [Суть проблемы](#суть-проблемы)
- [Создание проекта](#создание-проекта-и-подключение-зависимостей)
- [Создание сущностей](#создание-сущностей)
- [Создание конфигов](#создание-конфигов)
- [Создание бинов](#создание-бинов)
- [Получение проблемы N+1](#получение-проблемы-n1)
- [Что такое Entity Graph](#что-такое-entity-graph)
- [1 способ](#1-способ)
- [2 способ](#2-способ)
## Суть проблемы
Проблема N+1 возникает, когда мы генерируем запрос на получение одной сущности из БД, но у данной сущности есть свои 
связанные сущности, которые мы тоже хотим получить и Hibernate генерирует вначале 1 запрос к БД, чтобы получить 
интересующую нас сущность, а потом N запросов, чтобы достать из БД связанные сущности. Данная проблема отражается 
отрицательно на производительности БД из-за большого числа обращений к ней.
## Создание проекта и подключение зависимостей
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.github.javafaker</groupId>
        <artifactId>javafaker</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```
## Создание сущностей
```java
@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<EmailAddress> emailAddresses;

    public Client(String fullName, String mobileNumber, List<EmailAddress> emailAddresses) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.emailAddresses = emailAddresses;
    }
}

@Entity
@Table(name = "email_address")
@Data
@NoArgsConstructor
public class EmailAddress {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email")
    private String email;
    
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    
    public EmailAddress(String email) {
        this.email = email;
    }
}
```
## Создание конфигов
```application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_name
spring.datasource.password=your_password

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database=postgresql

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
## Создание бинов
```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByFullNameContaining(String name);
}

@Service
public class ClientService {
    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }
    
    public List<Client> findByNameContaining(String userName) {
        return repository.findByFullNameContaining(userName);
    }

    public void generateDB() {
        List<Client> clients = create2000Clients();
        for (int i = 0; i < clients.size(); i++) {
            repository.save(clients.get(i));
        }
    }
    
    public List<Client> create2000Clients() {
        List<Client> clients = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < 2000; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String sufixTel = String.valueOf(i);
            String telephone = "+375290000000";

            List<EmailAddress> emailAddresses = Arrays.asList(
                    new EmailAddress((firstName + lastName).toLowerCase() + "1" + i + "@gmail.com"),
                    new EmailAddress((firstName + lastName).toLowerCase() + "2" + i + "@gmail.com")
            );
            telephone = telephone.substring(0, telephone.length() - sufixTel.length()) + sufixTel;
            Client client = new Client(firstName + lastName, telephone, emailAddresses);
            
            for (EmailAddress emailAddress : emailAddresses) {
                emailAddress.setClient(client);
            }
            
            clients.add(client);
        }
        
        return clients;
    }
}

@RestController
@RequestMapping("api/v1/client")
public class ClientController {
    private final ClientService service;
    private final ClientRepository repository;
    
    @Autowired
    public ClientController(ClientService service, ClientRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Client> findByNameContaining(@RequestParam String clientName) {
        return service.findByNameContaining(clientName);
        
    }
    
    @GetMapping("/fillDB")
    @ResponseStatus(HttpStatus.OK)
    public String fillDataBase() {
        service.generateDB();
        return "Amount clients: " + repository.count();
    }
}
```
## Получение проблемы N+1
Переходим в Postman отправляем поиск по имени Ren. В консоли видим, что hibernate сделал сначала 1 запрос в БД в таблицу 
client и нашёл всех клиентов, а потом ещё N-запросов к таблице email_address, чтобы получить у каждого клиента email.

Как решить проблему? Наша задача сократить кол-во запросов до 1. Есть несколько возможных решений, но рассмотрим решение
с помощью JPA Entity Graph.
## Что такое Entity Graph
Начиная с JPA 2.1 появилась Entity Graph. Entity Graph позволяет улучшить производительность во время выполнения 
запросов к БД при загрузке связанных ассоциаций и основных полей объекта. JPA Entity Graph загружает данные в один 
запрос выбора, избегая повторного обращения к БД.
## 1 способ
Пишем аннотацию @EntityGraph над методом findByFullNameContaining в ClientRepository:
```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @EntityGraph(type = EntityGraph.EntitygraphType.FETCH, attributePaths = "emailAddresses")
    List<Client> findByFullNameContaining(String name);
}
```
По умолчанию EntityGraph имеет тип EntityGraphType.FETCH, применяет стратегию FetchType.EAGER к указанным атрибутам, 
т.е. к emailAddresses.

Если снова зайти в Postman и послать запрос, то мы увидим лишь 1 запрос к БД.
## 2 способ
Пишем аннотацию @NamedEntityGraph над классом Client:
```java
@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
@NamedEntityGraph(name = "client_entity-graph", attributeNodes = @NamedAttributeNode("emailAddresses"))
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<EmailAddress> emailAddresses;

    public Client(String fullName, String mobileNumber, List<EmailAddress> emailAddresses) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.emailAddresses = emailAddresses;
    }
}
```
В данном случае будет использоваться жадная загрузка указанной связной сущности emailAddresses.

Также необходимо добавить аннотацию над ClientRepository:
```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "client_entity-graph")
    List<Client> findByFullNameContaining(String name);
}
```
После выполнения запроса снова получим лишь 1 запрос.
