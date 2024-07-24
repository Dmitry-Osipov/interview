# Guide to java.util.concurrent.Locks
## Оглавление
- [Описание](#описание)
- [Разница между Lock и блоком synchronized](#разница-между-lock-и-блоком-synchronized)
- [Lock API](#lock-api)
- [Реализации Lock](#реализации-lock)
- [Работа с Conditions](#работа-с-conditions)
## Описание
Lock - это более гибкий и сложный механизм синхронизации потоков, чем стандартный синхронизированный блок.

Интерфейс Lock существует со времен Java 1.5. Он определен внутри пакета java.util.concurrent.lock и предоставляет 
обширные операции для блокировки.
## Разница между Lock и блоком synchronized
Есть несколько различий между использованием synchronized блока и использованием API Lock:
- Synchronized блок полностью содержится в методе. Операции Lock APIs lock() и unlock() могут выполняться в 
отдельных методах.
- Synchronized блок не поддерживает справедливость. Любой поток может получить блокировку после освобождения, и нельзя 
указать предпочтение. Мы можем добиться справедливости в API Lock, указав свойство fairness. Оно гарантирует, что
доступ к блокировке получит самый долгожданный поток.
- Поток блокируется, если он не может получить доступ к синхронизированному блоку. API Lock предоставляет метод 
tryLock(). Поток получает блокировку только в том случае, если она доступна и не удерживается другим потоком. Это
сокращает время блокировки потока, ожидающего блокировку.
- Поток, находящийся в состоянии «ожидания» получения доступа к синхронизированному блоку, не может быть прерван. 
API Lock предоставляет метод lockInterruptibly(), который можно использовать для прерывания потока, 
ожидающего блокировку.
## Lock API
Методы интерфейса Lock:
- void lock() - Получение блокировки, если она доступна. Если блокировка недоступна, поток блокируется до тех пор,
пока блокировка не будет освобождена.
- void lockInterruptibly() - аналогичен lock(), но позволяет прервать заблокированный поток и возобновить
выполнение через брошенный java.lang.InterruptedException.
- boolean tryLock() - Это неблокирующая версия метода lock(). Он пытается получить блокировку немедленно и 
возвращает true, если блокировка прошла успешно.
- boolean tryLock(long timeout, TimeUnit timeUnit) - аналогичен tryLock(), за исключением того, что он ожидает
заданный таймаут, прежде чем отказаться от попытки получить блокировку.
- void unlock() разблокирует экземпляр Lock.

Заблокированный экземпляр всегда должен быть разблокирован, чтобы избежать состояния тупика.

Рекомендуемый блок кода для использования блокировки должен содержать блок try/catch и блок finally:
```java
Lock lock = ...; 
lock.lock();
try {
    // доступ к общему ресурсу
} finally {
    lock.unlock();
}
```
В дополнение к интерфейсу Lock у нас есть интерфейс ReadWriteLock, который поддерживает пару блокировок, одну для 
операций только чтения и одну для операций записи. Блокировка на чтение может одновременно принадлежать нескольким 
потокам, пока не происходит запись.

ReadWriteLock объявляет методы для получения блокировок на чтение или запись:
- Lock readLock() возвращает блокировку, которая используется для чтения. 
- Lock writeLock() возвращает блокировку, используемую для записи.
## Реализации Lock
### ReentrantLock
Класс ReentrantLock реализует интерфейс Lock. Он предлагает ту же семантику параллелизма и памяти, что и неявная 
блокировка монитора, доступ к которой осуществляется с помощью синхронизированных методов и операторов,
с расширенными возможностями.

Давайте посмотрим, как мы можем использовать ReentrantLock для синхронизации:
```java
public class SharedObjectWithLock {
    //...
    ReentrantLock lock = new ReentrantLock();
    int counter = 0;

    public void perform() {
        lock.lock();
        try {
            // Critical section here
            count++;
        } finally {
            lock.unlock();
        }
    }
    //...
}
```
Нам нужно убедиться, что мы оборачиваем вызовы lock() и unlock() в блок try-finally, чтобы избежать ситуаций тупика.

Давайте посмотрим, как работает tryLock():
```java
public void performTryLock(){
    //...
    boolean isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
    
    if(isLockAcquired) {
        try {
            //Critical section here
        } finally {
            lock.unlock();
        }
    }
    //...
}
```
В этом случае поток, вызывающий tryLock(), будет ждать одну секунду и откажется от ожидания, если блокировка 
не будет доступна.
### ReentrantReadWriteLock
Класс ReentrantReadWriteLock реализует интерфейс ReadWriteLock.

Рассмотрим правила получения ReadLock или WriteLock потоком:
- Read Lock - если ни один поток не получил блокировку на запись или не запросил ее, несколько потоков могут получить 
блокировку на чтение. 
- Write Lock - если ни один поток не читает и не записывает, только один поток может получить блокировку записи.

Давайте рассмотрим, как использовать ReadWriteLock:
```java
public class SynchronizedHashMapWithReadWriteLock {

    Map<String,String> syncHashMap = new HashMap<>();
    ReadWriteLock lock = new ReentrantReadWriteLock();
    // ...
    Lock writeLock = lock.writeLock();

    public void put(String key, String value) {
        try {
            writeLock.lock();
            syncHashMap.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
    //...
    public String remove(String key){
        try {
            writeLock.lock();
            return syncHashMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }
    //...
}
```
Для обоих методов записи нам нужно окружить критическую секцию блокировкой записи - только один поток может 
получить к ней доступ:
```java
Lock readLock = lock.readLock();
//...
public String get(String key){
    try {
        readLock.lock();
        return syncHashMap.get(key);
    } finally {
        readLock.unlock();
    }
}

public boolean containsKey(String key) {
    try {
        readLock.lock();
        return syncHashMap.containsKey(key);
    } finally {
        readLock.unlock();
    }
}
```
Для обоих методов чтения нам нужно окружить критическую секцию блокировкой чтения. Несколько потоков могут получить 
доступ к этой секции, если не выполняется операция записи.
### StampedLock
StampedLock появился в Java 8. Он также поддерживает блокировки как на чтение, так и на запись.

Однако методы получения блокировки возвращают штамп, который используется для освобождения блокировки или для проверки 
того, что блокировка все еще действительна:
```java
public class StampedLockDemo {
    Map<String,String> map = new HashMap<>();
    private StampedLock lock = new StampedLock();

    public void put(String key, String value){
        long stamp = lock.writeLock();
        try {
            map.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public String get(String key) throws InterruptedException {
        long stamp = lock.readLock();
        try {
            return map.get(key);
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
```
Еще одна возможность, предоставляемая StampedLock, - оптимистичная блокировка. Чаще всего операциям чтения не нужно 
ждать завершения операции записи, и в результате полноценная блокировка чтения не требуется.

Вместо этого мы можем перейти на блокировку чтения:
```java
public String readWithOptimisticLock(String key) {
    long stamp = lock.tryOptimisticRead();
    String value = map.get(key);

    if(!lock.validate(stamp)) {
        stamp = lock.readLock();
        try {
            return map.get(key);
        } finally {
            lock.unlock(stamp);               
        }
    }
    return value;
}
```
## Работа с Conditions
Класс Condition предоставляет возможность потоку ждать наступления некоторого условия при выполнении критической секции.

Это может произойти, когда поток получает доступ к критической секции, но не имеет необходимых условий для выполнения 
своей операции. Например, поток-читатель может получить доступ к блокировке общей очереди, в которой еще нет 
данных для потребления.

Традиционно Java предоставляет методы wait(), notify() и notifyAll() для взаимодействия потоков.

Условия имеют схожие механизмы, но мы также можем задавать несколько условий:
```java
public class ReentrantLockWithCondition {

    Stack<String> stack = new Stack<>();
    int CAPACITY = 5;

    ReentrantLock lock = new ReentrantLock();
    Condition stackEmptyCondition = lock.newCondition();
    Condition stackFullCondition = lock.newCondition();

    public void pushToStack(String item){
        try {
            lock.lock();
            while(stack.size() == CAPACITY) {
                stackFullCondition.await();
            }
            stack.push(item);
            stackEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String popFromStack() {
        try {
            lock.lock();
            while(stack.size() == 0) {
                stackEmptyCondition.await();
            }
            return stack.pop();
        } finally {
            stackFullCondition.signalAll();
            lock.unlock();
        }
    }
}
```
