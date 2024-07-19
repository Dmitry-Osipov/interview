# Deadlocks
## Оглавление
- [Описание deadlock](#описание-deadlock)
- [Перепутанный порядок блокировки](#перепутанный-порядок-блокировки)
- [Выводы](#выводы)
## Описание deadlock
Допустим, у нас есть классы X и Y, LockA и LockB. Как только X блокирует LockA, Y блокирует LockB, далее X требует для
работы LockB, а Y - LockA. Но так как нужные блокировки уже заняты, то мы находимся в бесконечном ожидании
## Перепутанный порядок блокировки
```java
class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }
}
```
Иногда перепутанный порядок блокировки:
```java
void transferMoney(Account fromAccount, Account toAccount,
                     int amount) throws InsufficientFundsException {
  synchronized (fromAccount) {
    synchronized (toAccount) {
      if (fromAccount.getBalance() < amount)
        throw new InsufficientFundsException();
      else {
        fromAccount.debit(amount);
        toAccount.credit(amount);
      }
    }
  }
}
```
## Выводы
- Если процедура захватывает несколько блокировок, возможен deadlock
- Во избежание deadlocks нужно следить за тем, чтобы блокировки всегда захватывались в одном и том же порядке
- Если вы захватили блокировку, закончите с ней как можно скорее, не вызывайте внешних методов