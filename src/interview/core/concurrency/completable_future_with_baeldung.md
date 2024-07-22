# Guide to CompletableFuture
## Оглавление
- [Описание](#описание)
- [Асинхронные вычисления в Java](#асинхронные-вычисления-в-java)
- [Использование CompletableFuture как простое Future](#использование-completablefuture-как-простое-future)
- [CompletableFuture с инкапсулированной логикой вычислений](#completablefuture-с-инкапсулированной-логикой-вычислений)
- [Обработка результатов асинхронных вычислений](#обработка-результатов-асинхронных-вычислений)
- [Сбор Future (Combining Future)](#сбор-future-combining-future)
- [Разница между thenApply() и thenCompose()](#разница-между-thenapply-и-thencompose)
- [Параллельный запуск нескольких Future](#параллельный-запуск-нескольких-future)
- [Обработка исключений](#обработка-исключений)
- [Асинхронные методы](#асинхронные-методы)
- [JDK 9 CompletableFuture API](#jdk-9-completablefuture-api)
## Описание
CompletableFuture был представлен в качестве улучшения API Concurrency в Java 8.
## Асинхронные вычисления в Java
Об асинхронных вычислениях сложно рассуждать. Обычно мы хотим рассматривать любое вычисление как серию шагов, но в 
случае асинхронных вычислений действия, представленные в виде обратных вызовов, как правило, либо разбросаны по всему коду, либо глубоко вложены друг в друга. Ситуация становится еще хуже, когда нам нужно обработать ошибки, которые могут возникнуть во время одного из шагов.

Интерфейс Future был добавлен в Java 5, чтобы служить результатом асинхронных вычислений, но в нем не было методов для
объединения этих вычислений или обработки возможных ошибок.

В Java 8 появился класс CompletableFuture. Наряду с интерфейсом Future он также реализует интерфейс CompletionStage. 
Этот интерфейс определяет контракт для асинхронного этапа вычислений, который мы можем комбинировать с другими этапами.

CompletableFuture - это одновременно и строительный блок, и фреймворк, содержащий около 50 различных методов для 
составления, комбинирования и выполнения асинхронных шагов вычислений и обработки ошибок.

Такой большой API может показаться непомерно большим, но в большинстве случаев они делятся на несколько четких и ясных
сценариев использования.
## Использование CompletableFuture как простое Future
Во-первых, класс CompletableFuture реализует интерфейс Future, так что мы можем использовать его как реализацию Future,
но с дополнительной логикой завершения.

Например, мы можем создать экземпляр этого класса с конструктором no-arg для представления некоторого будущего 
результата, раздать его потребителям и завершить его в некоторое время в будущем с помощью метода complete. Потребители могут использовать метод get, чтобы заблокировать текущий поток до тех пор, пока этот результат не будет предоставлен.

В примере ниже мы имеем метод, который создает экземпляр CompletableFuture, затем выполняет некоторые вычисления в 
другом потоке и сразу же возвращает Future.

Когда вычисления завершены, метод завершает Future, предоставляя результат методу complete:
```java
public Future<String> calculateAsync() throws InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
        Thread.sleep(500);
        completableFuture.complete("Hello");
        return null;
    });

    return completableFuture;
}
```
Чтобы запустить вычисления, мы используем API Executor. Этот метод создания и завершения CompletableFuture может быть 
использован с любым механизмом параллелизма или API, включая необработанные потоки.

Обратите внимание, что метод calculateAsync возвращает экземпляр Future.

Мы просто вызываем метод, получаем экземпляр Future и вызываем для него метод get, когда готовы заблокировать результат.

Также обратите внимание, что метод get выбрасывает некоторые проверенные исключения, а именно ExecutionException
(инкапсулирует исключение, возникшее во время вычислений) и InterruptedException (исключение, означающее, что поток 
был прерван до или во время выполнения действия):
```java
Future<String> completableFuture = calculateAsync();

// ... 

String result = completableFuture.get();
assertEquals("Hello", result);
```
Если нам уже известен результат вычислений, мы можем использовать статический метод completedFuture с аргументом,
представляющим результат этих вычислений. Соответственно, метод get Future никогда не будет блокироваться, сразу
возвращая этот результат:
```java
Future<String> completableFuture = CompletableFuture.completedFuture("Hello");

// ...

String result = completableFuture.get();
assertEquals("Hello", result);
```
В качестве альтернативного сценария мы можем захотеть отменить выполнение Future.
## CompletableFuture с инкапсулированной логикой вычислений
Приведенный выше код позволяет нам выбрать любой механизм параллельного выполнения, но что, если мы хотим пропустить 
этот шаблон и выполнить некоторый код асинхронно?

Статические методы runAsync и supplyAsync позволяют нам создать экземпляр CompletableFuture из функциональных типов 
Runnable и Supplier соответственно.

Runnable и Supplier - это функциональные интерфейсы, которые позволяют передавать свои экземпляры в виде 
лямбда-выражений благодаря новой возможности Java 8.

Интерфейс Runnable - это старый интерфейс, используемый в потоках и не позволяющий возвращать значение.

Интерфейс Supplier - это общий функциональный интерфейс с единственным методом, который не имеет аргументов и 
возвращает значение параметризованного типа.

Это позволяет нам предоставить экземпляр поставщика в виде лямбда-выражения, которое выполняет вычисления и 
возвращает результат. Это очень просто:
```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

// ...

assertEquals("Hello", future.get());
```
## Обработка результатов асинхронных вычислений
Самый общий способ обработать результат вычислений - передать его в функцию. Метод thenApply делает именно это, он 
принимает экземпляр Function, использует его для обработки результата и возвращает Future, содержащее значение,
возвращенное функцией:
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");

assertEquals("Hello World", future.get());
```
Если нам не нужно возвращать значение по цепочке Future, мы можем использовать экземпляр функционального интерфейса
Consumer. Его единственный метод принимает параметр и возвращает void.

В CompletableFuture есть метод для этого случая использования. Метод thenAccept принимает Consumer и передает ему 
результат вычислений. Затем финальный вызов future.get() возвращает экземпляр типа Void:
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

CompletableFuture<Void> future = completableFuture.thenAccept(s -> System.out.println("Computation returned: " + s));

future.get();
```
Наконец, если нам не нужно значение вычислений или мы хотим вернуть какое-то значение в конце цепочки, то мы можем 
передать в метод thenRun лямбду Runnable. В следующем примере мы просто выводим строку в консоль после вызова
future.get():
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

CompletableFuture<Void> future = completableFuture.thenRun(() -> System.out.println("Computation finished."));

future.get();
```
## Сбор Future (Combining Future)
Лучшая часть API CompletableFuture - это возможность объединять экземпляры CompletableFuture в цепочку вычислительных
шагов.

Результат этой цепочки сам по себе является CompletableFuture, который допускает дальнейшее объединение и 
комбинирование. Такой подход широко распространен в функциональных языках и часто называется монадическим 
паттерном проектирования.

В следующем примере мы используем метод thenCompose для последовательного объединения двух Futures в цепочку.

Обратите внимание, что этот метод принимает функцию, которая возвращает экземпляр CompletableFuture. Аргументом этой
функции является результат предыдущего шага вычислений. Это позволяет нам использовать это значение внутри лямбды
следующего CompletableFuture:
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
        .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

assertEquals("Hello World", completableFuture.get());
```
Метод thenCompose вместе с thenApply реализуют основные строительные блоки паттерна монады. Они тесно связаны с 
методами map и flatMap классов Stream и Optional, также доступными в Java 8.

Оба метода получают функцию и применяют ее к результату вычислений, но метод thenCompose (flatMap) получает функцию, 
которая возвращает другой объект того же типа. Такая функциональная структура позволяет компоновать экземпляры этих
классов как строительные блоки.

Если мы хотим выполнить два независимых Futures и что-то сделать с их результатами, мы можем использовать метод 
thenCombine, который принимает Future и Function с двумя аргументами, чтобы обработать оба результата:
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
        .thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + s2));

assertEquals("Hello World", completableFuture.get());
```
Более простой случай - когда мы хотим что-то сделать с результатами двух Future, но нам не нужно передавать 
результирующее значение по цепочке Future. Здесь нам поможет метод thenAcceptBoth:
```java
CompletableFuture future = CompletableFuture.supplyAsync(() -> "Hello")
        .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"), 
                (s1, s2) -> System.out.println(s1 + s2));
```
## Разница между thenApply() и thenCompose()
В предыдущих разделах мы приводили примеры thenApply() и thenCompose(). Оба API помогают создавать цепочки различных 
вызовов CompletableFuture, но использование этих двух функций различно.
### thenApply()
Мы можем использовать этот метод для работы с результатом предыдущего вызова. Однако следует помнить, что возвращаемый 
тип будет комбинированным из всех вызовов.

Поэтому этот метод полезен, когда мы хотим преобразовать результат вызова CompletableFuture:
```java
CompletableFuture<Integer> finalResult = compute().thenApply(s-> s + 1);
```
### thenCompose()
Функция thenCompose() похожа на thenApply() тем, что обе возвращают новый CompletionStage. Однако thenCompose() 
использует предыдущий этап в качестве аргумента. Она сплющивает и возвращает Future с результатом напрямую, а не 
вложенное Future, как мы наблюдали в thenApply():
```java
CompletableFuture<Integer> computeAnother(Integer i){
    return CompletableFuture.supplyAsync(() -> 10 + i);
}
CompletableFuture<Integer> finalResult = compute().thenCompose(this::computeAnother);
```
Так что если идея состоит в том, чтобы выстроить цепочку методов CompletableFuture, то лучше использовать thenCompose().
## Параллельный запуск нескольких Future
Когда нам нужно выполнить несколько Futures параллельно, мы обычно хотим дождаться их выполнения и затем обработать их 
объединенные результаты.

Статический метод CompletableFuture.allOf позволяет дождаться завершения выполнения всех Future, указанных в 
качестве var-arg:
```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");

CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

// ...

combinedFuture.get();

assertTrue(future1.isDone());
assertTrue(future2.isDone());
assertTrue(future3.isDone());
```
Обратите внимание, что возвращаемый тип CompletableFuture.allOf() - CompletableFuture<Void>. Ограничение этого метода в
том, что он не возвращает объединенные результаты всех фьючерсов. Вместо этого нам приходится получать результаты из
Futures вручную. К счастью, метод CompletableFuture.join() и Java 8 Streams API упрощают эту задачу:
```java
String combined = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.joining(" "));

assertEquals("Hello Beautiful World", combined);
```
Метод CompletableFuture.join() похож на метод get, но он выбрасывает непроверенное исключение в случае, если Future не 
завершается нормально. Это позволяет использовать его в качестве ссылки на метод в методе Stream.map().
## Обработка исключений
Для обработки ошибок в цепочке асинхронных вычислительных шагов мы должны аналогичным образом адаптировать идиому
throw/catch.

Вместо того чтобы ловить исключение в синтаксическом блоке, класс CompletableFuture позволяет нам обрабатывать его в 
специальном методе handle. Этот метод получает два параметра: результат вычисления (если оно завершилось успешно) и 
выброшенное исключение (если какой-то шаг вычисления не завершился нормально).

В следующем примере мы используем метод handle для предоставления значения по умолчанию, когда асинхронное вычисление 
приветствия завершилось с ошибкой, поскольку не было предоставлено имя:
```java
String name = null;

// ...

CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(() -> {
    if (name == null) {
        throw new RuntimeException("Computation error!");
    }
    return "Hello, " + name;
}).handle((s, t) -> s != null ? s : "Hello, Stranger!");

assertEquals("Hello, Stranger!", completableFuture.get());
```
В качестве альтернативного сценария предположим, что мы хотим вручную заполнить Future значением, как в первом примере, 
но при этом имеем возможность заполнить его исключением. Метод completeExceptionally предназначен именно для этого.
Метод completableFuture.get() в следующем примере выбрасывает ExecutionException с RuntimeException в качестве причины:
```java
CompletableFuture<String> completableFuture = new CompletableFuture<>();

// ...

completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));

// ...

completableFuture.get(); // ExecutionException
```
В приведенном выше примере мы могли бы обработать исключение с помощью метода handle асинхронно, но с помощью метода
get мы можем использовать более типичный подход - синхронную обработку исключений.
## Асинхронные методы
Большинство методов fluent API в классе CompletableFuture имеют два дополнительных варианта с постфиксом Async. Эти
методы обычно предназначены для запуска соответствующего этапа выполнения в другом потоке.

Методы без постфикса Async запускают следующий этап выполнения, используя вызывающий поток. Напротив, метод Async без 
аргумента Executor запускает этап с использованием общей реализации пула вилок/соединений Executor, доступ к которому 
осуществляется с помощью ForkJoinPool.commonPool(), при условии, что параллелизм > 1. Наконец, метод Async с аргументом
Executor запускает шаг с использованием переданного Executor.

Вот модифицированный пример, который обрабатывает результат вычислений с помощью экземпляра Function. Единственное 
видимое отличие - метод thenApplyAsync, но под капотом применение функции обернуто в экземпляр ForkJoinTask. Это
позволяет нам еще больше распараллелить вычисления и более эффективно использовать системные ресурсы:
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> s + " World");

assertEquals("Hello World", future.get());
```
## JDK 9 CompletableFuture API
В Java 9 API CompletableFuture дополнен следующими изменениями:
- Добавлены новые фабричные методы
- Поддержка задержек и тайм-аутов
- Улучшена поддержка подклассификации

и новые API для экземпляров:
- Executor defaultExecutor()
- CompletableFuture<U> newIncompleteFuture()
- CompletableFuture<T> copy()
- CompletionStage<T> minimalCompletionStage()
- CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor)
- CompletableFuture<T> completeAsync(Supplier<? extends T> supplier)
- CompletableFuture<T> orTimeout(long timeout, TimeUnit unit)
- CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit)

У нас также есть несколько статических методов:
- Executor delayedExecutor(long delay, TimeUnit unit, Executor executor)
- Executor delayedExecutor(long delay, TimeUnit unit)
- <U> CompletionStage<U> completedStage(U value)
- <U> CompletionStage<U> failedStage(Throwable ex)
- <U> CompletableFuture<U> failedFuture(Throwable ex)

Наконец, для решения проблемы таймаута в Java 9 появились еще две новые функции:
- orTimeout()
- completeOnTimeout()
