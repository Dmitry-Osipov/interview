# Any
## Оглавление
- [От какого класса унаследованы все остальные классы?](#от-какого-класса-унаследованы-все-остальные-классы)
- [Чем Any в Kotlin отличается от Object в Java?](#чем-any-в-kotlin-отличается-от-object-в-java)
- [Какой тип находится на вершине иерархии типов?](#какой-тип-находится-на-вершине-иерархии-типов)
## От какого класса унаследованы все остальные классы?
Класс Any находится на вершине иерархии — все классы в Kotlin являются наследниками Any. Это стандартный родительский 
класс для всех классов, которые явно не унаследованы от другого класса. Именно в нем определены equals, hashCode и 
toString. Класс Any по назначению похож на Object в Java.
```kotlin
public open class Any {
   public open operator fun equals(other: Any?): Boolean
   public open fun hashCode(): Int
   public open fun toString(): String
}  
```
## Чем Any в Kotlin отличается от Object в Java?
Any не является полным аналогом Object.

В Object 11 методов в классе, в Any только 3 метода: equals(), hashCode() и toString(). При импорте типов Java в Kotlin 
все ссылки типа java.lang.Object преобразуются в Any. Поскольку Any не зависит от платформы, он объявляет только 
toString(), hashCode() и equals() в качестве своих членов, поэтому, чтобы сделать другие члены java.lang.Object 
доступными, Kotlin использует функции расширения.

Несмотря на то, что классы Object и Any имеют сходства (корневые классы иерархии классов), они также имеют и отличия,
связанные с языковыми особенностями Kotlin и Java:
1) Класс Any в Kotlin является не только базовым классом для пользовательских классов, но также и супертипом для всех
не-nullable типов данных, включая примитивные. В то время как в Java, класс Object является базовым классом только для
пользовательских классов.
2) Класс Any в Kotlin также имеет nullable версию Any?, которая является супертипом для всех nullable типов данных в 
Kotlin. В то время как в Java, класс Object не имеет nullable версии.
## Какой тип находится на вершине иерархии типов?
Аналогично Object в Java, к чему можно привести любой тип в Kotlin?
Правильным ответом будет Any?.

Сам по себе класс Any это почти аналог Object, однако, благодаря поддержке nullable и не-nullable типов в Kotlin мы 
получили Any?. Фактически, Any? соответствует любому типу и null, а Any только любому типу.

Если по порядку:
1) Any является корнем иерархии не-nullable типов. 
2) Any? является корнем иерархии nullable типов. 
3) Так как Any? является супертипом Any, то Any? находится в самом верху иерархии типов в Kotlin.

Картинка для понимания:
![Иерархия типов](https://habrastorage.org/r/w1560/getpro/habr/upload_files/78c/dde/a3c/78cddea3ca15b60997861e9e2332906e.png)
