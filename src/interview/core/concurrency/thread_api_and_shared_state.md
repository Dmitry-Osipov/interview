# Thread API
## Оглавление
- [Запуск параллельных вычислений](#запуск-параллельных-вычислений-в-java)
- [Проблемы с shared state (совместно используемого состояния)](#проблемы-с-shared-state-совместно-используемого-состояния)
- [Промежуточные выводы проблем параллелизма](#промежуточные-выводы-проблем-параллелизма)
- [Ключевое слово volatile](#ключевое-слово-volatile)
- [Final поля](#final-поля)
- [Неатомарные операции](#неатомарные-операции-final-не-подходит-volatile-не-спасёт)
- [Блокировки](#блокировки)
- [JMM Monitor Lock Rule](#jmm-monitor-lock-rule)
- [Condition Objects](#condition-objects)
- [Intrinsic Lock](#intrinsic-lock)
- [Промежуточный итог по intrinsic lock](#промежуточный-итог-по-intrinsic-lock)
- [Состояния Thread](#состояния-thread)
- [Итог](#итог)
## Запуск параллельных вычислений в Java
Для работы с многопоточностью в Java на низком уровне есть класс Thread и интерфейс Runnable. Единственный метод у 
Runnable void run() - выполняет некоторую логику. Основные методы Thread: 
- void start() - планирует начало выполнения этого потока. Поток будет выполняться независимо от текущего потока. 
Поток может быть запущен не более одного раза. В частности, поток не может быть перезапущен после его завершения.
- void join(long millis) throws InterruptedException -  Ждет не более N миллисекунд, пока этот поток не завершится.
- void interrupt() - прерывает данный поток.
- Thread.state getState() - Возвращает состояние данного потока. Этот метод предназначен для мониторинга состояния 
системы, а не для управления синхронизацией.

Пример создания своего кастомного потока:
```java
class CalcSquare extends Thread {
    final int argument;
    int result;
    
    CalcSquare(int argument) {
        this.argument = argument;
    }
    
    @Override
    public void run() {
        // Сложные вычисления
        result = argument * argument;
    }
}
```
Запуск параллельных вычислений через thread API:
```java
CalcSquare t1 = new CalcSquare(2);
CalcSquare t2 = new CalcSquare(3);
t1.start();
t2.start();
t1.join();
t2.join();
System.out.printf("%d, %d%n", t1.result, t2.result);  // 4, 9
```
## Проблемы с shared state (совместно используемого состояния)
- Race condition (гонка):
```java
class DumbCounter {
  int count;
  void increment(){
    count++;
  }
}

DumbCounter c1 = new DumbCounter();
IntStream.range(0, 1000000).forEach(i->c1.increment());

DumbCounter c2 = new DumbCounter();
IntStream.range(0, 1000000).parallel().forEach(i->c2.increment());

System.out.printf("%d, %d%n", c1.count, c2.count);  //1000000,??????
```
- Stale values (устаревшие значения:
```java
class DumbWayToFallAsleep implements Runnable {
  private boolean asleep;

  public void setAsleep(boolean asleep){
    this.asleep = asleep;
  }

  @Override
  public void run() {
    while (!asleep){
      //countSomeSheep
      //никогда не уснём, ибо jvm поставит в байт-код while(true)
    }
  }
}
```
- Reordering (перестановка):
```java
class PossibleReordering {
  static int x = 0, y = 0, a = 0, b = 0;
  public static void main(String... args)
                throws InterruptedException {
    Thread one = new Thread(() -> {
        a = 1; x = b;
    });
    Thread two = new Thread(() -> {
        b = 1; y = a;
    });
    one.start(); two.start();
    one.join();  two.join();
    System.out.printf("%d,%d", x, y);  //??,??
  }
}
```
## Промежуточные выводы проблем параллелизма
- Из-за reordering и других низкоуровневых особенностей _нельзя_ рассуждать о результате работы одного треда с точки 
зрения другого треда как о промежуточном результате выполнения исходного кода
- Все проблемы с параллельными вычислениями связаны с shared state (совместно используемым состоянием)
- Показанные здесь проблемы проявляются недетерминированно
- Любая программа с доступом к shared state (совместным состоянием) без должной синхронизации - _сломана_, даже если 
"вчера это работало на моей машине"
## Ключевое слово volatile
- Переменные классы могут быть определены с ключевым словом volatile
- Запись в volatile-переменную happens-before чтения из этой переменной в другом потоке
- Автоматически делает видимыми значения в других переменных. Полагаться на это не рекомендуется: это работает, но 
делает код хрупким. В процессе рефакторинга можно поменять порядок доступа к переменным и тем самым незаметно поломать 
программу
```java
class NotSoDumbWayToFallAsleep implements Runnable {
  private volatile boolean asleep;

  public void setAsleep(boolean asleep){
    this.asleep = asleep;
  }

  @Override
  public void run() {
    while (!asleep){
      //countSomeSheep
      //теперь у нас не будет цикла while(true)
    }
  }
}
```
## Final поля
- Если объект правильно опубликован, т.е. ссылка на него не утекает во время выполнения конструктора - final-поля 
объекта доступны всем тредам без синхронизации
- Лучший способ борьбы с проблемами mutable state - использовать immutable state где только возможно
## Неатомарные операции: final не подходит, volatile не спасёт
```java
class DumbCounter {
    int count;
    void increment() {
        count++;
    }
}
```
Мы можем объявить count как volatile, но ситуация не улучшится по производительности
```java
void dumbMoneyTransfer(int from, int to, int amount) {
    account[from] -= amount;
    account[to] += amount;
}
```
volatile array - это не массив volatile-элементов. Просто так нельзя создать массив volatile-элементов
## Блокировки
```java
//Reentrant так называется потому,
//что одному и тому же треду позволено входить повторно
private ReentrantLock bankLock = new ReentrantLock();

void moneyTransfer(int from, int to, int amount) {
  bankLock.lock();
  try {
    accounts[from]-=amount;
    accounts[to]+=amount;
  } finally {
    bankLock.unlock();
  }
}
```
## JMM Monitor Lock Rule
- Разблокировка (unlocking) happens-before другой блокировки (locking) того же самого "замка" (lock)
- Поэтому защищённые блокировкой переменные объявлять как volatile уже не нужно
## Condition Objects
```java
private ReentrantLock bankLock = new ReentrantLock();
private Condition sufficientFunds = bankLock.newCondition();

void moneyTransfer(int from, int to, int amount) {
  bankLock.lock();
  try {
    while (accounts[from] < amount)
      sufficientFunds.await();  // позволяет другим потокам зайти и положить деньги

    accounts[from]-=amount;
    accounts[to]+=amount;

    sufficientFunds.signalAll();  // заставляет проснуться другие потоки и проверить счёт
  } finally {
    bankLock.unlock();
  }
}
```
- await() отпускает блокировку и переводит тред в ждущее состояние
- signalAll() сигнализирует всем ждущим тредам, что что-то поменялось
- выход из await() снова захватывает блокировку
- При выходе из await() мы вновь проверяем условие, потому что
  - сигнал мог быть по-другому поводу
  - возможны "спонтанные пробуждения"

Чем гарантировано, что при выходе из await() мы увидим изменения, сделанные другим тредом? При выходе из await() мы 
снова захватываем блокировку, работает JMM Monitor Lock Rule
## Intrinsic Lock
- Начиная с Java 1.0, каждый объект имеет встроенный (intrinsic) lock
- У каждого Intrinsic lock есть 1 condition

Т.е. всё, что мы писали выше, можно переписать с помощью intrinsic lock:
```java
//enter intrinsic lock on *this*
synchronized void moneyTransfer(int from, int to, int amount) {
    while (accounts[from] < amount)
      wait(); //wait on intrinsic object's lock condition

    accounts[from]-=amount;
    accounts[to]+=amount;

    notifyAll(); //notify all threads waiting on the condition
}
```
Есть также и другая форма использования intrinsic lock:
```java
private Object lock = new Object();
void moneyTransfer(int from, int to, int amount) {
  synchronized (lock) {
    while (accounts[from] < amount)
      lock.wait();

    accounts[from]-=amount;
    accounts[to]+=amount;

    lock.notifyAll();
  }
}
```
## Промежуточный итог по intrinsic lock
- Нужно работать по строгому паттерну:
  - синхронизация
  - while-loop wait
  - нотификация
- Нужно держать в уме:
  - по intrinsic lock каждого объекта синхронизируемся
  - по condition какого объекта ждём
  - треды, ждущие на condition какого объекта нотифицируем (это всё должен быть один объект)
- Это низкоуровневый и сложный механизм
## Состояния Thread
- new 
  - new -> runnable через запуск
- runnable
  - runnable -> blocked через приобретение замка
  - runnable -> waiting через ожидание нотификации
  - runnable -> time waiting через ожидание timeout или нотификацию
  - runnable -> terminated через run или method exit
- blocked
  - blocked -> runnable через приобретённый замок
- waiting
  - waiting -> runnable через полученное уведомление
- time waiting
  - time waiting -> runnable через timeout или полученное уведомление
- terminated
## Итог
- Где возможно, использоваться immutable state: он автоматически потокобезопасен
- Использовать volatile переменные или синхронизацию для доступа к mutable state
- Удерживать блокировку во время выполнения операций, которые должны быть атомарными
- Программа с Shared Mutable State без должной синхронизации - сломанная программа
- Думать о потокобезопасности всё время
- Понимание JMM помогает