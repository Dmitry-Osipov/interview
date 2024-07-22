# @Scheduled in Spring
## Оглавление
- [Описание](#описание)
- [Включение поддержки @Scheduled](#включение-поддержки-scheduled)
- [Запланировать задачу с фиксированной задержкой (Fixed Delay)](#запланировать-задачу-с-фиксированной-задержкой-fixed-delay)
- [Запланируйте выполнение задачи с фиксированной скоростью (Fixed Rate)](#запланируйте-выполнение-задачи-с-фиксированной-скоростью-fixed-rate)
- [Fixed Rate vs Fixed Delay](#fixed-rate-vs-fixed-delay)
- [Планирование задачи с начальной задержкой (Initial Delay)](#планирование-задачи-с-начальной-задержкой-initial-delay)
- [Планирование задачи с помощью выражений Cron](#планирование-задачи-с-помощью-выражений-cron)
- [Параметризация @Scheduled](#параметризация-scheduled)
- [Конфигурация @Scheduled с использованием xml](#конфигурация-scheduled-с-использованием-xml)
- [Динамическая настройка задержки (Delay) или скорости (Rate) во время выполнения](#динамическая-настройка-задержки-delay-или-скорости-rate-во-время-выполнения)
- [Параллельное выполнение задач](#параллельное-выполнение-задач)
- [Использование Spring Boot](#использование-spring-boot)
## Описание
Чтобы аннотировать метод с помощью @Scheduled, необходимо следовать следующим простым правилам:
- метод, как правило, должен иметь тип возврата void (если это не так, возвращаемое значение будет проигнорировано)
- метод не должен ожидать никаких параметров
## Включение поддержки @Scheduled
Чтобы включить поддержку задач планирования и аннотации @Scheduled в Spring, мы можем использовать аннотацию:
```java
@Configuration
@EbableScheduling
public class SpringConfig { ... }
```
Мы можем сделать то же самое в XML:
```xml
<task:annotation-driven>
```
## Запланировать задачу с фиксированной задержкой (Fixed Delay)
Для начала настроим задачу на запуск после фиксированной задержки:
```java
@Scheduled(fixedDelay = 1000)
public void scheduleFixedDelayTask() {
    System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
}
```
В этом случае продолжительность между окончанием последнего выполнения и началом следующего фиксирована. Задача всегда 
ждет, пока не завершится предыдущая.

Эту опцию следует использовать, когда перед повторным запуском обязательно нужно завершить предыдущее выполнение.
## Запланируйте выполнение задачи с фиксированной скоростью (Fixed Rate)
Теперь давайте выполним задачу через фиксированный промежуток времени:
```java
@Scheduled(fixedRate = 1000)
public void scheduleFixedRateTask() {
    System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
}
```
Этот параметр следует использовать, когда каждое выполнение задачи является независимым.

Обратите внимание, что по умолчанию запланированные задачи не выполняются параллельно. Поэтому даже если мы 
использовали fixedRate, следующая задача не будет вызвана, пока не завершится предыдущая.

Если мы хотим поддерживать параллельное поведение в запланированных задачах, нам нужно добавить аннотацию @Async:
```java
@EnableAsync
public class ScheduledFixedRateExample {
    @Async
    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println("Fixed rate task async - " + System.currentTimeMillis() / 1000);
        Thread.sleep(2000);
    }
}
```
Теперь эта асинхронная задача будет вызываться каждую секунду, даже если предыдущая задача не выполнена.
## Fixed Rate vs Fixed Delay
Мы можем запустить запланированную задачу с помощью аннотации Spring @Scheduled, но благодаря свойствам fixedDelay и 
fixedRate характер выполнения меняется.

Свойство fixedDelay обеспечивает задержку в n миллисекунд между временем окончания выполнения задачи и временем начала 
следующего выполнения задачи.

Это свойство особенно полезно, когда нам нужно убедиться, что постоянно выполняется только один экземпляр задания. 
Для зависимых заданий это очень полезно.

Свойство fixedRate запускает запланированную задачу через каждые n миллисекунд. При этом не проверяется наличие 
предыдущих выполнений задачи.

Это удобно, когда все выполнения задачи независимы. Если мы не планируем превышать объем памяти и пула потоков, то 
fixedRate будет весьма кстати.

Хотя, если поступающие задачи не будут быстро завершаться, возможно, они закончатся исключением «Out of Memory».
## Планирование задачи с начальной задержкой (Initial Delay)
Далее запланируем задачу с задержкой (в миллисекундах):
```java
@Scheduled(fixedDelay = 1000, initialDelay = 1000)
public void scheduleFixedRateWithInitialDelayTask() {
    long now = System.currentTimeMillis() / 1000;
    System.out.println("Fixed rate task with one second initial delay - " + now);
}
```
Обратите внимание, что в этом примере мы используем как fixedDelay, так и initialDelay. Задача будет выполнена в первый
раз после значения initialDelay, а дальше она будет выполняться в соответствии с fixedDelay.

Этот вариант удобен, когда задача имеет настройки, которые должны быть завершены.
## Планирование задачи с помощью выражений Cron
Иногда задержки и скорости недостаточно, и нам нужна гибкость выражения cron для управления расписанием задач:
```java
@Scheduled(cron = "0 15 10 15 * ?")
public void scheduleTaskUsingCronExpression() {
    long now = System.currentTimeMillis() / 1000;
    System.out.println("schedule tasks using cron jobs - " + now);
}
```
Обратите внимание, что в этом примере мы планируем выполнение задачи на 10:15 утра 15 числа каждого месяца.

По умолчанию Spring будет использовать локальный часовой пояс сервера для выражения cron. Однако мы можем использовать 
атрибут zone, чтобы изменить этот часовой пояс:
```java
@Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris")
```
При такой конфигурации Spring будет планировать запуск аннотированного метода на 10:15 утра 15 числа каждого месяца по 
парижскому времени.
## Параметризация @Scheduled
Жесткое кодирование таких расписаний - это просто, но обычно нам нужно иметь возможность управлять расписанием без 
перекомпиляции и повторного развертывания всего приложения.

Мы воспользуемся выражениями Spring Expressions для внешней настройки задач и будем хранить их в файлах свойств.

Fixed Delay:
```java
@Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
```
FixedRate:
```java
@Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
```
CRON:
```java
@Scheduled(cron = "${cron.expression}")
```
## Конфигурация @Scheduled с использованием xml
Spring также предоставляет XML-способ настройки запланированных задач. Вот XML-конфигурация для их настройки:
```xml
<!-- Configure the scheduler -->
<task:scheduler id="myScheduler" pool-size="10" />

<!-- Configure parameters -->
<task:scheduled-tasks scheduler="myScheduler">
    <task:scheduled ref="beanA" method="methodA" 
      fixed-delay="5000" initial-delay="1000" />
    <task:scheduled ref="beanB" method="methodB" 
      fixed-rate="5000" />
    <task:scheduled ref="beanC" method="methodC" 
      cron="*/5 * * * * MON-FRI" />
</task:scheduled-tasks>
```
## Динамическая настройка задержки (Delay) или скорости (Rate) во время выполнения
Обычно все свойства аннотации @Scheduled разрешаются и инициализируются только один раз при запуске контекста Spring.

Поэтому изменение значений fixedDelay или fixedRate во время выполнения невозможно при использовании аннотации 
@Scheduled в Spring.

Однако существует обходной путь. Использование Spring's SchedulingConfigurer обеспечивает более настраиваемый способ, 
который дает нам возможность динамически устанавливать задержку или скорость.

Давайте создадим конфигурацию Spring, DynamicSchedulingConfig, и реализуем интерфейс SchedulingConfigurer:
```java
@Configuration
@EnableScheduling
public class DynamicSchedulingConfig implements SchedulingConfigurer {

    @Autowired
    private TickService tickService;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override 
                    public void run() {
                        tickService.tick();
                    }
                }, 
                new Trigger() {
                    @Override 
                    public Date nextExecutionTime(TriggerContext context) {
                        Optional<Date> lastCompletionTime = Optional.ofNullable(context.lastCompletionTime());
                        Instant nextExecutionTime = lastCompletionTime.orElseGet(Date::new).toInstant()
                                .plusMillis(tickService.getDelay());
                        return Date.from(nextExecutionTime);
                    }
                }
            );
    }
}
```
Как мы заметили, с помощью метода ScheduledTaskRegistrar.addTriggerTask мы можем добавить задачу Runnable и реализацию 
триггера для пересчета nextExecutionTime после окончания каждого выполнения.

Кроме того, мы аннотируем наш DynamicSchedulingConfig с помощью @EnableScheduling, чтобы планирование работало.

В результате мы запланировали запуск метода TickService.tick после каждого периода задержки, который динамически 
определяется во время выполнения методом getDelay.
## Параллельное выполнение задач
По умолчанию Spring использует локальный однопоточный планировщик для выполнения задач. В результате, даже если у нас 
несколько методов @Scheduled, каждый из них должен ждать, пока поток завершит выполнение предыдущей задачи.

Если наши задачи независимы, удобнее выполнять их параллельно. Для этого нам нужно предоставить TaskScheduler, который 
лучше подходит для наших нужд:
```java
@Bean
public TaskScheduler  taskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(5);
    threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
    return threadPoolTaskScheduler;
}
```
В приведенном выше примере мы настроили TaskScheduler с размером пула в 5 единиц, но следует помнить, что фактическая 
конфигурация должна быть настроена в соответствии с конкретными потребностями.
## Использование Spring Boot
Если мы используем Spring Boot, то можем воспользоваться еще более удобным подходом для увеличения размера пула 
планировщика.

Достаточно просто установить свойство spring.task.scheduling.pool.size:
```properties
spring.task.scheduling.pool.size=5
````
