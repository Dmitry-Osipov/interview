# Потокобезопасные структуры данных
## Оглавление
- [Неблокирующие алгоритмы](#неблокирующие-алгоритмы)
- [Atomics](#atomics)
- [Атомарные операции в классах атомарных переменных](#атомарные-операции-в-классах-атомарных-переменных)
- [Потокобезопасные коллекции](#потокобезопасные-коллекции)
- [CopyOnWriteArrayList и CopyOnWriteArraySet](#copyonwritearraylist-и-copyonwritearrayset)
- [ConcurrentLinkedQueue/Deque](#concurrentlinkedqueuedeque)
- [Блокирующие очереди: средство реализации producer-consumer pattern](#блокирующие-очереди-средство-реализации-producer-consumer-pattern)
- [ConcurrentHashMap](#concurrenthashmap)
- [ConcurrentSkipListMap](#concurrentskiplistmap)
## Неблокирующие алгоритмы
- Блокировка (synchronized или ReentrantLock) решает вопрос координации действий с разных тредов с переменной
- Но если много тредов конкурируют за блокировку (high lock contention), затраты ресурсов на координацию тредов 
становятся значительными
- Альтернативой являются неблокирующие алгоритмы, использующие поддержку специальных атомарных машинных инструкций 
(compare-and-swap)
- В Java-библиотеке доступны классы атомарных переменных и потокобезопасные коллекции, реализованные в т.ч. на 
неблокирующих алгоритмах
## Atomics
- package java.util.concurrent.atomic
  - AtomicBoolean, AtomicInteger, AtomicLong, AtomicReference
  - AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray
- Могут быть использованы как "улучшенные volatile-переменные", т.к. результат вызова set(...) виден другим тредам при 
вызове get(...)
- Поддерживают атомарные операции
## Атомарные операции в классах атомарных переменных
getAndSet(newValue) | compareAndSet(expect, update)

incrementAndGet() | decrementAndGet()

getAndIncrement() | getAndDecrement()

getAndAdd(delta) | addAndGet(delta)

getAndUpdate(updateFunction)
updateAndGet(updateFunction)

getAndAccumulate(x, accumulatorBiFunction)
accumulateAndGet(x, accumulatorBiFunction)
## Потокобезопасные коллекции
- В ранних версиях Java можно было "сделать" коллекцию потокобезопасной, обернув в Collections.synchronizedXXX(...). 
Это сериализовывало любой доступ к внутреннему состоянию коллекции. Из-за поддержки обратной совместимости и сейчас тоже 
можно, но не нужно
- Цена такого решения - плохой параллелизм: конкуренция за блокировку (lock contention)
- С версии 5 появились классы, специально разработанные для потокобезопасности, с меньшим кол-вом блокировок. Их 
использование является предпочтительным
## CopyOnWriteArrayList и CopyOnWriteArraySet
- Структуры данных на основе массива
- Пересоздают всё заново при каждой модификации
- Это дорого, зато все читающие итераторы стабильны
- Хороши, когда на одну операцию записи приходится много операций чтения
## ConcurrentLinkedQueue/Deque
ConcurrentLinkedQueue<E> extends AbstractQueue<E> implements Queue<E>

ConcurrentLinkedDeque<E> extends AbstractCollection<E> implements Deque<E>

- Основаны на неблокирующем алгоритме (CAS-операции)
- poll() вернёт null, если очередь пуста
- Под капотом - связаные (Queue)/двусвязные (Deque) списки
## Блокирующие очереди: средство реализации producer-consumer pattern
```java
public interface BlockingQueue<E> {
    void put(E e) throws InterruptedException;
    E take() throws InterruptedException;
}
```
Этот интерфейс наследуют классы: ArrayBlockingQueue, LinkedBlockingDeque, PriorityBlockingQueue, SynchronousQueue.
- Могут быть ограничены по размеру (capacity constrained)
- Методы put() и take() ждут, пока не появится возможность положить или взять элемент
- PriorityBlockingQueue не лимитируется по capacity
- SynchronousQueue не имеет capacity вовсе, передаёт элементы обрабатывающим тредам напрямую
## ConcurrentHashMap
class ConcurrentHashMap<K,V> extends AbstractMap<K,V> implements ConcurrentMap<K,V>
- Замена HashMap при разделённом доступе к данным
- Не блокируется при чтении и редко блокируется при записи
- Не позволяет использовать null в кач-ве ключа или значения
- Полезные методы атомарны:
  - putIfAbsent(key, value)
  - remove(key, value)
  - replace(key, oldValue, newValue)
## ConcurrentSkipListMap
class ConcurrentSkipListMap<K,V> extends AbstractMap<K,V> implements ConcurrentNavigableMap<K,V>
- Замена TreeMap при разделённом доступе к данным
- Не позволяет использовать null в кач-ве ключа или значения
- Имеет атомарные методы