# Guide to the Fork/Join
## Оглавление
- [Описание](#описание)
- [ForkJoinPool](#forkjoinpool)
- [Work-Stealing Algorithm](#work-stealing-algorithm)
- [Создание ForkJoinPool](#создание-forkjoinpool)
- [ForkJoinTask](#forkjointask)
- [RecursiveAction](#recursiveaction)
- [RecursiveTask](#recursivetask)
- [Отправка заданий в ForkJoinPool](#отправка-заданий-в-forkjoinpool)
## Описание
В Java 7 появился фреймворк fork/join. Он предоставляет инструменты для ускорения параллельной обработки, пытаясь 
использовать все доступные ядра процессора. Это достигается за счет подхода «разделяй и властвуй».

На практике это означает, что фреймворк сначала «форкает», рекурсивно разбивая задачу на более мелкие независимые 
подзадачи, пока они не станут достаточно простыми для асинхронного выполнения.

После этого начинается «объединение». Результаты всех подзадач рекурсивно объединяются в один результат. В случае 
задачи, которая возвращает пустоту, программа просто ждет, пока все подзадачи не выполнятся.

Чтобы обеспечить эффективное параллельное выполнение, фреймворк fork/join использует пул потоков, называемый 
ForkJoinPool. Этот пул управляет рабочими потоками типа ForkJoinWorkerThread.
## ForkJoinPool
ForkJoinPool - это сердце фреймворка. Это реализация ExecutorService, которая управляет рабочими потоками и 
предоставляет нам инструменты для получения информации о состоянии и производительности пула потоков.

Рабочие потоки могут выполнять только одну задачу за раз, но ForkJoinPool не создает отдельный поток для каждой 
подзадачи. Вместо этого каждый поток в пуле имеет свою собственную двустороннюю очередь, в которой хранятся задания.

Такая архитектура необходима для балансировки нагрузки на поток с помощью алгоритма перехвата работы.
## Work-Stealing Algorithm
Проще говоря, свободные потоки пытаются «украсть» работу из deques занятых потоков.

По умолчанию рабочий поток получает задания из головы своего собственного deque. Если она пуста, поток берет задание из 
хвоста deque другого занятого потока или из глобальной очереди на вход, поскольку именно там, скорее всего, находятся 
самые большие куски работы.

Такой подход минимизирует вероятность того, что потоки будут конкурировать за задания. Он также уменьшает количество 
раз, когда потоку приходится искать работу, поскольку он сначала работает с самыми большими доступными кусками работы.
## Создание ForkJoinPool
В Java 8 наиболее удобным способом получить доступ к экземпляру пула ForkJoinPool является использование его
статического метода commonPool(). Это позволит получить ссылку на общий пул, который является пулом потоков по 
умолчанию для каждой ForkJoinTask.

Согласно документации Oracle, использование предопределенного общего пула снижает потребление ресурсов, поскольку это
препятствует созданию отдельного пула потоков для каждой задачи.
```java
ForkJoinPool commonPool = ForkJoinPool.commonPool();
```
Мы можем добиться такого же поведения в Java 7, создав ForkJoinPool и присвоив его публичному статическому полю класса 
утилиты:
```java
public static ForkJoinPool forkJoinPool = new ForkJoinPool(2);
```
Теперь мы можем легко получить к нему доступ:
```java
ForkJoinPool forkJoinPool = PoolUtil.forkJoinPool;
```
С помощью конструкторов ForkJoinPool мы можем создать пользовательский пул потоков с определенным уровнем параллелизма,
фабрикой потоков и обработчиком исключений. Здесь пул имеет уровень параллелизма 2. Это означает, что пул будет
использовать два процессорных ядра.
## ForkJoinTask
ForkJoinTask - это базовый тип для задач, выполняемых внутри ForkJoinPool. На практике следует расширить один из двух 
его подклассов: RecursiveAction для задач с void и RecursiveTask<V> для задач, возвращающих значение. Оба они имеют 
абстрактный метод compute(), в котором определяется логика задачи.
## RecursiveAction
В приведенном ниже примере мы используем строку String под названием workload для представления единицы работы, которую 
нужно обработать. В демонстрационных целях задача является бессмысленной: Она просто переводит входные данные в верхний 
регистр и записывает их в журнал.

Чтобы продемонстрировать поведение фреймворка при создании подзадач, пример разделяет задачу, если workload.length() 
больше заданного порога, используя метод createSubtask().

Строка рекурсивно делится на подстроки, создавая экземпляры CustomRecursiveTask, основанные на этих подстроках.

В результате метод возвращает список List<CustomRecursiveAction>.

Этот список передается в ForkJoinPool с помощью метода invokeAll():
```java
public class CustomRecursiveAction extends RecursiveAction {
    private String workload = "";
    private static final int THRESHOLD = 4;

    private static Logger logger = 
      Logger.getAnonymousLogger();

    public CustomRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
           processing(workload);
        }
    }

    private List<CustomRecursiveAction> createSubtasks() {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2, workload.length());

        subtasks.add(new CustomRecursiveAction(partOne));
        subtasks.add(new CustomRecursiveAction(partTwo));

        return subtasks;
    }

    private void processing(String work) {
        String result = work.toUpperCase();
        logger.info("This result - (" + result + ") - was processed by " 
          + Thread.currentThread().getName());
    }
}
```
Мы можем использовать этот паттерн для разработки собственных классов RecursiveAction. Для этого мы создаем объект,
представляющий общий объем работы, выбираем подходящий порог, определяем метод для разделения работы и определяем 
метод для выполнения работы.
## RecursiveTask
Для задач, возвращающих значение, логика аналогична. Разница в том, что результат для каждой подзадачи объединяется в 
единый результат:
```java
public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] arr;
    private static final int THRESHOLD = 20;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
              .stream()
              .mapToInt(ForkJoinTask::join)
              .sum();
        } else {
            return processing(arr);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(
          Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new CustomRecursiveTask(
          Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
          .filter(a -> a > 10 && a < 27)
          .map(a -> a * 10)
          .sum();
    }
}
```
В этом примере для представления работы мы используем массив, хранящийся в поле arr класса CustomRecursiveTask. 
Метод createSubtasks() рекурсивно делит задачу на более мелкие части работы, пока каждая часть не станет меньше 
порогового значения. Затем метод invokeAll() отправляет подзадачи в общий пул и возвращает список Future.

Чтобы запустить выполнение, для каждой подзадачи вызывается метод join().

Для этого мы использовали API Stream в Java 8. Мы используем метод sum() для объединения результатов подзадач в 
конечный результат.
## Отправка заданий в ForkJoinPool
Мы можем использовать несколько подходов для отправки задач в пул потоков.

Начнем с метода submit() или execute() (их использование одинаково):
```java
forkJoinPool.execute(customRecursiveTask);
int result = customRecursiveTask.join();
```
Метод invoke() переадресует задачу и ожидает результата, при этом не требуется никакого ручного присоединения:
```java
int result = forkJoinPool.invoke(customRecursiveTask);
```
Метод invokeAll() - это самый удобный способ отправить последовательность ForkJoinTasks в ForkJoinPool. Он принимает 
задачи в качестве параметров (две задачи, var args или коллекцию), выполняет форки, а затем возвращает коллекцию 
объектов Future в том порядке, в котором они были созданы.

В качестве альтернативы мы можем использовать отдельные методы fork() и join(). Метод fork() отправляет задачу в пул,
но не запускает ее выполнение. Для этого необходимо использовать метод join().

В случае RecursiveAction метод join() не возвращает ничего, кроме null; для RecursiveTask<V> он возвращает результат
выполнения задачи:
```java
customRecursiveTaskFirst.fork();
result = customRecursiveTaskLast.join();
```
Здесь мы использовали метод invokeAll() для отправки последовательности подзадач в пул. Мы можем проделать ту же работу 
с помощью fork() и join(), хотя это имеет последствия для упорядочивания результатов.

Чтобы избежать путаницы, обычно лучше использовать метод invokeAll() для отправки в пул ForkJoinPool более одной задачи.
