# State Machine
## Оглавление
- [Что такое машина состояний](#что-такое-машина-состояний)
- [Компоненты машины состояний](#компоненты-машины-состояний)
- [Пример реализации машины состояний](#пример-реализации-машины-состояний)
- [Другие возможности](#другие-возможности)
- [Когда использовать?](#когда-использовать)
## Что такое машина состояний
State Machine (машина состояний) — это концепция, которая моделирует поведение системы, состоящей из множества
состояний, переходящих друг в друга в зависимости от событий. В Kotlin нет встроенной поддержки для машин состояний
как в некоторых специализированных языках или библиотеках, но их можно реализовать вручную с использованием классов,
sealed классов, enum и других подходов.
## Компоненты машины состояний
1) Состояния(States) - представляются через sealed или enum классы
2) Переходы(Transitions) - логика, которая определяет, как и когда происходит переход между состояниями
3) События(Events) - триггеры для изменения состояний, могут быть представлены через параметры функций или другие 
структуры
4) Контроллер состояний(State Controller) - код, управляющий текущим состоянием системы и вызовом переходов
## Пример реализации машины состояний
Допустим, мы моделируем работу светофора, который может находиться в одном из трёх состояний: красный, жёлтый, зелёный.
Светофор должен переключаться последовательно по кругу.

```kotlin
enum class TrafficLightState(val color: String) {
    RED("Red signal"),
    YELLOW("Yellow signal"),
    GREEN("Green signal"),
    ;
}

interface TrafficSignal {
    fun nextState(): TrafficLightState
    fun getState(): String
}

object RedSignal : TrafficSignal {
    override fun nextState() = TrafficLightState.GREEN

    override fun getState() = TrafficLightState.RED.color
}

object YellowSignal : TrafficSignal {
    override fun nextState() = TrafficLightState.RED

    override fun getState() = TrafficLightState.YELLOW.color
}

object GreenSignal : TrafficSignal {
    override fun nextState() = TrafficLightState.YELLOW

    override fun getState() = TrafficLightState.GREEN.color
}

class TrafficController(private var state: TrafficLightState = TrafficLightState.YELLOW) {
    private val signals: Map<TrafficLightState, TrafficSignal> = mapOf(
        TrafficLightState.RED to RedSignal,
        TrafficLightState.YELLOW to YellowSignal,
        TrafficLightState.GREEN to GreenSignal,
    )

    fun nextState() {
        state = signals[state]?.nextState() ?: throw IllegalStateException("Unknown traffic signal: ${state.color}")
    }

    fun showState() {
        println(signals[state]?.getState())
    }
}

fun main() {
    val controller = TrafficController(TrafficLightState.RED)

    repeat(6) {
        controller.showState()
        controller.nextState()
    }
}
```
Описание примера:
- Состояния: мы определяем три состояния светофора с помощью enum class — RED, YELLOW, и GREEN.
- Переходы: метод nextState() описывает логику, как светофор переходит от одного состояния к другому.
- События: переходы происходят при вызове функции nextState().
## Другие возможности
- Использование sealed class: Вместо enum class можно использовать sealed class.
- Асинхронные State Machine: В Kotlin также можно реализовать асинхронные машины состояний с использованием корутин.
Например, вы можете инициировать переход в другое состояние при наступлении определённых событий в другом потоке,
используя suspend функции и Flow:
```kotlin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

sealed class State {
    object Idle : State()
    object Loading : State()
    object Success : State()
    object Failure : State()
}

class StateMachine {
    var state: State = State.Idle

    suspend fun transitionToNextState() {
        state = when (state) {
            is State.Idle -> {
                println("State: Loading")
                delay(1000)  // симуляция долгой операции
                State.Loading
            }
            is State.Loading -> {
                println("State: Success")
                delay(1000)
                State.Success
            }
            is State.Success -> State.Idle
            is State.Failure -> State.Idle
        }
    }
}

fun main() = runBlocking {
    val machine = StateMachine()

    repeat(3) {
        machine.transitionToNextState()
    }
}
```
## Когда использовать?
1) Финитные автоматы: Когда система должна находиться в одном из фиксированных состояний и управлять переходами между
ними.
2) Асинхронные операции: Моделирование длительных операций (например, загрузка, обработка, успех или ошибка).
3) Игры и UI: Управление состояниями интерфейса или игровых объектов.
4) Бизнес-логика: Например, управление статусами заказов в системе электронной коммерции.
