# Livelock
## Описание
Проблема livelock заключается в том, что потоки внешне как бы живут, но при этом не могут ничего сделать, т.к. условие, 
по которым они пытаются продолжить свою работу, не могут выполниться. По сути Livelock похож на Deadlock, но только 
потоки не зависают на системном ожидании монитора, а что-то вечно делают
## Пример
```java
public class App {
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void log(String text) {
        String name = Thread.currentThread().getName(); //like Thread-1 or Thread-0
        String color = ANSI_BLUE;
        int val = Integer.valueOf(name.substring(name.lastIndexOf("-") + 1)) + 1;
        if (val != 0) {
            color = ANSI_PURPLE;
        }
        System.out.println(color + name + ": " + text + color);
        try {
            System.out.println(color + name + ": wait for " + val + " sec" + color);
            Thread.currentThread().sleep(val * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Lock first = new ReentrantLock();
        Lock second = new ReentrantLock();

        Runnable locker = () -> {
            boolean firstLocked = false;
            boolean secondLocked = false;
            try {
                while (!firstLocked || !secondLocked) {
                    firstLocked = first.tryLock(100, TimeUnit.MILLISECONDS);
                    log("First Locked: " + firstLocked);
                    secondLocked = second.tryLock(100, TimeUnit.MILLISECONDS);
                    log("Second Locked: " + secondLocked);
                }
                first.unlock();
                second.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(locker).start();
        new Thread(locker).start();
    }
}
```
Успешность кода выше зависит от того, в каком порядке планировщик потоков Java запустит потоки. Если первыми запустится 
Thread-1, то мы получим Livelock:
```
Thread-1: First Locked: true
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
Thread-0: Second Locked: true
Thread-0: wait for 1 sec
Thread-1: Second Locked: false
Thread-1: wait for 2 sec
Thread-0: First Locked: false
Thread-0: wait for 1 sec
...
```
Как видно из примера, оба потока поочерёдно пытаются захватить оба пока, но им это не удаётся. При этом они не в 
deadlock, т.е. визуально с ними всё хорошо и они выполняют свою работу