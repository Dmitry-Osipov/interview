# Основные изменения в Java по версиям
## Оглавление
- [Java 8](#java-8)
- [java 9](#java-9)
- [Java 10](#java-10)
- [Java 11](#java-11)
- [Java 12](#java-12)
- [Java 13](#java-13)
- [Java 14](#java-14)
- [Java 15](#java-15)
- [Java 16](#java-16)
- [Java 17](#java-17)
- [Java 18](#java-18)
- [Java 19](#java-19)
- [Java 21](#java-21)
## Java 8
- Stream API
- Лямбды, которые можно подставить вместо функционального интерфейса
- Методы по умолчанию для интерфейсов
## Java 9
- Вспомогательные методы для коллекций (например, List.of)
- Дополнение методов у Stream API в виде методов takeWhile, dropWhile и iterate
- Optional получили метод ifPresentOrElse(Consumer, Runnable)
- Интерфейсы получили private методы
- Появилась оболочка jshell
## Java 10
- Локальная переменная методов - var
## Java 11
- Строки и файлы получили новые методы 
- Запуск исходных файлов Java без предварительной их компиляции
- var может использоваться для лямбда-параметров
## Java 12
- Поддержка Unicode 11
- Предварительный просмотр нового выражения switch
## Java 13
- Поддержка Unicode 12.1
- switch-выражение теперь может возвращать значение
```java
// Было:
switch(status) {
  case SUBSCRIBER:
    // code block
    break;
  case FREE_TRIAL:
    // code block
    break;
  default:
    // code block
}

// Стало:
boolean result = switch (status) {
    case SUBSCRIBER -> true;
    case FREE_TRIAL -> false;
    default -> throw new IllegalArgumentException("something is murky!");
};
```
- Предварительная версия многострочных строк
## Java 14
- switch-выражения, которые были превью функцией в версиях 12 и 13, теперь стандартизированы:
```java
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY -> 7;
    default -> {
      String s = day.toString();
      int result = s.length();
      yield result;
    }
};
```
- Появились record, которые позволяют убрать getters, AllArgsConstructor, equals, hashCode, toString (т.е. за нас всё 
автоматически реализовано)
- NullPointerException теперь описывает, какая именно переменная имела значение null
- Предварительная версия сопоставления с образцом для instanceOf:
```java
// Было:
if (obj instanceof String) {
    String s = (String) obj;
// использование s
}

// Стало:
if (obj instanceof String s) {
    System.out.println(s.contains("hello"));
}
```
- Инструмент упаковки (инкубатор), позволяющий упаковать java-приложение в пакеты для конкретной платформы, включая 
необходимые зависимости: Linux - deb и rpm; macOS - pkg и dmg; Windows - msi и exe.
- Был удалён Concurrent Mark Sweep (CMS) сборщик мусора и добавлен экспериментальный Z
## Java 15
- Текстовые блоки как превью из Java 13 теперь полностью готовы к использованию
- Превью sealed-permits классы - указываем класс как sealed и после permits указываем, кто может наследовать класс:
```java
public abstract sealed class Shape
    permits Circle, Rectangle, Square {...}
```
- ZGC теперь готов к использованию полностью
## Java 16
- Закончили работу над сопоставлением (instanceOf) со времён Java 14
- Закончили работу на record со времён Java 14
## Java 17
- Сопоставление шаблонов для switch (предварительная версия)
```java
public String test(Object obj) {
    return switch(obj) {
    case Integer i -> "An integer";
    case String s -> "A string";
    case Cat c -> "A Cat";
    default -> "I don't know what it is";
    };
}
```
- Запечатанные классы (sealed-permits) завершены со времён Java 15
- Замена нативного интерфейса Java (JNI). Позволяет вызывать нативные функции и обращаться к памяти за пределами JVM. 
Пока это вызовы на C, но в будущем планируется поддержка дополнительных языков (таких как C++, Fortran).
## Java 18
- UTF-8 по умолчанию для всех ОС (ранее бралась кодировка ОС)
## Java 19
- Превью для VirtualThread - виртуальный поток jvm. Весит меньше, чем Thread, позволяет эмулировать многопоточность 
внутри виртуальной машины
## Java 21
- Сопоставление шаблонов для switch был закончен (со времён Java 17)
```java
Object obj = …
return switch (obj) {
    case Integer i -> String.format("int %d", i);
    case Long l -> String.format("long %d", l);
    case Double d -> String.format("double %f", d);
    case String s -> String.format("String %s", s);
    default -> obj.toString();
};
```
- Паттерны switch могут снабжаться условиями с использованием нового ключевого слова when:
```java
Object obj = …
return switch (obj) {
    case Integer i when i > 0 -> String.format("positive int %d", i);
    case Integer i -> String.format("int %d", i);
    case String s -> String.format("String %s", s);
    default -> obj.toString();
};
```
- Добавлена поддержка паттерна null для switch: можно вынести отдельной веткой или прописать через запятую с default 
(если case null отсутствует, то будет выбрасываться NPE, даже если есть ветка default)
- Несмотря на объединение null и default, другое объединение паттернов вместе работать не будет
- Были добавлены паттерны записей для record:
```java
record Point(int x, int y) {}

static void printSum(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println(x + y);
    }
}

static void printSum(Object obj) {
    switch (obj) {
        case Point(int x, int y) -> System.out.println(x + y);
        default -> System.out.println("Not a point");
    }
}
```
- Также паттерны записи могут быть вложенными:
```java
record Point(int x, int y) {}
enum Color { RED, GREEN, BLUE }
record ColoredPoint(Point p, Color c) {}
record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}

static void printColorOfUpperLeftPoint(Rectangle r) {
    if (r instanceof Rectangle(ColoredPoint(Point p, Color c), ColoredPoint lr)) {
        System.out.println(c);
    }
}
```
- Паттерны записей могут использоваться только в instanceOf и switch
- Превью строковых шаблонов: String str = STR."\{10} plus \{20} equals \{10 + 20}" - в str будет лежать "10 + 20 equals 
30"
- Превью - main больше не должен быть public static и иметь параметр String[], но будет следующий приоритет запуска 
нескольких main:
  1) static void main(String[] args)
  2) static void main()
  3) void main(String[] args)
  4) void main()
- Превью - можно писать программы без написания классов - под капотом создастся анонимный класс:
```java
String greeting = "Hello, World!";

void main() {
    System.out.println(greeting);
}
```
- VirtualThread стали стабильными, появилось следующее API:
  - Thread.Builder – билдер потоков. Например, виртуальный поток можно создать путём вызова Thread.ofVirtual().name("name").unstarted(runnable).
  - Thread.startVirtualThread(Runnable) – создаёт и сразу же запускает виртуальный поток.
  - Thread.isVirtual() – проверяет, является ли поток виртуальным.
  - Executors.newVirtualThreadPerTaskExecutor() – возвращает исполнитель, который создаёт новый виртуальный поток на каждую задачу.
- 32-битный порт OpenJDK стал deprecated for removal