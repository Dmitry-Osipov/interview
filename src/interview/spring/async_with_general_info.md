# @Async with General Info
## Оглавление
- [Для чего нам асинхронное выполнение](#для-чего-нам-асинхронное-выполнение)
- [@EnableAsync](#enableasync)
- [@Async](#async)
- [Task Executor (исполнитель задач)](#task-executor-исполнитель-задач)
- [Exception handle (обработка ошибок)](#exception-handle-обработка-ошибок)
## Для чего нам асинхронное выполнение
### 1. Улучшение производительности приложения
В обычном синхронном программировании задачи выполняются одна за другой (или по-другому говорят, что программа 
последовательно выполняется сверху вниз). Следующая задача выполняется только после того, как завершилась предыдущая.

В асинхронном программировании несколько задач могут быть выполнены одновременно. Можно перейти к выполнению любой 
другой задачи, не дожидаясь завершения работы запущенных ранее задач.

Благодаря одновременному выполнению нескольких задач достигается увеличение производительности приложения (по сути, 
уменьшается время, которое пользователь должен ждать, чтобы получить те же самые результаты по сравнению с синхронным 
выполнением программы).
### 2. Выделение затратных (по времени и ресурсам - expensive jobs) задач в отдельные потоки и/или выполнение их в фоновом режиме
Рассмотрим пример. Предположим, что мы разрабатываем приложение, в котором реализуем оплату заказа. Сам процесс 
выполнения и зачисления оплаты занимает некоторое продолжительное время. Мы хотим, чтобы пользователь не ждал окончания 
всего процесса и мог продолжить пользоваться приложением. Результат оплаты пользователь сможет в дальнейшем посмотреть
в своем личном кабинете или, например, получив уведомление на электронную почту или телефон.

Чтобы осуществить описанный выше сценарий, нам необходимо инициировать процесс оплаты асинхронно в отдельном потоке.
## @EnableAsync
Для того, чтобы разрешить асинхронное выполнение, необходимо создать конфигурационный класс, пометив его аннотацией 
@Configuration, а затем еще добавить к нему аннотацию @EnableAsync.
```java
@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport { 
    // ... 
}
```
Аннотация @EnableAsync включает следующее поведение Spring:
- Spring распознает методы, помеченные аннотацией @Async
- Spring запускает эти методы в фоновом пуле потоков (background thread pool)
## @Async
Как уже было сказано выше, для того Spring запустил метод асинхронно в отдельном потоке, необходимо обозначить его 
аннотацией @Async:
```java
@Async
public void asyncMethod(SomeDomainClass someDomainObject) { 
    // ... 
}
```
Существует несколько правил, касательно данной аннотации:
- Аннотация @Async должна применяться только на публичных методах (public). Дело в том, что Spring создает прокси для 
метода с этой аннотацией, и для работы прокси метод должен быть публичным.
- Нельзя вызывать метод, помеченный @Async из того же класса, где он определен. Такой вызов не сработает, так как он 
будет обходить прокси (proxy bypass).
- Если метод, помеченный @Async должен что-то возвращать, то его возвращаемый тип необходимо определить как 
CompletableFuture или Future:
```java
@Async
public CompletableFuture<SomeDomainClass> getObjectById(final Long id) throws InterruptedException {
    SomeDomainClass someDomainObject = new SomeDomainClass();
    // ...
    return CompletableFuture.completedFuture(someDomainObject);
}
```
## Task Executor (исполнитель задач)
Spring использует пул потоков для управления потоками фоновых процессов. В свою очередь, для управления и конфигурации 
пула потоков Spring использует Исполнитель задач - бин TaskExecutor.

По умолчанию аннотация @EnableAsync создает SimpleAsyncTaskExecutor.

К сожалению, эта реализация не имеет фактического верхнего предела для размера пула потоков. Это означает, что 
приложение Spring может упасть (crash), если одновременно будет запущено слишком много методов с аннотацией @Async.

Чтобы избежать этого, нам необходимо переопределить собственный исполнитель задач.

Вернемся к нашему конфигурационному классу и добавим в него определение Исполнителя задач:
```java
@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport { 
    @Override 
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncTaskThread::");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
  
  // ...
}
```
## Exception handle (обработка ошибок)
Для реализации обработки ошибок в асинхронных методах необходимо учитывать следующие моменты:
- Если метод возвращает CompletableFuture или Future, то вызов в нем Future.get() при наличии ошибок приведет к тому, 
что исключение (exception) будет выдано.
- Во всех остальных случаях (в особенности если метод ничего не возвращает – void), нам необходимо явно указать 
обработчик ошибок, так как по умолчанию исключения не будут передаваться вызывающему потоку.

Асинхронный обработчик ошибок должен реализовывать интерфейс AsyncUncaughtExceptionHandler.

Вновь возвращаемся к конфигурационному классу:
```java
@Configuration
@EnableAsync
public class AsyncConfiguration extends AsyncConfigurerSupport { 
    @Override 
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncTaskThread::");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
    
    @Override 
    public AsyncUncaughtExceptionHandler  getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override 
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                // Используйте здесь ваш любимый Logger
                // Задавайте удобный для вас формат логирования
                
                // Здесь для простоты примера использован метод System.out.println
                System.out.println("Exception: " + ex.getMessage());
                System.out.println("Method Name: " + method.getName());
                ex.printStackTrace();
        }
    };
  }}
```
