# Базовые функциональные интерфейсы Java 8
## Оглавление
- [Predicate](#predicate)
- [Consumer](#consumer)
- [BiConsumer](#biconsumer)
- [Supplier](#supplier)
- [Function](#function)
- [UnaryOperator](#unaryoperator)
- [BinaryOperator](#binaryoperator)
- [Функциональные интерфейсы в Stream](#функциональные-интерфейсы-в-stream)
## Predicate
Функциональный интерфейс проверки соблюдения некоторого условия. Возвращает __true__ или __false__:
```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}

class Main {
    public static void main(String[] args) {
        Predicate<Integer> isEvenNumber = x -> x % 2 == 0;
        System.out.println(isEvenNumber.test(4));  // true
        System.out.println(isEvenNumber.test(3));  // false
    }
}
```
## Consumer
Функциональный интерфейс, который принимает в кач-ве входного параметра объект типа Т, совершает какие-то действия, но ничего не возвращая:
```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

class Main {
    public static void main(String[] args) {
        Consumer<String> greetings = x -> System.out.println("Hello " + x + "!!!");
        greetings.accept("Elena");  // Hello Elena!!!
    }
}
```
## BiConsumer
Функциональный интерфейс, принимающий 2 типа Т и U, проводит какую-то операцию и не возвращает ничего:

```java
import java.util.Arrays;

@FunctionalInterface
public interface BiConsumer<T, U> {
    void accept(T t, U u);
}

class Main {
    public static void main(String[] args) {
        BiConsumer<Integer, Integer> consumer = (x, y) -> System.out.println(x + y);
        consumer.accept(1, 2);  // 3
    }
}
```
## Supplier
Функциональный интерфейс, который не принимает никаких аргументов, но возвращает некоторый объект типа Т:

```java
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}

class Main {
    public static void main(String[] args) {
        List<String> nameList = new ArrayList<>();
        nameList.add("Elena");
        nameList.add("John");
        nameList.add("Alex");
        nameList.add("Jim");
        nameList.add("Sara");
        
        Supplier<String> randomName = () -> nameList.get(Math.random() * nameList.size());
        System.out.println(randomName);  // Jim
    }
}
```
## Function
Функциональный интерфейс принимает аргумент Т и приводит его к объекту типа R, который возвращается как результат:
```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}

class Main {
    public static void main(String[] args) {
        Function<String, Integer> valueConverter = Integer::valueOf;
        System.out.println(valueConverter.apply("678"));  // 678
    }
}
```
## UnaryOperator
Функциональный интерфейс, который принимает в качестве аргумента тип Т, выполняет над ним некоторые операции и возвращает результат операции в виде объекта того же типа Т:
```java
@FunctionalInterface
public interface UnaryOperator<T> {
    T apply(T t);
}

class Main {
    public static void main(String[] args) {
        UnaryOperator<Integer> square = x -> x * x;
        System.out.println(square.apply(9));  // 81
    }
}
```
## BinaryOperator
Функциональный интерфейс, принимает в кач-ве параметра два объекта типа Т, проводит бинарную операцию и возвращает результат того же типа:
```java
@FunctionalInterface
public interface BinaryOperator<T> {
    T apply(T t1, T t2);
}

class Main {
    public static void main(String[] args) {
        BinaryOperator<Integer> multiply = (x, y) -> x * y;
        System.out.println(multiply.apply(3, 5));  // 15
        System.out.println(multiply.apply(10, -2));  // -20
    }
}
```
# Функциональные интерфейсы в Stream
filter, allMatch/anyMatch/noneMatch, dropWhile/takeWhile -> Predicate

peek -> Consumer

generate -> Supplier

map, flatMap -> Function

iterate -> UnaryOperator
