# Условия в блокировках
## Оглавление
- [Описание](#описание)
- [Основные методы Condition](#методы-condition)
- [Пример](#пример)
## Описание
Применение условий в блокировках позволяет добиться контроля над управлением доступом к потокам. Условие блокировки 
представляет собой объект интерфейса Condition из пакета java.util.concurrent.locks
## Методы Condition
Применение объектов Condition во многом аналогично использованию методов wait/notify/notifyAll класса Object, которые 
были рассмотрены в одной из прошлых тем. В частности, мы можем использовать следующие методы интерфейса Condition:
- await: поток ожидает, пока не будет выполнено некоторое условие и пока другой поток не вызовет методы 
signal/signalAll. Во многом аналогичен методу wait класса Object. Этот метод также имеет разные варианты со временем: 
ожидать конкретное время и до конкретного времени
- signal: сигнализирует, что поток, у которого ранее был вызван метод await(), может продолжить работу. 
Применение аналогично использованию методу notify класса Object
- signalAll: сигнализирует всем потокам, у которых ранее был вызван метод await(), что они могут продолжить работу. 
Аналогичен методу notifyAll() класса Object

Эти методы вызываются из блока кода, который попадает под действие блокировки ReentrantLock. Сначала, используя эту 
блокировку, нам надо получить объект Condition:
```java
ReentrantLock locker = new ReentrantLock();
Condition condition = locker.newCondition();
```
Как правило, сначала проверяется условие доступа. Если соблюдается условие, то поток ожидает, пока условие не изменится:
```java
while (условие)
    condition.await();
```
После выполнения всех действий другим потокам подается сигнал об изменении условия:
```java
condition.signalAll();
```
Важно в конце вызвать метод signal/signalAll, чтобы избежать возможности взаимоблокировки потоков.
## Пример
Для примера возьмем задачу из темы про методы wait/notify и изменим ее, применяя объект Condition.

Итак, у нас есть склад, где могут одновременно быть размещено не более 3 товаров. И производитель должен произвести 5 
товаров, а покупатель должен эти товары купить. В то же время покупатель не может купить товар, если на складе нет 
никаких товаров:
```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
 
public class Program {
  
    public static void main(String[] args) {
        Store store = new Store();
        Producer producer = new Producer(store);
        Consumer consumer = new Consumer(store);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

// Класс Магазин, хранящий произведенные товары
class Store {
   private int product=0;
   ReentrantLock locker;
   Condition condition;
    
   Store() {
       locker = new ReentrantLock(); // создаем блокировку
       condition = locker.newCondition(); // получаем условие, связанное с блокировкой
   }
    
   public void get() {
      locker.lock();
      try{
          // пока нет доступных товаров на складе, ожидаем
          while (product<1) {
              condition.await();
          }
           
          product--;
          System.out.println("Покупатель купил 1 товар");
          System.out.println("Товаров на складе: " + product);
           
          // сигнализируем
          condition.signalAll();
      } catch (InterruptedException e){
          System.out.println(e.getMessage());
      } finally{
          locker.unlock();
      }
   }
   
   public void put() {
       locker.lock();
       try {
          // пока на складе 3 товара, ждем освобождения места
          while (product>=3) {
              condition.await();
          }
           
          product++;
          System.out.println("Производитель добавил 1 товар");
          System.out.println("Товаров на складе: " + product);
          // сигнализируем
          condition.signalAll();
      } catch (InterruptedException e){
          System.out.println(e.getMessage());
      } finally{
          locker.unlock();
      }
   }
}

// класс Производитель
class Producer implements Runnable{
    Store store;
    
    Producer(Store store) {
       this.store=store; 
    }
    
    public void run() {
        for (int i = 1; i < 6; i++) {
            store.put();
        }
    }
}
// Класс Потребитель
class Consumer implements Runnable{ 
    Store store;
    
    Consumer(Store store) {
       this.store=store; 
    }
    
    public void run() {
        for (int i = 1; i < 6; i++) {
            store.get();
        }
    }
}
```
В итоге мы получим вывод наподобие следующего:
```
Производитель добавил 1 товар
Товаров на складе: 1
Производитель добавил 1 товар
Товаров на складе: 2
Производитель добавил 1 товар
Товаров на складе: 3
Покупатель купил 1 товар
Товаров на складе: 2
Покупатель купил 1 товар
Товаров на складе: 1
Покупатель купил 1 товар
Товаров на складе: 0
Производитель добавил 1 товар
Товаров на складе: 1
Производитель добавил 1 товар
Товаров на складе: 2
Покупатель купил 1 товар
Товаров на складе: 1
Покупатель купил 1 товар
Товаров на складе: 0
```