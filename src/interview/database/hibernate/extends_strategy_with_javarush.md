# Наследование в Hibernate
## Оглавление
- [Структура классов](#структура-классов)
- [Виды решений](#виды-решений)
- [@MappedSuperClass](#mappedsuperclass)
- [Single Table](#single-table)
- [Joined Table](#joined-table)
- [Table Per Class](#table-per-class)
- [Выбор стратегии сохранения иерархии](#выбор-стратегии-сохранения-иерархии)
- [EXPLICIT](#explicit)
## Структура классов
```java
class User {
    int id;
    String name;
    LocalDate birthday;
}

class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

class Client extends User {
    String address;
}
```
## Виды решений
В Hibernate существует 4 возможных способа, которыми он может связать иерархию классов с таблицами в базе данных:
- MappedSuperclass
- Single Table
- Joined Table
- Table per class

Каждая стратегия предполагает свою собственную структуру таблиц в базе данных. Иногда они довольно сложные. Зато 
запросы на HQL к ним очень простые. Это как раз тот случай, где ярко проявляются преимущества Hibernate.
## @MappedSuperClass
Начнем с самого простого решения — в базе данных у тебя отдельные таблицы для каждого класса. Например, такие:
```sql
CREATE TABLE user {
    id INT,
    name VARCHAR,
    birthday DATE
};

CREATE TABLE employee {
    id INT,
    name VARCHAR,
    birthday DATE,
    occupation VARCHAR,
    salary INT,
    join DATE
};

CREATE TABLE client {
    id INT,
    name VARCHAR,
    birthday DATE,
    address VARCHAR
};
```
О том, что классы для этих таблиц связаны в иерархию, знаешь только ты. Если ты хочешь, чтобы еще и Hibernate об этом 
знал, тебе нужно добавить родительскому классу аннотацию @MappedSuperclass. Без нее Hibernate просто проигнорирует поля 
и аннотации родительского класса.

Классы с этой аннотацией будут выглядеть так:
```java
@MappedSuperclass
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
class Client extends User {
    String address;
}
```
Это самый примитивный способ связывания иерархии классов и базы данных. Такой подход фактически позволяет тебе только 
избежать дубликата полей в классах.

Запросы к базе данных на HQL будут возвращать только ту сущность, тип которой указан явно. Ты не можешь написать запрос 
к базе на HQL и получить список всех пользователей: User, Employee, Client.
## Single table
Следующий подход к хранению иерархии классов – это хранить все классы иерархии в одной таблице. Такая стратегия
называется Single Table.

Например, так:
```sql
CREATE TABLE user_ employee_client {
    id INT,
    name VARCHAR,
    birthday DATE,
    occupation VARCHAR,
    salary INT,
    join DATE,
    address VARCHAR,
    DTYPE VARCHAR
};
```
То есть у нас есть одна таблица, у которой колонки для всех классов нашей иерархии обозначены разными цветами. Также 
есть специальная служебная колонка DTYPE VARCHAR, где Hibernate будет хранить имя Entity-класса.

Дело осталось за малым – объяснить Hibernate, что данные Entity-классов теперь хранятся в базе в одной таблице. Сделать 
это можно с помощью аннотации @Inheritance:
```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
class Client extends User {
    String address;
}
```
При сохранении данных в таблицу Hibernate передает только известные ему поля сущностей. Это означает, что неуказанные
колонки будут иметь значение NULL.

А это значит, что ты не можешь указать для колонки occupation тип NOT NULL, так как при хранении клиента в этой же 
таблице его occupation будет NULL. Это один из минусов хранения разных сущностей в одной таблице.

Последнее поле в SQL-запросе – это колонка DTYPE, в ней передается имя Entity-класса. Он используется Hibernate, когда
ты хочешь прочитать данные из своей таблицы.

Пример:
```java
List<User> accounts = session.createQuery("from User").list();
```
Данный запрос вернет список всех сохраненных в базе объектов типа пользователь: User, Employee и Client. На основе 
колонки DTYPE будет правильно определен тип сущности и создан объект правильного класса.

В нашем случае в списке accounts будут два объекта: типа Employee и типа Client.
### Дискриминатор (Single Table)
В предыдущей лекции ты видел, что Hibernate использует специальную колонку DTYPE VARCHAR для хранения имени
Entity-класса. Такая колонка называется дискриминатор. Ее используют для того, чтобы однозначно определить какой класс
нужно создать для данной строки базы данных.

Ты можешь управлять этой колонкой с помощью аннотации @DiscriminatorColumn.

Согласно спецификации JPA дискриминатор может иметь такие типы:
- STRING
- CHAR
- INTEGER

Однако Hibernate позволяет немного расширить этот список. Он поддерживает такие Java-типы: String, char, int, byte, 
short, boolean.

Если мы используем тип INTEGER, то как в нем кодировать имя Entity-класса? Для этого используется еще одна аннотация – 
@DiscriminatorValue.

Посмотри на пример:
```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",   discriminatorType = DiscriminatorType.INTEGER)
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
@DiscriminatorValue("1")
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
@DiscriminatorValue("2")
class Client extends User {
    String address;
}
```
В примере выше мы сказали Hibernate, что для дискриминатора будет использоваться колонка user_type, которая будет
хранить числа. Если в ней хранится значение 1, то это значит, что тип строки – Employee, если хранится 2, то тип 
строки – Client. Просто и красиво.
#### DiscriminatorValue
Но и это еще не все. Ты можешь указать Hibernate, как интерпретировать тип строки, когда ее дискриминатор равен NULL.

На самом деле это очень просто. Ты указываешь значение null у аннотации @DiscriminatorValue. Например, так:
```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",   discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("null")
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}
```
Мы указали Hibernate, что любая строка таблицы, где в колонке user_type стоит NULL, должна быть интерпретирована как 
объект типа User.

Но и это еще не все. Есть еще одно интересное значение у аннотации @DiscriminatorValue: "not null"

Пример класса:
```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",   discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("not null")
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}
```
С помощью этой аннотации мы указали Hibernate, что любая строка таблицы, где в колонке user_type стоит значение не NULL, 
должна быть интерпретирована как объект типа User. Но это только для случая, если не будет найден класс, у которого 
явно указан нужный номер.

Так работать это будет для разных значений дискриминаторов:
- 0 – создаем объект типа User
- 1 – создаем объект типа Employee
- 2 – создаем объект типа Client
- 3 – создаем объект типа User
- 4 – создаем объект типа User
#### DiscriminatorFormula
Но и это еще не все. Для нашего дискриминатора мы можем указать целую формулу, по которой он будет вычислять значения 
для аннотации @DiscriminatorValue.

Для этого есть специальная аннотация, она так и называется @DiscriminatorFormula.

Пример:
```java
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",   discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorFormula("case when ‘join’ is not null then 1 else 2 end")
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
@DiscriminatorValue("1")
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
@DiscriminatorValue("2")
class Client extends User {
    String address;
}
```
Значения, которые возвращает @DiscriminatorFormula, будут сравниваться Hibernate со значениями, указанными в аннотациях 
@DiscriminatorValue. С ее помощью можно записывать довольно сложные сценарии:
```java
@DiscriminatorFormula(
           	"case when address is not null " +
           	"then 'Client' " +
           	"else (" +
           	"   case when occupation is not null " +
           	"   then 'Employee' " +
           	"   else 'Unknown' " +
           	"   end) " +
           	"end "
)
```
## Joined Table
Еще одна стратегия хранения иерархии классов в базе данных называется Joined Table. Для нее есть специальный тип. Пример
наших классов:
```java
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
class Client extends User {
    String address;
}
```
При использовании этой аннотации Hibernate будет ожидать в базе отдельную таблицу для каждого класса и его подклассов. 
При выборке данных из них придется использовать SQL-оператор JOIN.

Пример схемы базы данных:
```sql
CREATE TABLE user {
    id INT,
    name VARCHAR,
    birthday DATE
};

CREATE TABLE employee {
    id INT,
    occupation VARCHAR,
    salary INT,
    join DATE
};

CREATE TABLE client {
    id INT,
    address VARCHAR
};
```
Если ты решишь получить из таблицы данные какого-нибудь клиента, то Hibernate придется использовать JOIN для 
объединения таблиц:
```hql
SELECT u.id, u.name, u.birthday, c.address FROM user u JOIN client c ON u.id = c.id;
```
#### @PrimaryKeyJoinColumn
Дочерние Entity-классы имеют колонку в таблице, которая ссылается на id объекта родительского Entity-класса. Имя этой 
колонки по умолчанию равно имени колонки родительского класса.

Пример:
```java
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
class User {
    @Id
    int user_identifier;
    String name;
    LocalDate birthday;
}

@Entity
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
class Client extends User {
    String address;
}
```
Тогда в базе таблицы будут выглядеть так:
```sql
CREATE TABLE user {
    user_identifier INT,
    name VARCHAR,
    birthday DATE
};

CREATE TABLE employee {
    user_identifier INT,
    occupation VARCHAR,
    salary INT,
    join DATE
};

CREATE TABLE client {
    user_identifier INT,
    address VARCHAR
};
```
Если ты хочешь переопределить имя колонки в зависимых таблицах, то тебе нужно использовать аннотацию 
@PrimaryKeyJoinColumn. Пример:
```java
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
class User {
    @Id
    int user_identifier;
    String name;
    LocalDate birthday;
}

@Entity
@PrimaryKeyJoinColumn(name="user_id")
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
@PrimaryKeyJoinColumn(name="user_id2")
class Client extends User {
    String address;
}
```
Тогда в базе таблицы будут выглядеть так:
```sql
CREATE TABLE user {
    user_identifier INT,
    name VARCHAR,
    birthday DATE
};

CREATE TABLE employee {
    user_id INT,
    occupation VARCHAR,
    salary INT,
    join DATE
};

CREATE TABLE client {
    user_id2 INT,
    address VARCHAR
};
```
## Table Per Class
И наконец-то последняя стратегия – это Table per class. Она означает, что для каждого класса будет использоваться
отдельная таблица. В каком-то смысле – это тот же MappedSuperClass, только в обновленном виде.

Пример:
```java
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
class User {
	int id;
	String name;
	LocalDate birthday;
}

@Entity
class Employee extends User {
 	String occupation;
 	int salary;
 	LocalDate join;
}

@Entity
class Client extends User {
	String address;
}
```
И отдельные таблицы для каждого класса. Например, такие:
```sql
CREATE TABLE user {
	id INT,
	name VARCHAR,
	birthday DATE
};

CREATE TABLE employee {
	id INT,
	name VARCHAR,
	birthday DATE,
	occupation VARCHAR,
	salary INT,
	join DATE
};

CREATE TABLE client {
	id INT,
	name VARCHAR,
	birthday DATE,
	address VARCHAR
};
```
Основное отличие – это то, что используется сквозной id (PRIMARY KEY) для всех таблиц. У тебя не могут быть разные 
строки с одним id не только в рамках одной таблицы, но и в рамках этой группы таблиц. Hibernate будет следить за этим.
#### Примеры
Очень интересно разобрать, как это все работает.

Ты можешь написать простой HQL-запрос, чтобы получить всех пользователей: User, Employee, Client:
```java
List<User> accounts = session.createQuery("from User").list();
```
А вот Hibernate, в свою очередь, сгенерирует очень интересный запрос. Он сделает выборку из всех таблиц, потом 
объединит ее через UNION ALL в подобие виртуальной таблицы, и только потом будет выполнять по ней поиск и/или выборку.

Но чтобы объединить таблицы с разными колонками, сначала их нужно дополнить фейковыми колонками. Например, таблицу user
нужно дополнить колонками:
- occupation VARCHAR
- salary INT
- join DATE
- address VARCHAR

Пример SQL-запроса к таблице user перед выполнением UNION ALL:
```sql
SELECT   id,
         name,
         birthday,
         CAST(NULL AS VARCHAR) AS occupation,
         CAST(NULL AS INT) AS salary,
         CAST(NULL AS DATE) AS join,
         CAST(NULL AS VARCHAR) AS address,
         0 AS clazz
FROM  user
```
Пример SQL-запроса к таблице employee перед выполнением UNION ALL:
```sql
SELECT   id,
         name,
         birthday,
         occupation,
         salary,
         join,
         CAST(NULL AS VARCHAR) AS address,
         1 AS clazz
FROM  employee
```
Пример SQL-запроса к таблице client перед выполнением UNION ALL:
```sql
SELECT  id,
        name,
        birthday,
        CAST(NULL AS VARCHAR) AS occupation,
        CAST(NULL AS INT) AS salary,
        CAST(NULL AS DATE) AS join,
        address,
        2 AS clazz
FROM client
```
Хорошая новость: HQL-запросы будут работать так, как тебе бы и хотелось.

Плохая новость: они могут работать медленно, если данных в таблицах очень много. Потому что сначала данные нужно 
выбрать из всех таблиц, потом объединить их строки с помощью UNION ALL, и только потом фильтровать.
## Выбор стратегии сохранения иерархии
Каждая из перечисленных выше стратегий и приемов имеет свои преимущества и недостатки. Общие рекомендации по выбору 
конкретной стратегии будут выглядеть так:
- Стратегия TABLE_PER_CLASS на основе UNION - Данную стратегию лучше выбирать, если полиморфные запросы и ассоциации не
требуются. Если ты редко выполняешь (или не выполняешь вообще) “select user from User user”. Если у тебя нет 
Entity-классов, ссылающихся на User, этот вариант будет лучшим (поскольку возможность добавления оптимизированных 
полиморфных запросов и ассоциаций сохранится).
- Стратегия SINGLE_TABLE - Данную стратегию стоит использовать:
  - Только для простых задач. В ситуациях, когда нормализация и ограничение NOT NULL являются критическими, следует 
отдать предпочтение стратегии №3 (JOINED). Имеет смысл задуматься, не стоит ли в данном случае полностью отказаться от
наследования и заменить его делегированием. 
  - Если требуются полиморфные запросы и ассоциации, а также динамическое определение конкретного класса во время
выполнения. При этом подклассы объявляют относительно мало новых полей, и основная разница с суперклассом заключается
в поведении.
- Стратегия JOINED - Данная стратегия самая эффективная по скорости и CONSTRAINTS. Она подойдет в случаях, когда
требуются полиморфные запросы и ассоциации, но подклассы объявляют относительно много новых полей. Здесь стоит
оговориться: решение между JOINED и TABLE_PER_CLASS требует оценки планов выполнения запросов на реальных данных, 
поскольку ширина и глубина иерархии наследования могут сделать стоимость соединений (и, как следствие, 
производительность) неприемлемыми. Отдельно стоит принять во внимание, что аннотации наследования невозможно применить 
к интерфейсам.
## EXPLICIT
Еще может быть ситуация, когда у тебя есть иерархия Entity-классов с совместной стратегией хранения в базе банных. Но 
по каким-либо причинам ты не хочешь, чтобы некий класс иерархии возвращался, когда делается запрос по базовому классу.

Пример:
```java
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
class User {
    int id;
    String name;
    LocalDate birthday;
}

@Entity
class Employee extends User {
    String occupation;
    int salary;
    LocalDate join;
}

@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
class Client extends User {
    String address;
}
```
HQL-запросы будут игнорировать объекты этого класса при запросе базового класса:
```java
List<User> accounts = session.createQuery("from User").getResultList();
```
Данный запрос вернет список объектов User и Employee, но не Client.
