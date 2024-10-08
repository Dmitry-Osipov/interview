# Паттерны проектирования
## Оглавление
- [Определение](#определение)
- [Плюсы](#плюсы)
- [Минусы](#минусы)
- [Основные шаблоны (Fundamental)](#основные-шаблоны-fundamental)
- [Порождающие шаблоны (Creational)](#порождающие-шаблоны-creational)
- [Структурные шаблоны (Structural)](#структурные-шаблоны-structural)
- [Поведенческие шаблоны (Behavioral)](#поведенческие-шаблоны-behavioral)
- [Шаблоны параллельного программирования](#шаблоны-параллельного-программирования)
- [Другие типы шаблонов проектирования](#другие-типы-шаблонов-проектирования)
## Определение
Шаблон проектирования (паттерн, от англ. design pattern) — повторяемая архитектурная конструкция в сфере проектирования 
программного обеспечения, предлагающая решение проблемы проектирования в рамках некоторого часто возникающего контекста.
## Плюсы
В сравнении с полностью самостоятельным проектированием шаблоны обладают рядом преимуществ. Основная польза от 
использования шаблонов состоит в снижении сложности разработки за счёт готовых абстракций для решения целого класса 
проблем. Шаблон даёт решению своё имя, что облегчает коммуникацию между разработчиками, позволяя ссылаться на известные 
шаблоны. Таким образом, за счёт шаблонов производится унификация деталей решений: модулей, элементов проекта, — 
снижается количество ошибок. Применение шаблонов концептуально сродни использованию готовых библиотек кода. Правильно 
сформулированный шаблон проектирования позволяет, отыскав удачное решение, пользоваться им снова и снова. Набор
шаблонов помогает разработчику выбрать возможный, наиболее подходящий вариант проектирования.
## Минусы
Хотя легкое изменение кода под известный шаблон может упростить понимание кода с применением шаблонов могут быть 
связаны две сложности. Во-первых, слепое следование некоторому выбранному шаблону может привести к усложнению программы. 
Во-вторых, у разработчика может возникнуть желание попробовать некоторый шаблон в деле без особых оснований 
("Золотой молоток" - антипаттерн проектирования, заключающийся в использовании одного и того же решения везде, в том 
числе путём искусственной подгонки условий, требований, ограничений задачи под данное решение.).
## Основные шаблоны (Fundamental)
| Название                       | Оригинальное название | Описание                                                                                                                                                                                                                                                                                                                                                                                             |
|--------------------------------|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Шаблон делегирования           | Delegation pattern    | Объект внешне выражает некоторое поведение, но в реальности передаёт ответственность за выполнение этого поведения связанному объекту                                                                                                                                                                                                                                                                |
| Шаблон функционального дизайна | Functional design     | Гарантирует, что каждый модуль компьютерной программы имеет только одну обязанность и исполняет её с минимумом побочных эффектов на другие части программы                                                                                                                                                                                                                                           |
| Неизменяемый интерфейс         | Immutable interface   | Создание неизменяемого объекта                                                                                                                                                                                                                                                                                                                                                                       |
| Интерфейс                      | Interface             | Общий метод для структурирования компьютерных программ для того, чтобы их было проще понять                                                                                                                                                                                                                                                                                                          |
| Интерфейс-маркер               | Marker interface      | В качестве атрибута (как пометки объектной сущности) применяется наличие или отсутствие реализации интерфейса-маркера. В современных языках программирования вместо этого могут применяться атрибуты или аннотации                                                                                                                                                                                   |
| Контейнер свойств              | Property container    | Позволяет добавлять дополнительные свойства для класса в контейнер (внутри класса), вместо расширения класса новыми свойствами                                                                                                                                                                                                                                                                       |
| Канал событий                  | Event channel         | Расширяет шаблон «издатель — подписчик», создавая централизованный канал для событий. Использует объект-представитель для подписки и объект-представитель для публикации события в канале. Представитель существует отдельно от реального издателя или подписчика. Подписчик может получать опубликованные события от более чем одного объекта, даже если он зарегистрирован только на одном канале  |
## Порождающие шаблоны (Creational)
Порождающие шаблоны - шаблоны проектирования, которые абстрагируют процесс инстанцирования. Они позволяют сделать 
систему независимой от способа создания, композиции и представления объектов. Шаблон, порождающий классы, использует 
наследование, чтобы изменять инстанцируемый класс, а шаблон, порождающий объекты, делегирует инстанцирование другому 
объекту.

| Название                              | Оригинальное название                         | Описание                                                                                                               |
|---------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| Абстрактная фабрика                   | Abstract factory                              | Класс, который представляет собой интерфейс для создания компонентов системы                                           |
| Строитель                             | Builder                                       | Класс, который представляет собой интерфейс для создания сложного объекта                                              |
| Фабричный метод                       | Factory method                                | Определяет интерфейс для создания объекта, но оставляет подклассам решение о том, какой класс инстанцировать           |
| Отложенная инициализация              | Lazy initialization                           | Объект, инициализируемый во время первого обращения к нему                                                             |
| Мультитон                             | Multiton                                      | Гарантирует, что класс имеет поименованные экземпляры объекта и обеспечивает глобальную точку доступа к ним            |
| Объектный пул                         | Object pool                                   | Класс, который представляет собой интерфейс для работы с набором инициализированных и готовых к использованию объектов |
| Прототип                              | Prototype                                     | Определяет интерфейс создания объекта через клонирование другого объекта вместо создания через конструктор             |
| Получение ресурса есть ининциализация | Resource acquisition is initialization (RAII) | Получение некоторого ресурса совмещается с инициализацией, а освобождение — с уничтожением объекта                     |
| Одиночка                              | Singleton                                     | Класс, который может иметь только один экземпляр                                                                       |
## Структурные шаблоны (Structural)
Структурные шаблоны определяют различные сложные структуры, которые изменяют интерфейс уже существующих объектов или его
реализацию, позволяя облегчить разработку и оптимизировать программу.

| Название                      | Оригинальное название | Описание                                                                                                                                                        |
|-------------------------------|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Адаптер                       | Adapter / Wrapper     | Объект, обеспечивающий взаимодействие двух других объектов, один из которых использует, а другой предоставляет несовместимый с первым интерфейс                 |
| Мост                          | Bridge                | Структура, позволяющая изменять интерфейс обращения и интерфейс реализации класса независимо                                                                    |
| Компоновщик                   | Composite             | Объект, который объединяет в себе объекты, подобные ему самому                                                                                                  |
| Декоратор или Wrapper/Обёртка | Decorator             | Класс, расширяющий функциональность другого класса без использования наследования                                                                               |
| Фасад                         | Facade                | Объект, который абстрагирует работу с несколькими классами, объединяя их в единое целое                                                                         |
| Единая точка входа            | Front controller      | Обеспечивает унифицированный интерфейс для интерфейсов в подсистеме. Front Controller определяет высокоуровневый интерфейс, упрощающий использование подсистемы |
| Приспособленец                | Flyweight             | Это объект, представляющий себя как уникальный экземпляр в разных местах программы, но фактически не являющийся таковым                                         |
| Заместитель                   | Proxy                 | 	Объект, который является посредником между двумя другими объектами, и который реализует/ограничивает доступ к объекту, к которому обращаются через него        |
## Поведенческие шаблоны (Behavioral)
Поведенческие шаблоны определяют взаимодействие между объектами, увеличивая таким образом его гибкость.

| Название                             | Оригинальное название   | Описание                                                                                                                                                                         |
|--------------------------------------|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Цепочка обязанностей                 | Chain of responsibility | Предназначен для организации в системе уровней ответственности                                                                                                                   |
| Команда, Action, Transaction         | Command                 | Представляет действие. Объект команды заключает в себе само действие и его параметры                                                                                             |
| Интерпретатор                        | Interpreter             | Решает часто встречающуюся, но подверженную изменениям, задачу                                                                                                                   |
| Итератор, Cursor                     | Iterator                | Представляет собой объект, позволяющий получить последовательный доступ к элементам объекта-агрегата без использования описаний каждого из объектов, входящих в состав агрегации |
| Посредник                            | Mediator                | Обеспечивает взаимодействие множества объектов, формируя при этом слабую связанность и избавляя объекты от необходимости явно ссылаться друг на друга                            |
| Хранитель                            | Memento                 | Позволяет не нарушая инкапсуляцию зафиксировать и сохранить внутренние состояния объекта так, чтобы позднее восстановить его в этих состояниях                                   |
| Null Object                          | Null Object             | Предотвращает нулевые указатели, предоставляя объект «по умолчанию»                                                                                                              |
| Наблюдатель или Издатель - подписчик | Observer                | Определяет зависимость типа «один ко многим» между объектами таким образом, что при изменении состояния одного объекта все зависящие от него оповещаются об этом событии         |
| Слуга                                | Servant                 | Используется для обеспечения общей функциональности группе классов                                                                                                               |
| Спецификация                         | Specification           | Служит для связывания бизнес-логики                                                                                                                                              |
| Состояние                            | State                   | Используется в тех случаях, когда во время выполнения программы объект должен менять своё поведение в зависимости от своего состояния                                            |
| Стратегия                            | Strategy                | Предназначен для определения семейства алгоритмов, инкапсуляции каждого из них и обеспечения их взаимозаменяемости                                                               |
| Шаблонный метод                      | Template method         | Определяет основу алгоритма и позволяет наследникам переопределять некоторые шаги алгоритма, не изменяя его структуру в целом                                                    |
| Посетитель                           | Visitor                 | Описывает операцию, которая выполняется над объектами других классов. При изменении класса Visitor нет необходимости изменять обслуживаемые классы                               |
| Одноразовый посетитель               | Single-serving visitor  | Оптимизирует реализацию шаблона посетитель, который инициализируется, единожды используется, и затем удаляется                                                                   |
| Иерархический посетитель             | Hierarchical visitor    | Предоставляет способ обхода всех вершин иерархической структуры данных (напр. древовидной)                                                                                       |
## Шаблоны параллельного программирования
Шаблоны параллельного программирования используются для более эффективного написания многопоточных программ и
предоставляют готовые решения проблем синхронизации.

| Название                             | Оригинальное название                             | Описание                                                                                                                                                  |
|--------------------------------------|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Активный объект                      | Active object                                     | Служит для отделения потока выполнения метода от потока, в котором он был вызван. Использует шаблоны асинхронный вызов методов и планировщик              |
| Balking                              | Balking                                           | Служит для выполнения действия над объектом только тогда, когда тот находится в корректном состоянии                                                      |
| Связывающие свойства                 | Binding properties                                | Комбинирует несколько наблюдателей для обеспечения синхронизации свойств в различных объектах                                                             |
| Обмен сообщениями                    | Messaging pattern, Messaging design pattern (MDP) | Позволяет компонентам и приложениям обмениваться информацией (сообщениями)                                                                                |
| Блокировка с двойной проверкой       | Double checked locking                            | Предназначен для уменьшения накладных расходов, связанных с получением блокировки                                                                         |
| Асинхронная модель на основе событий | Event-Based Asynchronous                          | Адресные проблемы с Асинхронным паттерном, которые возникают в программах с несколькими потоками                                                          |
| Охраняемая приостановка              | Guarded suspension                                | Используется для блокировки выполнения действия над объектом только тогда, когда тот находится в корректном состоянии                                     |
| Блокировка                           | Lock                                              | Один поток блокирует ресурс для предотвращения доступа или изменения его другими потоками                                                                 |
| Монитор                              | Monitor                                           | Объект, предназначенный для безопасного использования более чем одним потоком                                                                             |
| Реактор                              | Reactor                                           | Предназначен для синхронной передачи запросов сервису от одного или нескольких источников                                                                 |
| Блокировка чтения-записи             | Read/write lock                                   | Позволяет нескольким потокам одновременно считывать информацию из общего хранилища, но позволяя только одному потоку в текущий момент времени её изменять |
| Планировщик                          | Scheduler                                         | Обеспечивает механизм реализации политики планирования, но при этом не зависящих ни от одной конкретной политики                                          |
| Thread Pool                          | Thread Pool                                       | Предоставляет пул потоков для обработки заданий, представленных обычно в виде очереди                                                                     |
| Thread-Specific Storage              | Thread-Specific Storage                           | Служит для предоставления различных глобальных переменных для разных потоков                                                                              |
| Однопоточное выполнение              | Single thread execution                           | Препятствует конкурентному вызову метода, тем самым запрещая параллельное выполнение этого метода                                                         |
| Кооперативный паттерн                | Cooperative pattern                               | Обеспечивает механизм безопасной остановки потоков исполнения, используя общий флаг для сигнализирования прекращения работы потоков                       |
## Другие типы шаблонов проектирования
### Шаблоны генерации объектов
- Singleton
- Factory Method
- Abstract Factory
- Prototype
### Шаблоны программирования гибких объектов
- Composite
- Decorator
- Facade
### Шаблоны выполнения задач
- Interpreter
- Strategy
- Observer
- Visitor
- Command
### Шаблоны архитектуры системы
- Model-View-Controller (MVC)
- Model-View-Presenter (MVP)
- Model-View-ViewModel (MVVM)
- Presentation-abstraction-control
- Naked objects
- Hierarchical Model-View-Controller
- View-Interactor-Presenter-Entity-Routing (VIPER)
### Enterprise
- Active Record - способ доступа к данным реляционных баз данных в ООП
- Business Delegate
- Composite Entity/Составная сущность
- Composite View
- DAO (Data Access Object) - объект доступа к данным
- Dispatcher View
- Front Controller
- Intercepting Filter
- Registry
- Service Activator
- Service Locator/Локатор служб
- Service to Worker
- Session Facade/Фасад Сессии
- Transfer Object Assembler
- Transfer Object/Объект перемещения
- Value List Handler/Обработчик Списка Значений
- View Helper
- Unit of Work
### Шаблоны проектирования потоковой обработки
- Обработка событий по отдельности
- Обработка событий с использованием локального состояния
- Многоэтапная обработка/повторное разделение на разделы
- Обработка с применением внешнего справочника: соединение потока данных с таблицей
- Соединение потоков данных
- Внеочередные события
- Повторная обработка
### Шаблоны баз данных
- Data Mapper
- Identity Map
- Unit of Work
- Lazy Load
### Прочие
- Repository/Хранилище
### Другие типы шаблонов
- Carrier Rider Mapper описывают предоставление доступа к хранимой информации
- Аналитические шаблоны описывают основной подход для составления требований для ПО (requirement analysis) до начала 
самого процесса программной разработки
- Коммуникационные шаблоны описывают процесс общения между отдельными участниками/сотрудниками организации
- Организационные шаблоны описывают организационную иерархию предприятия/фирмы
- Антипаттерны (Anti-Design-Patterns) описывают, как не следует поступать при разработке программ, показывая характерные
ошибки в дизайне и в реализации
