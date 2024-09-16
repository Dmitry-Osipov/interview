# Топ вопросов по Java
## Оглавление
- [Что такое JDK, JRE, JVM](#что-такое-jdk-jre-jvm)
- [Объясните механизм Garbage Collection](#объясните-механизм-garbage-collection)
- [Что такое Stack и Heap в контексте памяти?](#что-такое-stack-и-heap-в-контексте-памяти)
- [Объясните разницу между checked и unchecked исключениями](#объясните-разницу-между-checked-и-unchecked-исключениями)
- [Что такое Immutable объекты?](#что-такое-immutable-объекты)
- [Как работает ключевое слово final](#как-работает-ключевое-слово-final)
- [Что такое static?](#что-такое-static)
- [Что такое полиморфизм?](#что-такое-полиморфизм)
- [Что такое абстрактные классы и интерфейсы?](#что-такое-абстрактные-классы-и-интерфейсы)
- [В чём разница между equals() и ==?](#в-чём-разница-между-equals-и-)
- [Что такое сборщик мусора и как его можно настраивать?](#что-такое-сборщик-мусора-и-как-его-можно-настраивать)
- [Объясните ключевое слово volatile](#объясните-ключевое-слово-volatile)
- [Что такое Race Condition и как его избежать?](#что-такое-race-condition-и-как-его-избежать)
- [Как работает механизм блокировки с помощью synchronized?](#как-работает-механизм-блокировки-с-помощью-synchronized)
- [Что такое Deadlock и как его избежать?](#что-такое-deadlock-и-как-его-избежать)
- [Объясните различия между ArrayList и LinkedList](#объясните-различия-между-arraylist-и-linkedlist)
- [Что такое HashMap и как она работает?](#что-такое-hashmap-и-как-она-работает)
- [Объясните Comparable и Comparator](#объясните-comparable-и-comparator)
- [Что такое дженерики и для чего они используются?](#что-такое-дженерики-и-для-чего-они-используются)
- [Как работает механизм рефлексии?](#как-работает-механизм-рефлексии)
- [Что такое Optional?](#что-такое-optional)
- [Объясните лямбда-выражения](#объясните-лямбда-выражения)
- [Что такое Stream API?](#что-такое-stream-api)
- [Как работает метод filter() в Stream API?](#как-работает-метод-filter-в-stream-api)
- [Объясните Collectors в Stream API](#объясните-collectors-в-stream-api)
- [Что такое Method Reference?](#что-такое-method-reference)
- [Объясните Default Methods в интерфейсах](#объясните-default-methods-в-интерфейсах)
- [Что такое Fork/Join Framework?](#что-такое-forkjoin-framework)
- [Как работает try-with-resources?](#как-работает-try-with-resources)
- [Что такое Concurrency API?](#что-такое-concurrency-api)
- [Как работает Future и CompletableFuture?](#как-работает-future-и-completablefuture)
- [Что такое Immutable коллекции?](#что-такое-immutable-коллекции)
- [Что такое модульная система](#что-такое-модульная-система)
- [Что такое LocalDate, LocalTime и LocalDateTime?](#что-такое-localdate-localtime-и-localdatetime)
- [Как работает var?](#как-работает-var)
- [Что такое Switch-выражения?](#что-такое-switch-выражения)
- [Что такое Pattern Matching для instanceof?](#что-такое-pattern-matching-для-instanceof)
- [Как работают Sealed Classes?](#как-работают-sealed-classes)
- [Что такое Proxy Pattern?](#что-такое-proxy-pattern)
- [Как работает Stream API?](#как-работает-stream-api)
- [Что такое WeakReference?](#что-такое-weakreference)
- [Какой принцип работы коллекции TreeMap?](#какой-принцип-работы-коллекции-treemap)
- [Как работает CopyOnWriteArrayList?](#как-работает-copyonwritearraylist)
- [Что такое AtomicInteger?](#что-такое-atomicinteger)
- [Что такое ReentrantLock и как он работает?](#что-такое-reentrantlock-и-как-он-работает)
## Что такое JDK, JRE, JVM?
- JDK (Java Development Kit) — это набор инструментов для разработки Java-приложений. Включает компилятор (javac), 
библиотеки, документацию и другие инструменты для разработки.
- JRE (Java Runtime Environment) — это среда выполнения Java-программ, включающая JVM (Java Virtual Machine) и 
библиотеки для выполнения байт-кода.
- JVM (Java Virtual Machine) — это виртуальная машина, которая исполняет скомпилированный байт-код Java. JVM 
обеспечивает переносимость кода между разными операционными системами.
## Объясните механизм Garbage Collection
Garbage Collection — это процесс автоматической очистки памяти от объектов, которые больше не используются. JVM 
отслеживает объекты, до которых больше нельзя достучаться в программе, и освобождает память, которую они занимают.

[Подробнее про GC](../core/memory/garbage_collector.md)
## Что такое Stack и Heap в контексте памяти?
- Stack используется для хранения примитивных типов данных и ссылок на объекты. Каждый вызов метода создает новый блок
в стеке.
- Heap — это область памяти, где хранятся все объекты. Каждый раз, когда создается объект с помощью new, он попадает
в heap.

[Подробнее про Memory Model](../core/memory/java_process_memory_model.md)
## Объясните разницу между checked и unchecked исключениями
- Checked Exception — это исключения, которые проверяются во время компиляции. Программист обязан обработать их с 
помощью try-catch или указать с помощью throws. Пример: IOException.
- Unchecked Exception — это исключения времени выполнения (Runtime), которые не проверяются во время компиляции.
Например: NullPointerException, ArrayIndexOutOfBoundsException.

[Подробнее про exception](../core/collection_and_exception/exception_ierarchy.md)
## Что такое Immutable объекты?
Immutable объекты — это объекты, состояние которых не может быть изменено после создания. Пример: класс String 
является неизменяемым. Это означает, что при изменении строки создается новый объект, а не изменяется существующий.
## Как работает ключевое слово final
Ключевое слово final используется для:
- Переменных: значение переменной не может быть изменено после инициализации.
- Методов: метод не может быть переопределен в подклассах.
- Классов: класс не может быть наследован.
## Что такое static?
Ключевое слово static используется для указания, что переменная, метод или блок принадлежит классу, а не 
экземпляру объекта. Например:
- Static переменная: разделяется между всеми экземплярами класса.
- Static метод: может быть вызван без создания объекта.
## Что такое полиморфизм?
Полиморфизм позволяет объектам одного типа принимать множество форм. В Java это может проявляться в виде:
- Перегрузки методов (Compile-time polymorphism): один и тот же метод может иметь несколько сигнатур.
- Переопределение методов (Run-time polymorphism): подкласс может переопределять методы своего родительского класса.
## Что такое абстрактные классы и интерфейсы?
- Абстрактный класс — это класс, который может содержать абстрактные методы (без реализации) и реализованные методы. 
Его нельзя создать напрямую. 
- Интерфейс — это контракт, который класс должен выполнить. Все методы интерфейса являются абстрактными (до Java 8)
или могут иметь реализацию по умолчанию (Java 8 и выше).

[Подробнее про функциональные интерфейсы](../core/functional/base_functional_interfaces.md)
## В чём разница между equals() и ==?
- ==: используется для сравнения ссылок на объекты (или для примитивных типов, где сравниваются значения).
- equals(): используется для сравнения значений объектов (например, строки, числа).
## Что такое сборщик мусора и как его можно настраивать?
Сборщик мусора (Garbage Collector, GC) отвечает за автоматическое управление памятью. Можно настраивать GC с помощью
флагов JVM:
- -XX:+UseG1GC: использование сборщика G1.
- -XX:+UseParallelGC: использование многопоточного сборщика.

Подробнее про [сборку мусора](../core/memory/garbage_collector.md) и [Memory Model](../core/memory/java_process_memory_model.md)
## Объясните ключевое слово volatile
Ключевое слово volatile используется для указания, что переменная может быть изменена несколькими потоками. JVM 
обеспечивает, что изменения в переменной сразу видны всем потокам, а не только локальному кэшу.
[Подробнее про volatile](../core/concurrency/thread_api_and_shared_state.md)
## Что такое Race Condition и как его избежать?
Race Condition возникает, когда несколько потоков пытаются одновременно изменить общие ресурсы. Для решения можно
использовать:
- Ключевое слово synchronized для блокировки ресурса.
- Lock API из библиотеки java.util.concurrent.

[Подробнее про condition](../core/concurrency/condition.md)
## Как работает механизм блокировки с помощью synchronized?
Ключевое слово synchronized гарантирует, что только один поток может выполнять блок кода или метод одновременно. Можно 
синхронизировать метод или блок кода:
```java
synchronized (this) {  // Лок может быть на кондишене текущего объекта (this) или какого-то объекта (Object obj = new Object)
    // критическая секция
}
```

[Подробнее про доступ к общим ресурсам](../core/concurrency/thread_api_and_shared_state.md)
## Что такое Deadlock и как его избежать?
Deadlock возникает, когда два или более потоков ожидают друг друга для освобождения ресурсов, что приводит к 
бесконечному ожиданию. Чтобы избежать, следует:
- Избегать вложенной блокировки.
- Использовать таймауты на блокировках.

[Подробнее про deadlock](../core/concurrency/deadlocks.md)
## Объясните различия между ArrayList и LinkedList
- ArrayList: Быстрая произвольная выборка элементов, так как элементы хранятся в массиве. Однако добавление/удаление
элементов в середине может быть медленным.
- LinkedList: Каждый элемент связан с предыдущим и следующим элементом. Быстрое добавление/удаление, но медленная
произвольная выборка.

[Подробнее про коллекции](../core/collection_and_exception/collection_ierarchy.md)
## Что такое HashMap и как она работает?
HashMap — это структура данных, которая хранит данные в виде ключ-значение. Для вычисления индекса используется 
хэш-функция. В случае коллизий (когда несколько ключей попадают в одну корзину) используется связанный список 
или дерево. [Подробнее про коллекции](../core/collection_and_exception/collection_ierarchy.md)
## Объясните Comparable и Comparator
- Comparable: интерфейс, который позволяет объектам сравнивать себя с другими объектами. Требует реализации метода
compareTo().
- Comparator: интерфейс для внешнего сравнения объектов, позволяющий определить несколько способов сортировки с помощью
метода compare().
## Что такое дженерики и для чего они используются?
Generics позволяют создавать классы, интерфейсы и методы, которые могут работать с любым типом данных, обеспечивая
безопасность типов на этапе компиляции.
## Как работает механизм рефлексии?
Рефлексия позволяет программам динамически анализировать и изменять свое поведение во время выполнения. Она 
используется для доступа к полям, методам и конструкторам классов в рантайме:
```java
Class<?> clazz = Class.forName("com.example.MyClass");
Method method = clazz.getDeclaredMethod("myMethod");
method.invoke(instance);
```
## Что такое Optional?
Optional — это контейнер для значений, который помогает избежать NullPointerException. Он используется для работы с
потенциально отсутствующими значениями. Пример:
```java
Optional<String> name = Optional.ofNullable(getName());
name.ifPresent(System.out::println);
```
## Объясните лямбда-выражения
Лямбда-выражения — это способ создания анонимных функций. Они облегчают написание функциональных интерфейсов, таких как 
Runnable или Comparator. Пример:
```java
List<Integer> numbers = Arrays.asList(1, 2, 3);
numbers.forEach(n -> System.out.println(n));
```
## Что такое Stream API?
Stream API позволяет работать с последовательностями данных с использованием функциональных операций, таких как
фильтрация, маппинг и редукция. Пример:
```java
List<String> names = Arrays.asList("John", "Jane", "Jake");
names.stream()
     .filter(name -> name.startsWith("Ja"))
     .forEach(System.out::println);
```
## Как работает метод filter() в Stream API?
Метод filter() принимает предикат (логическое условие) и возвращает новый поток, содержащий только те элементы, 
которые удовлетворяют этому условию:
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream()
       .filter(n -> n % 2 == 0)
       .forEach(System.out::println);
```

[Подробнее про функциональные интерфейсы](../core/functional/base_functional_interfaces.md)
## Объясните Collectors в Stream API
Collectors предоставляют набор методов для сборки элементов потока в разные структуры данных, такие как list, set, map.
Пример:
```java
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());
```
## Что такое Method Reference?
Method Reference — это сокращенная форма записи для вызова метода, которая позволяет ссылаться на методы или
конструкторы. Пример:
```java
List<String> names = Arrays.asList("Alice", "Bob");
names.forEach(System.out::println);
```
[Подробнее про типы ссылок](../core/memory/java_process_memory_model.md)
## Объясните Default Methods в интерфейсах
В Java 8 были добавлены Default Methods в интерфейсы. Они позволяют добавлять реализацию методов прямо в интерфейс без
нарушения совместимости с существующими реализациями.
## Что такое Fork/Join Framework?
Fork/Join Framework — это механизм для параллельного выполнения задач, который делит задачу на более мелкие подзадачи
(fork) и затем объединяет результаты (join). Пример:
```java
ForkJoinPool pool = new ForkJoinPool();
pool.invoke(new MyRecursiveTask());
```
[Подробнее про ForkJoinPool](../core/concurrency/for_join_pool_with_baeldung.md)
## Как работает try-with-resources?
Try-with-resources — это улучшенный способ работы с ресурсами, которые нужно закрывать после использования. Пример:
```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
```
## Что такое Concurrency API?
Concurrency API предоставляет классы и интерфейсы для работы с многопоточными программами. Например, ExecutorService, 
Future, Semaphore. Пример:
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
executor.submit(() -> System.out.println("Task executed"));
```
## Как работает Future и CompletableFuture?
- [Future](../core/concurrency/future_with_baeldung.md) используется для асинхронного получения результатов выполнения
задач. Пример:
```java
Future<String> result = executor.submit(() -> "Task completed");
```
- [CompletableFuture](../core/concurrency/completable_future_with_baeldung.md) расширяет Future, позволяя использовать
цепочки обработки задач:
```java
CompletableFuture.supplyAsync(() -> "Task")
                 .thenApply(result -> result + " completed")
                 .thenAccept(System.out::println);
```
## Что такое Immutable коллекции?
С появлением Java 9 появилась возможность создавать неизменяемые коллекции с помощью фабричных методов:
```java
List<String> list = List.of("one", "two", "three");
Set<String> set = Set.of("apple", "banana");
```
## Что такое модульная система
В Java 9 была введена модульная система, которая позволяет разбивать программы на модули для лучшей организации кода 
и контроля зависимости. Пример:
```
module com.example.myapp {
    requires java.base;
    exports com.example.myapp;
}
```
## Что такое LocalDate, LocalTime и LocalDateTime?
Эти классы предоставляют новый API для работы с датами и временем в Java 8:
- LocalDate: представляет дату без времени (например, 2023-09-16).
- LocalTime: представляет время без даты (например, 12:30).
- LocalDateTime: представляет дату и время вместе.
## Как работает var?
Ключевое слово var позволяет JVM автоматически определять тип переменной на основе присвоенного значения. Пример:
```java
var name = "John Doe"; // автоматически определит тип String
```
## Что такое Switch-выражения?
Switch-выражения в Java 12 позволяют возвращать значения и использовать лямбда-стиль:
```java
String result = switch (day) {
    case MONDAY -> "Start of the week";
    case FRIDAY -> "End of the week";
    default -> "Middle of the week";
};
```
## Что такое Pattern Matching для instanceof?
Pattern Matching для instanceof упрощает приведение типов:
```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```
## Как работают Sealed Classes?
Sealed Classes позволяют ограничить, какие классы могут наследоваться от родительского класса. Пример:
```java
public sealed class Shape permits Circle, Square {}
```
## Что такое Proxy Pattern?
Proxy Pattern — это шаблон проектирования, который используется для создания суррогата или замещающего объекта для
контроля доступа к другому объекту. Пример:
```java
InvocationHandler handler = new MyInvocationHandler(new RealObject());
MyInterface proxy = (MyInterface) Proxy.newProxyInstance(
    RealObject.class.getClassLoader(),
    RealObject.class.getInterfaces(),
    handler);
```
## Как работает Stream API?
Stream API используется для обработки коллекций и массивов в функциональном стиле. Пример:
```java
List<String> names = List.of("John", "Jane", "Jack");
names.stream()
    .filter(name -> name.startsWith("J"))
    .map(String::toUpperCase)
    .forEach(System.out::println);
```
[Подробнее про функциональные интерфейсы](../core/functional/base_functional_interfaces.md)
## Что такое WeakReference?
WeakReference — это тип ссылки, который позволяет объекту быть удаленным сборщиком мусора, если на него больше нет 
сильных ссылок. Пример:
```java
WeakReference<MyObject> weakRef = new WeakReference<>(new MyObject());
```
[Подробнее про типы ссылок](../core/memory/java_process_memory_model.md)
## Какой принцип работы коллекции TreeMap?
TreeMap хранит ключи в отсортированном порядке, основываясь на их естественном порядке или на переданном компараторе.
Внутренне он использует красно-черное дерево для обеспечения логарифмического времени доступа.
## Как работает CopyOnWriteArrayList?
CopyOnWriteArrayList — это потокобезопасная коллекция, которая создает копию внутреннего массива при каждой операции
изменения, что делает ее эффективной для сценариев, где чтения больше, чем записей. 
[Подробнее про потокобезопасные коллекции](../core/concurrency/collections_and_atomics.md)
## Что такое AtomicInteger?
AtomicInteger предоставляет атомарные операции над переменными целого типа без необходимости использования блокировок.
Пример:
```java
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();
```
[Подробнее про атомарность](../core/concurrency/collections_and_atomics.md)
## Что такое ReentrantLock и как он работает?
ReentrantLock — это расширенная версия synchronized, предоставляющая более гибкое управление блокировкой. Пример:
```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // критическая секция
} finally {
    lock.unlock();
}
```
[Подробнее про локи](../core/concurrency/lock_with_baeldung.md)
