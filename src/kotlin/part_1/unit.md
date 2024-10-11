# Unit
## Оглавление
- [Кратко о Unit](#кратко-о-unit)
- [Сколько существует instance Unit (1)](#сколько-существует-instance-unit-1)
## Кратко о Unit
Тип Unit в Kotlin выполняет ту же функцию, что и void в Java.

Возвращаемый тип можно не указывать, если функция ничего не возвращает. По умолчанию там будет Unit:
```kotlin
fun knockKnock() {
   println("Who’s there?")
}

// то же самое, но с указанным типом Unit
fun knockKnock(): Unit = println("Who’s there?")
```
## Сколько существует instance Unit (1)?
В стандартной библиотеке Kotlin Unit определён как объект, наследуемый от Any и содержащий единственный метод,
переопределяющий toString():
```kotlin
public object Unit {
   override fun toString() = "kotlin.Unit"
}
```
Unit является синглтоном (ключевое слово object). Unit ничего не возвращает, а метод toString всегда будет возвращать
“kotlin.Unit”. При компиляции в java-код Unit всегда будет превращаться в void.
