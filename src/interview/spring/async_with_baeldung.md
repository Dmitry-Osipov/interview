# How To Do @Async in Spring
## Оглавление
- [Описание](#описание)
- [Включение поддержки асинхрона](#включение-поддержки-асинхрона)
- [@Async](#async)
- [@Async with Void Return Type](#async-with-void-return-type)
- [@Async with Return Type](#async-with-return-type)
- [Объединение ответов двух @Async-сервисов](#объединение-ответов-двух-async-сервисов)
- [Executor](#executor)
- [Переопределение Executor на уровне метода](#переопределите-executor-на-уровне-метода)
- [Переопределение Executor на уровне приложения](#переопределите-executor-на-уровне-приложения)
- [Перехват исключений](#перехват-исключений)
## Описание
В этом уроке мы рассмотрим поддержку асинхронного выполнения в Spring и аннотацию @Async.

Проще говоря, аннотирование метода bean с помощью @Async приводит к его выполнению в отдельном потоке. Другими словами, 
вызывающая сторона не будет ждать завершения работы вызванного метода.

Интересным аспектом Spring является то, что поддержка событий во фреймворке также поддерживает асинхронную обработку, 
если это необходимо.
## Включение поддержки асинхрона
Давайте начнем с включения асинхронной обработки в конфигурации Java.

Для этого добавим @EnableAsync в класс конфигурации:
```java
@Configuration
@EnableAsync
public class SpringAsyncConfig { ... }
```
Аннотации enable достаточно. Но есть также несколько простых опций для настройки:
- annotation - По умолчанию @EnableAsync обнаруживает аннотацию Spring @Async и EJB 3.1 javax.ejb.Asynchronous. Мы 
можем использовать эту опцию для обнаружения и других, определенных пользователем типов аннотаций
- mode указывает тип совета, который должен быть использован - JDK proxy based или AspectJ weaving
- proxyTargetClass указывает тип прокси, который должен быть использован - CGLIB или JDK. Этот атрибут имеет силу 
только в том случае, если режим установлен в AdviceMode.PROXY
- order задает порядок, в котором должен применяться AsyncAnnotationBeanPostProcessor. По умолчанию он запускается 
последним, чтобы учесть все существующие прокси.

Мы также можем включить асинхронную обработку с помощью XML-конфигурации, используя пространство имен задач:
```xml
<task:executor id="myexecutor" pool-size="5"  />
<task:annotation-driven executor="myexecutor"/>
```
## @Async
Для начала давайте пройдемся по правилам. У @Async есть два ограничения:
- Он должен применяться только к публичным методам.
- Self-invocation - вызов метода async из того же класса - не работает.

Причины просты: Метод должен быть публичным, чтобы его можно было проксировать. Самовызов не работает, потому что он 
обходит прокси и вызывает базовый метод напрямую.
## @Async with Void Return Type
```java
@Async
public void syncMethodWithVoidReturnType() {
    System.out.println("Execute method asynchronously." + Thread.currentThread().getName());
}
```
## @Async with Return Type
Мы также можем применить @Async к методу с типом возврата, обернув фактический возврат в Future:
```java
import java.util.concurrent.Future;

@Async
public Future<String> asyncMethodWithReturnType() {
    System.out.println("Execute method asynchronously - " + Thread.currentThread().getName());
    try {
        Thread.sleep(5000);
        return new AsyncResult<String>("hello world !!!!!");
    } catch (InterruptedException e) {
        log.error("InterruptedException: {}", e.getMessage());
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
    }
}
```
Spring также предоставляет класс AsyncResult, который реализует Future. Мы можем использовать его для отслеживания 
результата выполнения асинхронного метода.

Теперь давайте вызовем описанный выше метод и получим результат асинхронного процесса с помощью объекта Future:
```java
public void testAsyncAnnotationForMethodsWithReturnType() throws InterruptedException, ExecutionException {
    System.out.println("Invoking an asynchronous method. " + Thread.currentThread().getName());
    Future<String> future = asyncAnnotationExample.asyncMethodWithReturnType();

    while (true) {
        if (future.isDone()) {
            System.out.println("Result from asynchronous process - " + future.get());
            break;
        }
        System.out.println("Continue doing something else. ");
        Thread.sleep(1000);
    }
}
```
## Объединение ответов двух @Async-сервисов
Используя метод completable для ответа типа AsyncResult, мы заворачиваем фактический возврат метода в CompletableFuture:
```java
import java.util.concurrent.CompletableFuture;

@Async
public CompletableFuture<String> asyncGetData() throws InterruptedException {
    System.out.println("Execute method asynchronously " + Thread.currentThread().getName());
    Thread.sleep(4000);
    return new AsyncResult<>(super.getClass().getSimpleName() + " response !!!").completable();
}
```
Теперь мы реализуем основной сервис, который мы будем использовать для объединения ответов CompletableFuture двух 
@Async-сервисов:
```java
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    @Autowired
    private FirstAsyncService firstService;
    @Autowired
    private SecondAsyncService secondService;

    public CompletableFuture<String> asyncMergeServicesResponse() throws InterruptedException {
        CompletableFuture<String> firstServiceResponse = firstService.asyncGetData();
        CompletableFuture<String> secondServiceResponse = secondService.asyncGetData();
        
        // Merge responses from FirstAsyncService and SecondAsyncService
        return firstServiceResponse.thenCompose(firstServiceValue -> 
                secondServiceResponse.thenApply(secondServiceValue -> firstServiceValue + secondServiceValue)
        );
    }
}
```
Давайте вызовем вышеуказанную службу и получим результат работы асинхронных служб с помощью объекта CompletableFuture:
```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public void testAsyncAnnotationForMergedServicesResponse() throws InterruptedException, ExecutionException {
    System.out.println("Invoking an asynchronous method. " + Thread.currentThread().getName());
    CompletableFuture<String> completableFuture = asyncServiceExample.asyncMergeServicesResponse();
    
    while (true) {
        if (completableFuture.isDone()) {
            System.out.println("Result from asynchronous process - " + completableFuture.get());
            break;
        }

        System.out.println("Continue doing something else. ");
        Thread.sleep(1000);
    }
}
```
## Executor
По умолчанию Spring использует SimpleAsyncTaskExecutor для асинхронного выполнения этих методов. Однако мы можем 
переопределить настройки по умолчанию на двух уровнях: на уровне приложения или на уровне отдельных методов.
## Переопределите Executor на уровне метода
Нам нужно объявить требуемого исполнителя в классе конфигурации:

```java
@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
```
Затем нужно указать имя исполнителя в качестве атрибута в @Async:
```java
@Async("threadPoolTaskExecutor")
public void asyncMethodWithConfiguredExecutor() {
    System.out.println("Execute method with configured executor - " + Thread.currentThread().getName());
}
```
## Переопределите Executor на уровне приложения
Класс конфигурации должен реализовать интерфейс AsyncConfigurer. Таким образом, он должен реализовать метод 
getAsyncExecutor(). Здесь мы вернем исполнителя для всего приложения. Теперь он станет исполнителем по умолчанию для 
запуска методов, аннотированных @Async:
```java
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
```
## Перехват исключений
Когда тип возвращаемого метода - Future, обработка исключений проста. Метод Future.get() выбросит исключение.

Однако исключения не будут переданы вызывающему потоку, если тип возвращаемого значения - void. Поэтому нам нужно 
добавить дополнительные конфигурации для обработки исключений.

Мы создадим собственный асинхронный обработчик исключений, реализовав интерфейс AsyncUncaughtExceptionHandler. Метод
handleUncaughtException() вызывается при возникновении любых не пойманных асинхронных исключений:
```java
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.out.println("Exception message - " + throwable.getMessage());
        System.out.println("Method name - " + method.getName());
        for (Object param : obj) {
            System.out.println("Parameter value - " + param);
        }
    }
}
```
В предыдущем разделе мы рассмотрели интерфейс AsyncConfigurer, реализованный в классе конфигурации. В рамках этого нам 
также необходимо переопределить метод getAsyncUncaughtExceptionHandler(), чтобы вернуть наш собственный обработчик 
асинхронных исключений:
```java
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
```