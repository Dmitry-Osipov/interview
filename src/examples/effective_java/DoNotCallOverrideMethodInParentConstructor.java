package examples.effective_java;

import java.time.Instant;

public class DoNotCallOverrideMethodInParentConstructor {
    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.overrideMe();  // Видим 2 состояния final поля
    }

    private static class Super {
        public Super() {
            overrideMe();  // Конструктор вызывает метод, который может быть переопределён
        }

        public void overrideMe() {
        }
    }

    private static class Sub extends Super {
        private final Instant instant;

        public Sub() {
            this.instant = Instant.now();
        }

        @Override
        public void overrideMe() {
            System.out.println(instant);
        }
    }
}
