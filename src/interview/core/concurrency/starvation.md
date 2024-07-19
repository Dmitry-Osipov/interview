# Starvation
## Оглавление
- [Описание](#описание)
- [Пример](#пример)
- [Fairness (справедливость)](#fairness-справедливость)
## Описание
Голодание от блокировок отличается тем, что потоки не заблокированы, а им просто не хватает ресурсов на всех, поэтому 
пока одни потоки берут на себя всё время выполнения, другие не могут выполниться.

Голодание - это ситуация, когда поток постоянно игнорирует попытку завладеть внутренней блокировкой в пользу других 
потоков.

Из документации Oracle:
```
Голодание описывает ситуцацию, когда поток не может получить регулярный доступ к общим ресурсам и не в состоянии 
добиться прогресса. Это происходит, когда общие ресурсы надолго становятся недоступными для "жадных" потоков. Например, 
предположим, что объект предоставляет синхронизированный метод, возврат которого часто занимает много времени. Если один 
поток часто вызывает этот метод, то другие потоки, которым также необходим частый синхронизированный доступ к тому же 
объекту, будут часто блокироваться.
```
## Пример
Попробуем разобраться на примере. Создадим 5 потоков и отобразить прогресс каждого потока на Swing JProgressBar:
```java
public class StarvationDemo {
    private static Object sharedObj = new Object();

    public static void main (String[] args) {
        JFrame frame = createFrame();
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));

        for (int i = 0; i < 5; i++) {
            ProgressThread progressThread = new ProgressThread();
            frame.add(progressThread.getProgressComponent());
            progressThread.start();
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame () {
        JFrame frame = new JFrame("Starvation Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(300, 200));
        return frame;
    }

    private static class ProgressThread extends Thread {
        JProgressBar progressBar;

        ProgressThread () {
            progressBar = new JProgressBar();
            progressBar.setString(this.getName());
            progressBar.setStringPainted(true);
        }

        JComponent getProgressComponent () {
            return progressBar;
        }

        @Override
        public void run () {

            int c = 0;
            while (true) {
                synchronized (sharedObj) {
                    if (c == 100) {
                        c = 0;
                    }
                    progressBar.setValue(++c);
                    try {
                        //sleep the thread to simulate long running task
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
```
При запуске увидим, что все потоки не имеют равных шансов на процессорное время. Некоторые потоки долгое время голодают, 
чтобы потом получить блокировку.
## Fairness (справедливость)
Справедливость - это ситуация, когда все потоки имеют равные возможности для получения внутренней блокировки.

Как мы увидели на примере выше, у долго выполняющегося потока больше шансов снова последовательно получить блокировку.

В общем случае мы не можем предсказать, как базовый планировщик потоков (встроенный в ОС) выберет следующий поток для 
получения блокировки.

С точки зрения разработчика, код не должен удерживать блокировку долгое время, чтобы поток не стал жадным.

Мы можем исправить приведённый выше код, используя метод wait(), который освобождает блокировку, но переходит в 
состояние ожидания, т.е. планировщик не может выбрать его для повторного получения блокировки. Таким образом, другие 
потоки получат равные возможности для выполнения:
```java
public class FairnessDemo {
    private static Object sharedObj = new Object();

    public static void main (String[] args) {
        JFrame frame = createFrame();
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));

        for (int i = 0; i < 5; i++) {
            ProgressThread progressThread = new ProgressThread();
            frame.add(progressThread.getProgressComponent());
            progressThread.start();
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame () {
        JFrame frame = new JFrame("Fairness Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(300, 200));
        return frame;
    }

    private static class ProgressThread extends Thread {
        JProgressBar progressBar;

        ProgressThread () {
            progressBar = new JProgressBar();
            progressBar.setString(this.getName());
            progressBar.setStringPainted(true);
        }

        JComponent getProgressComponent () {
            return progressBar;
        }

        @Override
        public void run () {

            int c = 0;
            while (true) {
                synchronized (sharedObj) {
                    if (c == 100) {
                        c = 0;
                    }
                    progressBar.setValue(++c);
                    try {
                        //simulate long running task with wait..
                        // releasing the lock for long running task gives
                        //fair chances to run other threads
                        sharedObj.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
```
