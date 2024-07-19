package examples;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("java:S1192")
public class Main {
    public static void main(String[] args) throws Exception {
        getMapFromCollectors();
        System.out.println("-----------------------------------");
        mapAndFlatMap();
        System.out.println("-----------------------------------");
        systemArrayCopy();
        System.out.println("-----------------------------------");
        basicExecutorService();
        System.out.println("-----------------------------------");
        forkJoinPool();
        System.out.println("-----------------------------------");
    }

    private static void mapAndFlatMap() {
        List<String> words = List.of("hello", "world", "java");
        System.out.println("words: " + words);

        List<Integer> wordsLength = words.stream()
                .map(String::length)
                .toList();
        System.out.println("wordsLength: " + wordsLength);

        List<Character> characters = words.stream()
                .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                .toList();
        System.out.println("characters: " + characters);

        System.out.println("-----------------------------------");

        List<List<Integer>> listOfLists = List.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8, 9)
        );
        System.out.println("listOfLists: " + listOfLists);

        List<Integer> allNumbers = listOfLists.stream()
                .flatMap(List::stream)
                .toList();
        System.out.println("allNumbers: " + allNumbers);

        System.out.println("----------------------------------");

        List<Person> people = List.of(
                new Person("John", Arrays.asList(new Address("New York"), new Address("Los Angeles"))),
                new Person("Jane", Arrays.asList(new Address("Chicago"), new Address("New York"))),
                new Person("Tom", Arrays.asList(new Address("San Francisco"), new Address("Chicago")))
        );
        people.forEach(System.out::println);

        List<String> uniqueCities = people.stream()
                .flatMap(person -> person.getAddresses().stream())
                .map(Address::getCity)
                .distinct()
                .toList();
        System.out.println("uniqueCities: " + uniqueCities);
    }

    private static void getMapFromCollectors() {
        List<Book> list = new ArrayList<>(
                List.of(new Book(new Author("T"), LocalDate.of(1999, 4, 1)),
                        new Book(new Author("A"), LocalDate.of(2001, 10, 3)),
                        new Book(new Author("A"), LocalDate.of(2000, 11, 1)),
                        new Book(new Author("B"), LocalDate.of(2015, 10, 10))));
        Map<Author, List<Book>> result = list.stream()
                .filter(book -> book.getPublishDate().getYear() >= 2000)
                .collect(Collectors.groupingBy(Book::getAuthor));
        result.forEach((author, books) -> System.out.println(author + " - " + books));
        System.out.println(result.get(new Author("A")));
    }

    private static void systemArrayCopy() {
        var source = new int[]{1, 2, 3, 4, 5};
        System.out.println("source: " + Arrays.toString(source));
        var destination = new int[]{0, 0, 0, 0, 0, 0};
        System.out.println("old destination: " + Arrays.toString(destination));
        // Важно:
        // 1. Если передавать массив, в который не поместятся все значения, получим ArrayIndexOutOfBoundsException.
        // 2. Если типы массивов отличаются, вылетит ArrayStoreException (даже при int[] и Integer[]).
        System.arraycopy(source, 0, destination, 1, source.length);
        System.out.println("new destination: " + Arrays.toString(destination));
    }

    private static void basicExecutorService() throws InterruptedException, ExecutionException {
        try (ExecutorService service = Executors.newFixedThreadPool(10)) {
            Runnable runnableTask = () -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            Callable<String> callableTask = () -> {
                TimeUnit.MILLISECONDS.sleep(300);
                return "Task's execution";
            };
            List<Callable<String>> callableTasks = new ArrayList<>();
            callableTasks.add(callableTask);
            callableTasks.add(callableTask);
            callableTasks.add(callableTask);

            service.execute(runnableTask);
            Future<String> future = service.submit(callableTask);
            System.out.println(future);
            String result = service.invokeAny(callableTasks);
            System.out.println(result);
            List<Future<String>> futures = service.invokeAll(callableTasks);
            System.out.println(futures);
        }
    }

    private static void forkJoinPool() throws InterruptedException {
        measureArrayRuntime();  // Работает за 13 секунд
        SimpleClass simpleClass = new SimpleClass();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.invoke(simpleClass));
        forkJoinPool.close();
        simpleClass.fork();
        System.out.println(simpleClass.join());
        System.out.println("-------------------------------------------");
        measureArrayRuntimeWithForkJoinPool();  // Работает за секунду
    }

    private static void measureArrayRuntimeWithForkJoinPool() {
        var array = getInitArray(10_000);
        var counter = new ValueSumCounter(array);
        System.out.println(new Date());
        var forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.invoke(counter));
        forkJoinPool.close();
        System.out.println(new Date());
    }

    private static void measureArrayRuntime() throws InterruptedException {
        var array = getInitArray(10_000);
        System.out.println(new Date());
        long sum = 0;
        for (int i = 0; i < array.length; i++) {
            Thread.sleep(1);
            sum += array[i];
        }
        System.out.println(sum);
        System.out.println(new Date());
    }

    private static int[] getInitArray(int capacity) {
        var array = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = 1;
        }

        return array;
    }
}

class SimpleClass extends RecursiveTask<String> {
    @Override
    protected String compute() {
        System.out.println("I am work in thread " + Thread.currentThread().getName());
        return "I am just annocent simple class";
    }
}

class ValueSumCounter extends RecursiveTask<Integer> {
    private int[] array;

    public ValueSumCounter(int[] array) {
        this.array = array;
    }

    @Override
    protected Integer compute() {
        try {
            if (array.length <= 2) {
//                System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
//                Видим работу 8 Thread
                Thread.sleep(1);
                return Arrays.stream(array).sum();
            }

            var firstHalfArrayValueCounter =
                    new ValueSumCounter(Arrays.copyOfRange(array, 0, array.length / 2));
            var secondHalfArrayValueCounter =
                    new ValueSumCounter(Arrays.copyOfRange(array, array.length / 2, array.length));
            firstHalfArrayValueCounter.fork();
            secondHalfArrayValueCounter.fork();
            return firstHalfArrayValueCounter.join() + secondHalfArrayValueCounter.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
