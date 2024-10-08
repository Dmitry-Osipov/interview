# Проксирование в Spring
## Оглавление
- [Проксирование](#проксирование)
- [Основная терминология](#основная-терминология-аоп)
- [Пример работы прокси](#пример-работы-прокси)
- [Влияние на производительность](#влияние-на-производительность)
## Проксирование
Прокси в нашем случае - это объект, созданный при помощи АОП для реализации так называемых аспектных контрактов. Проще 
говоря, это обёртка вокруг экземпляра bean, которая может использовать функционал оригинального бина, но со своими 
доработками. Spring использует прокси под капотом для автоматического добавления доп. поведения без изменения 
существующего кода. Достигается одним из 2 способов:
1) JDK dynamic proxy - Spring AOP по умолчанию использует JDK dynamic proxy, которые позволяют проксировать любой 
интерфейс (или набор интерфейсов). Если целевой объект реализует хотя бы 1 интерфейс, то будет использоваться 
динамический прокси JDK.
2) CGLIB-прокси - используется по умолчанию, если бизнес-объект не реализует ни одного интерфейса.

Так как прокси по сути просто оборачивает bean, то он может добавить свою логику до и после выполнения методов. Таким 
образом можно реализовать любую доп. логику по типу логгирования, транзакционности, метрики и т.д.
## Основная терминология АОП
- Aspect - некий код, который актуален для нескольких классов. Управление транзакциями является хорошим примером 
сквозного аспекта в корпоративных Java-приложениях. В Spring AOP аспекты реализуются с помощью аннотации @Aspect или 
XML-конфигурации класса.
- Join point - точка во время выполнения программы, такая как выполнение метода или обработка исключения. В Spring AOP
точка соединения всегда представляет собой выполнение метода. 
- Advice - действие, предпринимаемое аспектом в определённой точке соединения. Advice можно разделить на те, которые 
выполняются только "до" основной логики метода, либо "после", либо "вокруг" (и до, и после). Многие AOP-фреймворки 
моделируют advice как перехватчик, который поддерживает цепочку других перехватчиков вокруг точки соединения.
- Pointcut - предикат, который соответствует join point. Advice ассоциируется с выражением pointcut и запускается в 
любой точке соединения, совпадающей с указателем (например, выполнение метода с определённым именем). Концепция точек 
соединения (join point), сопоставляемых выражениями pointcut, является центральной в AOP, и Spring по умолчанию 
использует язык выражений AspectJ pointcut.
- Introduction - объявление доп. методов или полей от имени типа. Spring AOP позволяет нам вводить новые интерфейсы 
(и соответствующую реализацию) в любой рекомендуемый объект. Например, вы можете использовать introduction, чтобы 
заставить bean реализовать интерфейс IsModified, чтобы упростить кеширование.
- Target object - объект, который советуется одним или несколькими аспектами. Также известен как "advised object". 
Поскольку Spring AOP реализуется с помощью прокси во время выполнения, этот объект всегда является проксированным 
объектом.
- AOP proxy - объект, созданный AOP-фреймворком для реализации аспектов. В Spring прокси AOP - это динамический прокси 
JDK или прокси CGLIB.
- Weaving - связывание аспектов с другими типами приложений или объектами для создания нужной логики. Это может быть 
сделано во время компиляции, во время загрузки или во время выполнения. Spring AOP, как и другие Java AOP-фреймворки, 
выполняет weaving во время выполнения.
## Пример работы прокси

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Loggable {
}

@Aspect
@Component
public class LoggerAspect {
    @Pointcut("@annotation(Loggable)")
    public void loggableMethod() {
    }
    
    @Around("loggableMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            System.out.println("Execution time for " + className + "." + methodName + " :: " + 
                    stopWatch.getTotalTimeMillis() + " ms");
        }
    }
}

@Component
public class Pojo {
    @Loggable
    public void test() {
        System.out.println("test method called");
        test.testUtil();
    }
    
    @Loggable
    public void testUtil() {
        System.out.println("testUtil method called");
    }
}

@SpringBootApplication
public class SpringAopDemoApplication implements CommandLineRunner {
    @Autowired
    Pojo pojo;

    public static void main(String[] args) {
        SpringApplication.run(SpringAopDemoApplication.class, args);
    }
    
    @Override
    public void run(String... args) {
        pojo.test();
        System.out.println("Out of Test");
        pojo.testUtil();
    }
}
```
Результат выполнения кода:
```
test method called
testUtil method called
Execution time for Test.test :: 18 ms
Out of Test
testUtil method called
Execution time for Test.testUtil :: 0 ms
```
Последовательность действий:
1) Прокси создан
2) Вызвана какая-то логика до основного метода
3) Происходит вызов основного метода
4) Данный метод внутри себя "что-то делает" и что именно - прокси не имеет понятия
5) В числе этих самых "что-то" метод вызывает другой метод не через бин, а у себя напрямую. Таким образом, вызов самого 
себя не приводит к выполнению условий создания прокси

Примечание: аннотация @Aspect на классе помечает его кандидатом в прокси и, следовательно, исключает его из 
автопроксирования. Следовательно, в Spring AOP невозможно, чтобы сами аспекты были целью рекомендаций от других аспектов
## Влияние на производительность
Поскольку прокси является дополнительным промежуточным звеном между вызывающим кодом и целевым объектом, неудивительно, 
что возникают некоторые накладные расходы. Примечательно, что эти накладные расходы фиксированы. Прокси-вызов добавляет 
фиксированную задержку независимо от времени выполнения обычного метода. Вопрос в том, должна ли нас волновать эта 
задержка? И да, и нет!

Если дополнительное поведение само по себе имеет гораздо большее влияние на производительность (например, кэширование
или управление транзакциями), чем сам механизм проксирования, то накладные расходы кажутся незначительными. Однако, если 
поведение должно применяться к большому количеству объектов (например, протоколирование каждого метода), то накладные 
расходы уже не являются незначительными.

Еще один момент, вызывающий беспокойство - это количество проксируемых объектов, задействованных в одном запросе. Если 
один запрос включает вызовы сотен или тысяч проксированных методов, то накладные расходы становятся значительными и их 
нельзя игнорировать.

Для таких редких сценариев, когда требования не могут быть решены с помощью систем на основе прокси, предпочтительнее 
использовать byte code weaving. При byte code weaving берутся классы и аспекты, а на выходе получаются woven файлы 
.class. Поскольку аспекты вплетаются непосредственно в код, это обеспечивает лучшую производительность, но сложнее в 
реализации по сравнению с Spring AOP.