# Guide to Future
## Оглавление
- [Описание](#описание)
- [Создание Future](#создание-future)
- [Потребление Future](#потребление-future)
- [Больше многопоточки с пулом потоков](#больше-многопоточки-с-пулом-потоков)
- [Обзор ForkJoinTask](#обзор-forkjointask)
## Описание
Этот интерфейс, появившийся еще в Java 1.5, может быть весьма полезен при работе с асинхронными вызовами и одновременной
обработкой.
## Создание Future
Проще говоря, класс Future представляет будущий результат асинхронного вычисления. Этот результат в конечном итоге 
появится в Future после завершения обработки.

Давайте посмотрим, как писать методы, которые создают и возвращают экземпляр Future.

Длительно работающие методы - хорошие кандидаты для асинхронной обработки и интерфейса Future, потому что мы можем 
выполнять другие процессы, пока ждем завершения задачи, заключенной в Future.

Примерами операций, в которых можно использовать асинхронную природу Future, являются:
- интенсивные вычислительные процессы (математические и научные расчеты)
- манипулирование большими структурами данных (большие данные)
- удаленные вызовы методов (загрузка файлов, HTML-скраппинг, веб-сервисы)

Для нашего примера мы создадим очень простой класс, который вычисляет квадрат целого числа. Он определенно не относится
к категории долго выполняющихся методов, но мы поместим в него вызов Thread.sleep(), чтобы он проработал 1 секунду
до завершения:
```java
public class SquareCalculator {    
    
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    public Future<Integer> calculate(Integer input) {        
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }
}
```
Часть кода, которая фактически выполняет вычисления, содержится в методе call() и представлена в виде лямбда-выражения. 
Как мы видим, в нем нет ничего особенного, за исключением упомянутого ранее вызова sleep().

Все становится интереснее, когда мы обращаем внимание на использование Callable и ExecutorService.

Callable - это интерфейс, представляющий задачу, которая возвращает результат, и имеющий единственный метод call(). 
Здесь мы создали его экземпляр с помощью лямбда-выражения.

Создание экземпляра Callable никуда не ведет: нам все равно нужно передать этот экземпляр исполнителю, который 
позаботится о запуске задачи в новом потоке и вернет нам ценный объект Future. Вот тут-то и приходит на 
помощь ExecutorService.

Есть несколько способов получить доступ к экземпляру ExecutorService, и большинство из них предоставляются статическими
фабричными методами утилитарного класса Executors. В этом примере мы использовали базовый метод 
newSingleThreadExecutor(), который дает нам ExecutorService, способный обрабатывать один поток за раз.

Когда у нас есть объект ExecutorService, нам нужно просто вызвать submit(), передав в качестве аргумента наш 
Callable. Затем submit() запустит задачу и вернет объект FutureTask, который является реализацией интерфейса Future.
## Потребление Future
До этого момента мы узнали, как создать экземпляр Future.

В этом разделе мы научимся работать с этим экземпляром, изучив все методы, входящие в состав API Future.

Теперь нам нужно вызвать calculate() и использовать возвращаемое Future для получения результирующего Integer. Два
метода из Future API помогут нам справиться с этой задачей.

Future.isDone() сообщает нам, закончил ли исполнитель обработку задачи. Если задача выполнена, то возвращается true, в
противном случае - false.

Метод, возвращающий фактический результат вычислений, - Future.get(). Мы видим, что этот метод блокирует выполнение
до тех пор, пока задача не будет завершена. Однако в нашем примере это не будет проблемой, потому что мы проверим,
завершена ли задача, вызвав isDone().

Используя эти два метода, мы можем запускать другой код, пока ждем завершения основной задачи:
```java
Future<Integer> future = new SquareCalculator().calculate(10);

while(!future.isDone()) {
    System.out.println("Calculating...");
    Thread.sleep(300);
}

Integer result = future.get();
```
В этом примере мы напишем на выходе простое сообщение, чтобы пользователь знал, что программа выполняет вычисления.

Метод get() будет блокировать выполнение до тех пор, пока задача не будет завершена. Опять же, это не будет проблемой, 
потому что в нашем примере get() будет вызван только после того, как мы убедимся, что задача завершена. Поэтому в 
данном сценарии future.get() всегда будет возвращаться немедленно.

Стоит отметить, что у get() есть перегруженная версия, которая принимает в качестве аргументов таймаут и TimeUnit:
```java
Integer result = future.get(500, TimeUnit.MILLISECONDS);
```
Разница между get(long, TimeUnit) и get() заключается в том, что в первом случае будет выброшен TimeoutException, 
если задача не вернется до истечения указанного периода времени.

Предположим, мы запустили задачу, но по какой-то причине результат нас больше не интересует. Мы можем использовать 
Future.cancel(boolean), чтобы сообщить исполнителю о прекращении операции и прервать его базовый поток:
```java
Future<Integer> future = new SquareCalculator().calculate(4);
boolean canceled = future.cancel(true);
```
Наш экземпляр Future из приведенного выше кода никогда не завершит свою работу. На самом деле, если мы попытаемся
вызвать get() из этого экземпляра после вызова cancel(), результатом будет CancellationException. Future.isCancelled() 
сообщит нам, было ли будущее уже отменено. Это может быть очень полезно, чтобы избежать получения CancellationException.

Также возможно, что вызов cancel() не удался. В этом случае возвращаемое значение будет равно false. Важно отметить, 
что cancel() принимает в качестве аргумента булево значение. Оно определяет, должен ли поток, выполняющий задачу,
быть прерван или нет.
## Больше многопоточки с пулом потоков
Наш текущий ExecutorService является однопоточным, поскольку он был получен с помощью Executors.newSingleThreadExecutor.
Чтобы выделить этот единственный поток, давайте запустим два вычисления одновременно:
```java
SquareCalculator squareCalculator = new SquareCalculator();

Future<Integer> future1 = squareCalculator.calculate(10);
Future<Integer> future2 = squareCalculator.calculate(100);

while (!(future1.isDone() && future2.isDone())) {
    System.out.println(
      String.format(
        "future1 is %s and future2 is %s", 
        future1.isDone() ? "done" : "not done", 
        future2.isDone() ? "done" : "not done"
      )
    );
    Thread.sleep(300);
}

Integer result1 = future1.get();
Integer result2 = future2.get();

System.out.println(result1 + " and " + result2);

squareCalculator.shutdown();
```
Теперь давайте проанализируем вывод этого кода:
```
calculating square for: 10
future1 is not done and future2 is not done
future1 is not done and future2 is not done
future1 is not done and future2 is not done
future1 is not done and future2 is not done
calculating square for: 100
future1 is done and future2 is not done
future1 is done and future2 is not done
future1 is done and future2 is not done
100 and 10000
```
Очевидно, что процесс не является параллельным. Мы видим, что вторая задача запускается только после завершения первой,
в результате чего весь процесс занимает около 2 секунд.

Чтобы сделать нашу программу действительно многопоточной, нам следует использовать другой тип ExecutorService. Давайте 
посмотрим, как изменится поведение нашего примера, если мы будем использовать пул потоков, предоставляемый фабричным 
методом Executors.newFixedThreadPool():
```java
public class SquareCalculator {
 
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    
    //...
}
```
С помощью простого изменения в классе SquareCalculator у нас теперь есть исполнитель, который может использовать 2
одновременных потока.

Если мы снова запустим точно такой же клиентский код, то получим следующий результат:
```
calculating square for: 10
calculating square for: 100
future1 is not done and future2 is not done
future1 is not done and future2 is not done
future1 is not done and future2 is not done
future1 is not done and future2 is not done
100 and 10000
```
Теперь все выглядит гораздо лучше. Мы видим, что две задачи запускаются и завершаются одновременно, а весь процесс 
занимает около 1 секунды.

Существуют и другие фабричные методы, которые можно использовать для создания пулов потоков, например 
Executors.newCachedThreadPool(), который повторно использует ранее использованные потоки, когда они доступны, и 
Executors.newScheduledThreadPool(), который планирует выполнение команд после заданной задержки.
## Обзор ForkJoinTask
ForkJoinTask - это абстрактный класс, реализующий Future и способный выполнять большое количество задач, размещенных в 
небольшом количестве реальных потоков в ForkJoinPool.

В этом разделе мы быстро рассмотрим основные характеристики ForkJoinPool.

Главная особенность ForkJoinTask заключается в том, что она обычно порождает новые подзадачи в рамках работы, 
необходимой для выполнения ее основной задачи. Она порождает новые задачи, вызывая fork(), и собирает все результаты 
с помощью join(), отсюда и название класса.

Существует два абстрактных класса, реализующих ForkJoinTask: RecursiveTask, который возвращает значение по завершении,
и RecursiveAction, который ничего не возвращает. Как следует из их названий, эти классы предназначены для выполнения
рекурсивных задач, таких как навигация по файловой системе или сложные математические вычисления.

Давайте расширим наш предыдущий пример и создадим класс, который, получив Integer, будет вычислять квадраты сумм для 
всех его факториальных элементов. Так, например, если мы передадим в калькулятор число 4, то получим результат от 
суммы 4² + 3² + 2² + 1², которая равна 30.

Сначала нам нужно создать конкретную реализацию RecursiveTask и реализовать ее метод compute(). Именно в нем мы напишем
нашу бизнес-логику:
```java
public class FactorialSquareCalculator extends RecursiveTask<Integer> {
    private Integer n;

    public FactorialSquareCalculator(Integer n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }

        FactorialSquareCalculator calculator 
          = new FactorialSquareCalculator(n - 1);

        calculator.fork();

        return n * n + calculator.join();
    }
}
```
Обратите внимание, как мы добиваемся рекурсивности, создавая новый экземпляр FactorialSquareCalculator внутри compute(). 
Вызвав неблокирующий метод fork(), мы просим ForkJoinPool инициировать выполнение этой подзадачи.

Метод join() вернет результат вычислений, к которому мы добавим квадрат числа, к которому мы сейчас обращаемся.

Теперь нам осталось создать ForkJoinPool для выполнения и управления потоками:
```java
ForkJoinPool forkJoinPool = new ForkJoinPool();

FactorialSquareCalculator calculator = new FactorialSquareCalculator(10);

forkJoinPool.execute(calculator);
```
