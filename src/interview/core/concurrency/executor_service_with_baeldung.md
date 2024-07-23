# Guide to the ExecutorService
## Оглавление
- [Описание](#описание)
- [Создание ExecutorService](#создание-executorservice)
- [Обработка задач ExecutorService](#обработка-задач-executorservice)
- [Закрытие ExecutorService](#закрытие-executorservice)
- [Интерфейс Future](#интерфейс-future)
- [Интерфейс ScheduledExecutorService](#интерфейс-scheduledexecutorservice)
- [ExecutorService vs Fork/Join](#executorservice-vs-forkjoin)
## Описание
ExecutorService - это JDK API, который упрощает выполнение задач в асинхронном режиме. В общем случае ExecutorService 
автоматически предоставляет пул потоков и API для назначения им задач.
## Создание ExecutorService
### Фабричные методы класса Executors
Самый простой способ создать ExecutorService - использовать один из фабричных методов класса Executors.

Например, следующая строка кода создаст пул потоков с 10 потоками:

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
```
Существует несколько других фабричных методов для создания предопределенного ExecutorService, отвечающего конкретным
условиям использования. Чтобы найти лучший метод для ваших нужд, обратитесь к официальной документации Oracle.
### Непосредственное создание ExecutorService
Поскольку ExecutorService - это интерфейс, можно использовать экземпляр любой его реализации. В пакете 
java.util.concurrent есть несколько реализаций на выбор, или вы можете создать свою собственную.

Например, класс ThreadPoolExecutor имеет несколько конструкторов, которые мы можем использовать для настройки 
службы-исполнителя и ее внутреннего пула:
```java
ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, 
        new LinkedBlockingQueue<Runnable>());
```
Вы можете заметить, что приведенный выше код очень похож на исходный код фабричного метода newSingleThreadExecutor(). 
В большинстве случаев подробная ручная настройка не требуется.
## Обработка задач ExecutorService
ExecutorService может выполнять задачи Runnable и Callable. Для упрощения в этой статье будут использоваться две 
примитивные задачи. Обратите внимание, что вместо анонимных внутренних классов мы используем лямбда-выражения:
```java
Runnable runnableTask = () -> {
    try {
        TimeUnit.MILLISECONDS.sleep(300);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
};

Callable<String> callableTask = () -> {
    TimeUnit.MILLISECONDS.sleep(300);
    return "Task's execution";
};

List<Callable<String>> callableTasks = new ArrayList<>();
callableTasks.add(callableTask);
callableTasks.add(callableTask);
callableTasks.add(callableTask);
```
Мы можем назначать задачи сервису ExecutorService с помощью нескольких методов, включая execute(), который наследуется
от интерфейса Executor, а также submit(), invokeAny() и invokeAll().

Метод execute() является недействительным и не дает возможности получить результат выполнения задачи или проверить ее 
статус (запущена ли она):
```java
executorService.execute(runnableTask);
```
submit() отправляет задачу Callable или Runnable в службу ExecutorService и возвращает результат типа Future:
```java
Future<String> future = executorService.submit(callableTask);
```
invokeAny() назначает коллекцию задач сервису ExecutorService, вызывая выполнение каждой из них, и возвращает результат 
успешного выполнения одной задачи (если она была успешно выполнена):
```java
String result = executorService.invokeAny(callableTasks);
```
invokeAll() назначает коллекцию задач сервису ExecutorService, вызывая выполнение каждой из них, и возвращает результат
выполнения всех задач в виде списка объектов типа Future:
```java
List<Future<String>> futures = executorService.invokeAll(callableTasks);
```
## Закрытие ExecutorService
В общем случае ExecutorService не будет автоматически уничтожен, если нет задачи для обработки. Он останется в живых и 
будет ждать новой работы.

В некоторых случаях это очень полезно, например, когда приложению нужно обрабатывать задачи, которые появляются 
нерегулярно или количество задач не известно на момент компиляции.

С другой стороны, приложение может дойти до конца, но не быть остановленным, потому что ожидающий ExecutorService 
заставит JVM продолжать работать.

Чтобы правильно завершить работу ExecutorService, у нас есть API shutdown() и shutdownNow().

Метод shutdown() не приводит к немедленному уничтожению ExecutorService. Он заставит ExecutorService перестать 
принимать новые задания и завершится после того, как все запущенные потоки закончат свою текущую работу:
```java
executorService.shutdown();
```
Метод shutdownNow() пытается уничтожить ExecutorService немедленно, но это не гарантирует, что все запущенные потоки 
будут остановлены одновременно:
```java
List<Runnable> notExecutedTasks = executorService.shutDownNow();
```
Этот метод возвращает список задач, ожидающих обработки. Разработчик сам решает, что делать с этими задачами.

Одним из хороших способов завершения работы ExecutorService (который также рекомендуется Oracle) является использование
обоих этих методов в сочетании с методом awaitTermination():
```java
executorService.shutdown();
try {
    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
        executorService.shutdownNow();
    } 
} catch (InterruptedException e) {
    executorService.shutdownNow();
}
```
При таком подходе служба ExecutorService сначала прекращает принимать новые задания, а затем ждет в течение 
определенного периода времени, пока все задания не будут завершены. Если это время истечет, выполнение будет 
немедленно остановлено.
## Интерфейс Future
Методы submit() и invokeAll() возвращают объект или коллекцию объектов типа Future, что позволяет нам получить 
результат выполнения задачи или проверить ее состояние (запущена ли она).

Интерфейс Future предоставляет специальный блокирующий метод get(), который возвращает фактический результат выполнения 
задачи Callable или null в случае задачи Runnable:
```java
Future<String> future = executorService.submit(callableTask);
String result = null;
try {
    result = future.get();
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```
Вызов метода get() во время выполнения задачи приведет к блокировке выполнения до тех пор, пока задача не завершится 
должным образом и не будет получен результат.

При очень длительной блокировке, вызванной методом get(), производительность приложения может снизиться. Если 
получаемые данные не являются критически важными, можно избежать такой проблемы, используя таймауты:
```java
String result = future.get(200, TimeUnit.MILLISECONDS);
```
Если время выполнения превышает указанное (в данном случае 200 миллисекунд), будет выброшено исключение 
TimeoutException.

Мы можем использовать метод isDone(), чтобы проверить, обработана ли уже назначенная задача или нет.

Интерфейс Future также позволяет отменить выполнение задачи с помощью метода cancel() и проверить отмену с помощью 
метода isCancelled():
```java
boolean canceled = future.cancel(true);
boolean isCancelled = future.isCancelled();
```
## Интерфейс ScheduledExecutorService
Служба ScheduledExecutorService запускает задания после некоторой заданной задержки и/или периодически.

И снова, лучший способ создать ScheduledExecutorService - это использовать фабричные методы класса Executors.

В этом разделе мы используем ScheduledExecutorService с одним потоком:
```java
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
```
Чтобы запланировать выполнение одной задачи с фиксированной задержкой, используйте метод scheduled() службы 
ScheduledExecutorService.

Два метода scheduled() позволяют выполнять задачи Runnable или Callable:
```java
Future<String> resultFuture = executorService.schedule(callableTask, 1, TimeUnit.SECONDS);
```
Метод scheduleAtFixedRate() позволяет периодически запускать задачу с фиксированной задержкой. В приведенном выше коде 
задержка перед выполнением callableTask составляет одну секунду.

Следующий блок кода запустит задачу после начальной задержки в 100 миллисекунд. И после этого он будет запускать ту же 
задачу каждые 450 миллисекунд:
```java
executorService.scheduleAtFixedRate(runnableTask, 100, 450, TimeUnit.MILLISECONDS);
```
Если процессору требуется больше времени для выполнения назначенной задачи, чем параметр period метода 
scheduleAtFixedRate(), служба ScheduledExecutorService будет ждать завершения текущей задачи перед запуском следующей.

Если необходимо, чтобы между итерациями задачи была задержка фиксированной длины, следует использовать 
метод scheduleWithFixedDelay().

Например, следующий код гарантирует 150-миллисекундную паузу между завершением текущего выполнения и началом другого:
```java
executorService.scheduleWithFixedDelay(task, 100, 150, TimeUnit.MILLISECONDS);
```
Согласно контрактам методов scheduleAtFixedRate() и scheduleWithFixedDelay(), периодическое выполнение задачи закончится 
при завершении работы ExecutorService или если во время выполнения задачи возникнет исключение.
## ExecutorService vs Fork/Join
После выхода Java 7 многие разработчики решили заменить фреймворк ExecutorService на фреймворк fork/join.

Однако это не всегда правильное решение. Несмотря на простоту и частый прирост производительности, связанный с 
fork/join, он снижает контроль разработчика над параллельным выполнением.

ExecutorService дает разработчику возможность контролировать количество генерируемых потоков и гранулярность задач, 
которые должны выполняться отдельными потоками. Лучший вариант использования ExecutorService - обработка независимых 
задач, таких как транзакции или запросы, по схеме «один поток для одной задачи».

В отличие от этого, согласно документации Oracle, fork/join был разработан для ускорения работы, которая может быть
разбита на более мелкие части рекурсивно.
