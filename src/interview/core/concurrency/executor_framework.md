# Executor Framework
## Оглавление
- [Описание](#описание)
- [Future](#future)
- [Как создать ExecutorService](#как-создать-executorservice)
- [Обработка задач "пачками"](#обработка-задач-пачками)
- [Отмена задач и прекращение обслуживания](#отмена-задача-и-прекращение-обслуживания)
- [Прерывание задач](#прерывание-задач)
- [Прерывание тредов](#прерывание-тредов)
- [Разница между interrupted() и isInterrupted()](#разница-между-interrupted-и-isinterrupted)
- [Кооперативный механизм прерывания](#кооперативный-механизм-прерывания)
- [Что делать с InterruptedException](#что-делать-с-interruptedexception)
- [CompletableFuture](#completablefuture)
## Описание
- Тред - дорогой ресурс, поэтому мы хотим:
  - ограничивать кол-во наших тредов, чтобы не устроить Out of Memory
  - переиспользовать имеющиеся треды, подавая им новые задачи после завершения старых
  - но если какой-то тред "вылетел" - автоматически создавать новый
- В стандартной библиотеке для этого есть Thread Pools, не надо ничего делать самостоятельно
- Никто не использует Thread API напрямую
```java
//Абстракция вычислительной задачи, возвращающей результат
public interface Callable<V> {
  V call() throws Exception;
}

//Абстракция «менджера тредов»
public interface ExecutorService {
  <T> Future<T> submit(Callable<T> task);
  /*...есть и много другого, речь впереди...*/
}
```
## Future
```java
//Абстракция результата "in progress", который можно ждать,
//а можно и отменить
public interface Future<V> {
    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isDone();

    boolean isCancelled();
}
```
## Как создать ExecutorService
```java
public class  Executors { 
    public static ExecutorService newFixedThreadPool(int nThreads);  //фиксированный размер пула
    public static ExecutorService newSingleThreadExecutor();
    public static ExecutorService newCachedThreadPool();  //пул растёт по необходимости, держит неактивный тред 60 секунд
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize);  //позволяет выполнять задачи с задержкой или периодичностью
    // ...
}
```
## Обработка задач "пачками"
```java
//Запускаем и ждём, пока все выполнятся
//List<Future<T>>, а не List<T>, т. к. возможны исключения
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;

//Запускаем, возвращаем первый успешный результат,
//отменяем остальные
<T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException;
```
## Отмена задача и "прекращение обслуживания"
```java
//отменить задачу, если она ещё не начала выполняться
future.cancel(false);
//запросить прерывание задачи (подробности впереди)
future.cancel(true);

//Запретить приём новых задач
executorService.shutdown();
//Подождать, пока принятые задачи завершатся
if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
    //Прервать выполнение задач
    service.shutdownNow();
}
```
## Прерывание задач
- В ранних версиях Java существовали (ныне deprecated) методы принудительной остановки и приостановки/возобновления 
тредов, но это _оказалось плохой идеей_:
  - нет гарантий, что тред не остановится посередине атомарной операции
  - приостановка может на неопределённое время "завесить" блокировку
- В итоге, имеется кооперативный механизм прерывания
## Прерывание тредов
- ExecutorService.shutdownNow() вызывает метод Thread.interrupt() на потоках выполнения
- Метод Thread.isInterrupted() возвращает статус прерывания треда
- JMM Interruption Rule: вызов метода interrupt() внешним потоком happens-before прерываемый поток узнаёт о том, что он 
прерван
## Разница между interrupted() и isInterrupted()
Механизм прерывания работы потока в Java реализован с использованием внутреннего флага, известного как статус 
прерывания. Прерывание потока вызовом Thread.interrupt() устанавливает этот флаг. Методы Thread.interrupted() и 
isInterrupted() позволяют проверить, является ли поток прерванным. Когда прерванный поток проверяет статус прерывания, 
вызывая статический метод Thread.interrupted(), статус прерывания сбрасывается. Нестатический метод isInterrupted() 
используется одним потоком для проверки статуса прерывания у другого потока, не изменяя флаг прерывания.
## Кооперативный механизм прерывания
- Если вычисления в цикле, тред обязан периодически проверять статус Thread.currentThread().isInterrupted() и, если флаг
выставлен, записав в лог факт прерывания, выходить из метода
- На ждущих методах может быть выброшен InterruptedException. Что с ним делать?
## Что делать с InterruptedException
- Если контекст позволяет, его всегда следует пробрасывать выше
- Если выше пробрасывать нельзя (например, находимся в методе run интерфейса Runnable), то:
```java
try {
    throw new InterruptedException();
} catch (InterruptedException e) {
    //записываем факт прерывания в лог ...
    ...
    //восстанавливаем interrupted-статус
    Thread.currentThread().interrupt();
    //выходим из прерванной процедуры
    return;
}
```
- Просто так "проглатывать" InterruptedException _ни в кое случае нельзя_
- Рекомендуется записывать факт прерывания в лог для прозрачности отладки
## CompletableFuture
- Появились в Java 8
- Позволяют явно задать результат (отсюда и "Completable") и собрать цепочку асинхронных вычислений
- Могут быть использованы так:
```java
CompletableFuture<Integer> f = new CompletableFuture<>();
executor.execute(() -> {
  int n = workHard(arg);
  f.complete(n); 
});

executor.execute(() -> {
  int n = workSmart(arg);
  f.complete(n); 
});

executor.execute(() -> {
  Throwable t = ...;
  f.completeExceptionally(t); 
});
```
Композиция CompletableFuture с действием:

| Method            | Parameter                 | Description                                                               |
|-------------------|---------------------------|---------------------------------------------------------------------------|
| thenApply         | T -> U                    | Применение функции к результату                                           |
| thenAccept        | T -> void                 | Как thenApply, но с void                                                  |
| thenCompose       | T -> CompletableFuture<U> | Вызов функции на результате и возврат CompletableFuture                   |
| handle            | (T, Throwable) -> U       | Обработать результат или ошибку и выдать новый результат                  |
| whenComplete      | (T, Throwable) -> void    | Как handle, но с void                                                     |
| completeOnTimeout | T, long, TimeUnit         | Выдать заданное значение в качестве результата в случае timeout (Java 9+) |
| orTimeout         | long, TimeUnit            | Выбросить "TimeoutException" в случае timeout (Java 9+)                   |
| thenRun           | Runnable                  | Выполнение Runnable с void                                                |

Композиция нескольких CompletableFuture:

| Method         | Parameter                            | Description                                                                              |
|----------------|--------------------------------------|------------------------------------------------------------------------------------------|
| thenCombine    | CompletableFuture<U>, (T, U) -> V    | Выполнить оба и объедините результаты с помощью заданной функции                         |
| thenAcceptBoth | CompletableFuture<U>, (T, U) -> void | Как thenCombine, но с void                                                               |
| runAfterBoth   | CompletableFuture<?>, Runnable       | Выполнить runnable после завершения                                                      |
| applyToEither  | CompletableFuture<T>, T -> V         | Когда результат будет получен от одного или другого, передайте его в данную функцию      |
| acceptEither   | CompletableFuture<T>, T -> void      | Как applyEither, но с void                                                               |
| runAfterEither | CompletableFuture<?>, Runnable       | Выполнение runnable после завершения одного или другого                                  | 
| static allOf   | CompletableFuture<?>                 | Завершить с void после завершения всех futures                                           |
| static anyOf   | CompletableFuture<?>                 | Завершить после любого из переданных futures, с тем же результатом, приведённым к Object |

Если этого оказалось мало, то каждый из этих методов имеет вариант с постфиксов Async (например, thenApplyAsync), 
позволяющий выполнить доп. действие в другом треде заданного Executor-а