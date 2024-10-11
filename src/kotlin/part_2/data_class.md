# Data classes
## Оглавление
- [Расскажите о Data классах](#расскажите-о-data-классах)
- [Что такое мульти-декларации?](#что-такое-мульти-декларации)
- [Что делает функция componentN()?](#что-делает-функция-componentn)
- [Какие требования должны быть соблюдены для создания data класса?](#какие-требования-должны-быть-соблюдены-для-создания-data-класса)
- [Можно ли наследоваться от data класса?](#можно-ли-наследоваться-от-data-класса)
## Расскажите о Data классах
Data класс предназначен исключительно для хранения каких-либо данных.

Основное преимущество: для параметров, переданных в основном конструкторе автоматически будут переопределены методы
toString(), equals(), hashCode(), copy(). Важно: метод copy() вернёт поверхностную копию. Каждый сгенерированный 
по умолчанию метод можно переопределить.

Также для каждой переменной, объявленной в основном конструкторе, автоматически генерируются функции componentN(), 
где N — номер позиции переменной в конструкторе.

Благодаря наличию вышеперечисленных функций внутри data класса мы исключаем написание шаблонного кода.
```kotlin
data class DataClassExample(private var name: String, private var age: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataClassExample

        if (name != other.name) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age
        return result
    }

    override fun toString(): String {
        return "DataClassExample(name='$name', age=$age)"
    }

    fun copy(): DataClassExample {
        return DataClassExample("No Name", 0)
    }
}
```
## Что такое мульти-декларации?

Мульти-декларации (destructuring declarations или деструктуризирующее присваивание) — это способ извлечения значений из
объекта и присвоения их сразу нескольким переменным. В Kotlin этот механизм поддерживается с помощью оператора 
распаковки (destructuring operator) — componentN(), где N — номер компонента.

При создании data класса Kotlin автоматически создает функции componentN() для каждого свойства класса, где N — номер
позиции переменной в конструкторе. Функции componentN() возвращают значения свойств в порядке их объявления в
конструкторе. Это позволяет использовать мульти-декларации для распаковки значений свойств и присваивания их отдельным
переменным.

Например, если у нас есть data класс Person с двумя свойствами name и age, мы можем использовать мульти-декларации,
чтобы извлечь эти свойства и присвоить их двум переменным:
```kotlin
data class Person(val name: String, val age: Int)

val person = Person("Alice", 29)
val (name, age) = person

println(name) // Alice
println(age) // 29
```
Также можно использовать мульти-декларации в циклах, чтобы итерироваться по спискам объектов и распаковывать значения 
свойств:
```kotlin
val people = listOf(Person("Alice", 30), Person("Bob", 40))
for ((name, age) in people) {
    println("$name is $age years old")
}

// Alice is 30 years old
// Bob is 40 years old
```
Мульти-декларации также могут быть использованы с массивами и другими коллекциями:
```kotlin
val list = listOf("apple", "banana", "orange")
val (first, second, third) = list

println(first) // apple
println(second) // banana
println(third) // orange
```
## Что делает функция componentN()?
Функция componentN() возвращает значение переменной и позволяет обращаться к свойствам объекта класса по их порядковому
номеру. Генерируется автоматически только для data классов.

Также функцию componentN() можно создать самому для класса, который не является data классом.
```kotlin
class Person(val firstName: String, val lastName: String, val age: Int) {
    operator fun component1() = firstName
    operator fun component2() = lastName
    operator fun component3() = age
}
```
Теперь можно использовать мульти-декларации для класса Person:
```kotlin
val person = Person("John", "Doe", 30)
val (firstName, lastName, age) = person
println("$firstName $lastName is $age years old.")
```
В данном примере мы определили функции component1(), component2() и component3() как операторы с ключевым словом
operator. Они возвращают значения свойств firstName, lastName и age соответственно. После этого мы можем использовать
мульти-декларации для разбивки объекта Person на отдельные переменные.
## Какие требования должны быть соблюдены для создания data класса?
- Класс должен иметь хотя бы одно свойство, объявленное в основном конструкторе.
- Все параметры основного конструктора должны быть отмечены val или var (рекомендуется val).
- Классы данных не могут быть abstract, open, sealed или inner.
## Можно ли наследоваться от data класса?
От data класса нельзя наследоваться т.к. он является final классом, но он может наследоваться от других классов.
