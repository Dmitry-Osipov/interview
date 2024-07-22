# Runnable vs Callable
## Оглавление
- [Описание](#описание)
- [Механизм выполнения](#механизм-выполнения)
- [Возвращаемые значения](#возвращаемые-значения)
- [Перехват исключений](#перехват-исключений)
## Описание
Runnable - это основной интерфейс для представления многопоточных задач, а в Java 1.5 появился Callable как улучшенная 
версия Runnable.
## Механизм выполнения
Оба интерфейса предназначены для представления задачи, которая может выполняться несколькими потоками. Мы можем 
запускать задачи Runnable с помощью класса Thread или ExecutorService, в то время как Callables можно запускать только 
с помощью последнего.
## Возвращаемые значения
### Runnable
Интерфейс Runnable является функциональным интерфейсом и имеет единственный метод run(), который не принимает никаких 
параметров и не возвращает никаких значений.

Это подходит для ситуаций, когда нам не нужен результат выполнения потока, например, для регистрации входящих событий:
```java
public interface Runnable {
    void run();
}
```

Давайте разберемся в этом на примере:
```java
public class EventLoggingTask implements  Runnable{
    private Logger logger = LoggerFactory.getLogger(EventLoggingTask.class);

    @Override
    public void run() {
        logger.info("Message");
    }
}
```
В этом примере поток просто прочитает сообщение из очереди и запишет его в файл журнала. Никаких значений задача не 
возвращает.

Мы можем запустить задачу с помощью ExecutorService:
```java
public void executeTask() {
    executorService = Executors.newSingleThreadExecutor();
    Future future = executorService.submit(new EventLoggingTask());
    executorService.shutdown();
}
```
В этом случае объект Future не будет иметь никакого значения.
### Callable
Интерфейс Callable - это общий интерфейс, содержащий единственный метод call(), который возвращает общее значение V:
```java
public interface Callable<V> {
    V call() throws Exception;
}
```
Давайте посмотрим, как вычислить факториал числа:
```java
public class FactorialTask implements Callable<Integer> {
    int number;
    
    public Integer call() throws InvalidParamaterException {
        int fact = 1;
        // ...
        for(int count = number; count > 1; count--) {
            fact = fact * count;
        }

        return fact;
    }
}
```
Результат метода call() возвращается в объекте Future при использовании ExecutorService:
```java
@Test
public void whenTaskSubmitted_ThenFutureResultObtained(){
    FactorialTask task = new FactorialTask(5);
    Future<Integer> future = executorService.submit(task);
 
    assertEquals(120, future.get().intValue());
}
```
## Перехват исключений
### Runnable
Поскольку в сигнатуре метода не указан пункт «throws», у нас нет возможности распространять дальнейшие проверенные 
исключения.
### Callable
Метод Callable call() содержит пункт «throws Exception», поэтому мы можем легко распространять проверенные исключения 
дальше:
```java
public class FactorialTask implements Callable<Integer> {
    // ...
    public Integer call() throws InvalidParamaterException {

        if(number < 0) {
            throw new InvalidParamaterException("Number should be positive");
        }
    // ...
    }
}
```
В случае запуска вызываемой программы с помощью ExecutorService исключения собираются в объекте Future. Мы можем 
проверить это, вызвав метод Future.get().

При этом будет выброшено исключение ExecutionException, которое обертывает исходное исключение:
```java
@Test(expected = ExecutionException.class)
public void whenException_ThenCallableThrowsIt() {
 
    FactorialCallableTask task = new FactorialCallableTask(-5);
    Future<Integer> future = executorService.submit(task);
    Integer result = future.get().intValue();
}
```
В приведенном выше тесте возникает исключение ExecutionException, поскольку мы передаем недопустимое число. Мы можем 
вызвать метод getCause() на этом объекте исключения, чтобы получить исходное проверенное исключение.

Если мы не вызовем метод get() класса Future, исключение, выброшенное методом call(), не будет сообщено обратно, и 
задача по-прежнему будет отмечена как выполненная:
```java
@Test
public void whenException_ThenCallableDoesntThrowsItIfGetIsNotCalled(){
    FactorialCallableTask task = new FactorialCallableTask(-5);
    Future<Integer> future = executorService.submit(task);
 
    assertEquals(false, future.isDone());
}
```
Приведенный выше тест будет успешно пройден, даже если мы выбросили исключение из-за отрицательного значения 
параметра FactorialCallableTask.
