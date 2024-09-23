package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TestTasks {
    public static void main(String[] args) {
        interview();
        System.out.println("----------------------------------------");
        streamTasks();
        System.out.println("----------------------------------------");
        streamTasksHarder();
        System.out.println("----------------------------------------");
        collectionTasks();
        System.out.println("----------------------------------------");
        collectionTasksHarder();
    }

    public static void interview() {
        List<String> letters = List.of("a", "b", "c", "d", "e", "f", "g");
        System.out.println(letters);  // Предугадать вывод - собственно, что и видим в содержимом списка

        List<String> numbers = List.of("1", "2", "3");
        numbers.stream().peek(System.out::println);  // Нет терминирующей операции - вывода не будет
    }

    public static void streamTasks() {
        // 1. Фильтрация и подсчёт чётных чисел
        // У вас есть список целых чисел. Используя Stream API, отфильтруйте чётные числа и подсчитайте их количество.
        // Пример:
        // Ввод: [1, 2, 3, 4, 5, 6]
        // Вывод: 3
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        System.out.println(numbers.stream()
                .filter(x -> x % 2 == 0)
                .count());
        System.out.println("##########################");

        // 2. Поиск максимальной строки по длине
        // Дан список строк. Используя Stream API, найдите строку с максимальной длиной.
        // Пример:
        // Ввод: ["apple", "banana", "kiwi", "strawberry"]
        // Вывод: "strawberry"
        List<String> words = List.of("apple", "banana", "kiwi", "strawberry");
        System.out.println(words.stream()
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(RuntimeException::new));
        System.out.println("##########################");

        // 3. Преобразование списка строк в список их длин
        // Дан список строк. Используя Stream API, преобразуйте его в список, содержащий длины этих строк.
        // Пример:
        // Ввод: ["cat", "elephant", "tiger"]
        // Вывод: [3, 8, 5]
        List<String> animals = List.of("cat", "elephant", "tiger");
        System.out.println(animals.stream()
                .map(String::length)
                .toList());
        System.out.println("##########################");

        // 4. Объединение списка строк в одну строку
        // Дан список строк. Используя Stream API, объедините все строки в одну, разделённую запятой.
        // Пример:
        // Ввод: ["apple", "banana", "cherry"]
        // Вывод: "apple,banana,cherry"
        List<String> fruits = List.of("apple", "banana", "cherry");
        System.out.println(fruits.stream()
                .collect(Collectors.joining(",")));  // В идеале использовать String.join
        System.out.println("##########################");

        // 5. Найти сумму квадратов нечётных чисел
        // Дан список целых чисел. Используя Stream API, найдите сумму квадратов всех нечётных чисел.
        // Пример:
        // Ввод: [1, 2, 3, 4, 5]
        // Вывод: 35 (1^2 + 3^2 + 5^2)
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        System.out.println(nums.stream()
                .filter(x -> x % 2 != 0)
                .map(x -> x * x)
                .reduce(0, Integer::sum));
        System.out.println("##########################");
    }

    public static void streamTasksHarder() {
        // 1. Группировка людей по возрасту
        // Дан список объектов Person, каждый из которых имеет два поля: имя и возраст. Используя Stream API,
        // сгруппируйте людей по возрасту.
        // Ввод:
        // List<Person> people = List.of(
        //     new Person("Alice", 30),
        //     new Person("Bob", 25),
        //     new Person("Charlie", 30),
        //     new Person("David", 25)
        // );
        //Вывод:
        // {25=[Bob, David], 30=[Alice, Charlie]}
        record Person(String name, Integer age) {}
        List<Person> persons = List.of(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 30),
                new Person("David", 25)
        );
        persons.stream()
                .collect(Collectors.groupingBy(Person::age))
                .forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("##########################");

        // 2. Найти строку с наибольшим количеством уникальных символов
        // Дан список строк. Используя Stream API, найдите строку с наибольшим количеством уникальных символов.
        // Пример:
        // Ввод: ["apple", "banana", "cherry", "date"]
        // Вывод: "banana"
        System.out.println("old:");
        List<String> fruits = List.of("apple", "banana", "cherry", "kiwi");
        Map<Integer, List<String>> strLengthMap = fruits.stream()
                .collect(Collectors.groupingBy(str -> (int) str.chars().distinct().count()));
        Integer max = strLengthMap.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElseThrow(RuntimeException::new);
        System.out.println(strLengthMap.get(max).getFirst());
        // Улучшение:
        System.out.println("new:");
        System.out.println(fruits.stream()
                .max(Comparator.comparingInt(str -> (int) str.chars().distinct().count()))
                .orElseThrow(RuntimeException::new));  // А мб и не улучшение, ибо раньше лишь раз создавал Stream
        System.out.println("##########################");

        // 3. Дан список строк (предложения). Используя Stream API, создайте карту, где ключом будет слово,
        // а значением — количество его вхождений во всех строках.
        // Пример:
        // Ввод: ["hello world", "hello", "world hello"]
        // Вывод: {"hello"=3, "world"=2}
        List<String> words = List.of("hello world", "hello", "world hello");
        System.out.println(words.stream()
                .map(str -> Arrays.asList(str.split(" ")))
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting())));
        System.out.println("##########################");

        // 4. Проверка, содержит ли список строк палиндромы
        // Дан список строк. Используя Stream API, проверьте, содержит ли он хотя бы один палиндром (строка, которая
        // читается одинаково в обе стороны).
        // Пример:
        // Ввод: ["level", "world", "java", "radar"]
        // Вывод: true (так как "level" и "radar" — палиндромы)
        List<String> palindromes = List.of("level", "world", "java", "radar");
        System.out.println("old:");
        System.out.println(palindromes.stream()
                .anyMatch(str -> {
                    var s = str.toLowerCase();
                    var sb = new StringBuilder(s);
                    return s.contentEquals(sb.reverse());
                }));
        System.out.println("##########################");

        // 5. Нахождение медианы в списке целых чисел
        // Дан список целых чисел. Используя Stream API, найдите медиану этого списка. (Медиана — это значение,
        // которое находится посередине в отсортированном списке. Если количество элементов чётное, медианой считается
        // среднее арифметическое двух средних значений).
        // Пример:
        // Ввод: [3, 1, 4, 2, 5]
        // Вывод: 3
        // Ввод: [1, 2, 3, 4]
        // Вывод: 2.5
        List<Integer> numbers = List.of(1, 2, 3, 4);
        var size = numbers.size();
        System.out.println(numbers.stream()
                .sorted()
                .mapToInt(Integer::intValue)
                .skip((size - 1) / 2)
                .limit(size % 2 == 0 ? 2 : 1)
                .average()
                .orElseThrow(RuntimeException::new));
        System.out.println("##########################");
    }

    public static void collectionTasks() {
        // 1. Поиск второго по величине элемента в коллекции
        // Дан список целых чисел. Нужно найти второй по величине элемент в коллекции, не сортируя её полностью.
        // Пример:
        // Ввод: [3, 1, 4, 2, 5]
        // Вывод: 4
        List<Integer> nums = List.of(3, 1, 4, 2, 5);
        System.out.println(findSecondMax(nums));
        System.out.println("##########################");

        // 2. Удаление дубликатов из списка с сохранением порядка
        // Дан список строк, содержащий дубликаты. Необходимо удалить все дубликаты и при этом сохранить порядок
        // появления уникальных элементов.
        // Пример:
        // Ввод: ["apple", "banana", "apple", "cherry", "banana", "date"]
        // Вывод: ["apple", "banana", "cherry", "date"]
        List<String> fruits = List.of("apple", "banana", "apple", "cherry", "banana", "date");
        System.out.println("Stream API:");
        System.out.println(fruits.stream()
                .distinct()
                .toList());

        System.out.println("Handmade:");
        List<String> distincts = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (String fruit : fruits) {
            if (!seen.contains(fruit)) {
                seen.add(fruit);
                distincts.add(fruit);
            }
        }
        System.out.println(distincts);
        System.out.println("##########################");

        // 3. Группировка строк по длине
        // Дан список строк. Нужно сгруппировать строки по их длине и вернуть карту, где ключ — длина строки, а
        // значение — список строк этой длины.
        // Пример:
        // Ввод: ["apple", "banana", "cherry", "date", "kiwi"]
        // Вывод: {4=[date, kiwi], 5=[apple], 6=[banana, cherry]}
        List<String> words = List.of("apple", "banana", "cherry", "date", "kiwi");
        System.out.println(words.stream()
                .collect(Collectors.groupingBy(String::length)));
        System.out.println("##########################");

        // 4. Проверка, содержат ли два списка одинаковые элементы
        // Даны два списка строк. Необходимо проверить, содержат ли они одинаковый набор элементов, без учёта порядка
        // и дубликатов.
        // Пример:
        // List<String> list1 = List.of("apple", "banana", "cherry", "banana");
        // List<String> list2 = List.of("cherry", "apple", "banana");
        // Вывод: true
        List<String> list1 = List.of("apple", "banana", "cherry", "banana");
        List<String> list2 = List.of("cherry", "apple", "banana");
        System.out.println(list1.containsAll(list2));
        System.out.println("##########################");

        // 5. Нахождение всех пересекающихся элементов из нескольких множеств
        // Даны несколько множеств (Set). Нужно найти все элементы, которые встречаются хотя бы в двух из этих множеств.
        // Ввод:
        // Set<Integer> set1 = Set.of(1, 2, 3, 4);
        // Set<Integer> set2 = Set.of(3, 4, 5, 6);
        // Set<Integer> set3 = Set.of(4, 6, 7, 8);
        // Вывод: [4, 3, 6]
        Set<Integer> set1 = Set.of(1, 2, 3, 4);
        Set<Integer> set2 = Set.of(3, 4, 5, 6);
        Set<Integer> set3 = Set.of(4, 6, 7, 8);
        System.out.println(findIntersections(set1, set2, set3));
        System.out.println("##########################");
    }

    private static int findSecondMax(List<Integer> nums) {
        if (nums.size() < 2) {
            throw new IllegalArgumentException("List size must be greater than 2");
        }

        int max = nums.getFirst();
        int res = nums.getFirst();
        for (int i = 1; i < nums.size(); i++) {
            int current = nums.get(i);
            if (current > max) {
                res = max;
                max = current;
            } else if (current > res) {
                res = current;
            }
        }

        return res;
    }

    @SafeVarargs
    private static Set<Integer> findIntersections(Set<Integer> ...sets) {
        Set<Integer> intersections = new HashSet<>();
        if (sets == null || sets.length == 0) {
            return intersections;
        }

        Map<Integer, Integer> counter = new HashMap<>();
        for (Set<Integer> set : sets) {
            for (Integer i : set) {
                counter.put(i, counter.getOrDefault(i, 0) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            if (entry.getValue() > 1) {
                intersections.add(entry.getKey());
            }
        }

        return intersections;
    }

    public static void collectionTasksHarder() {
        // 1. Найти пересечение двух списков
        // Напиши функцию, которая принимает два списка List<Integer> и возвращает список, содержащий только те
        // элементы, которые есть в обоих списках.
        // Условия:
        // Списки могут содержать дубликаты.
        // Порядок элементов в результирующем списке должен совпадать с порядком в первом списке.
        System.out.println(getListsIntersection(List.of(1, 2), List.of(1, 2, 2, 3)));
        System.out.println(getListsIntersection(List.of(1, 2), List.of(1, 2)));
        System.out.println("##########################");

        // 2. Удалить дубликаты из списка
        // Напиши метод, который принимает список List<String> и удаляет из него все дубликаты, сохраняя
        // порядок элементов.
        // Условия:
        // Нужно использовать коллекции Java для эффективного удаления дубликатов.
        // Порядок элементов в списке должен быть сохранён.
        System.out.println(removeDuplicates(List.of("1", "2", "3", "5", "2", "3", "4")));
        System.out.println("##########################");

        // 3. Частота появления элементов
        // Реализуй метод, который принимает список List<String> и возвращает карту Map<String, Integer>, где
        // ключ — это элемент списка, а значение — количество его появлений.
        // Условия:
        // Список может содержать дубликаты.
        // Порядок элементов в карте не важен.
        System.out.println(countElements(List.of("1", "2", "3", "4")));
        System.out.println(countElements(List.of("1", "2", "3", "3")));
        System.out.println("##########################");

        // 4. Напиши метод, который принимает два списка List<String> и проверяет, имеют ли они одинаковое содержание.
        // Условия:
        // Порядок элементов не имеет значения.
        // В каждом списке может быть несколько строк.
        // Пустые строки считаются анаграммами.
        System.out.println(isSameContent(List.of("1", "2", "3", "4"), List.of("1", "2", "3", "4")));
        System.out.println(isSameContent(List.of("1", "2", "3", "4"), List.of("1", "2", "3")));
        System.out.println("##########################");

        // 5. Группировка анаграмм
        // Напиши метод, который принимает список строк List<String> и группирует их по анаграммам. Возвращаемый
        // результат — это List<List<String>>, где каждая внутренняя коллекция содержит анаграммы.
        // Условия:
        // Каждый элемент списка должен быть помещён в соответствующую группу анаграмм.
        // Строки без анаграмм должны быть размещены в отдельные группы.
        System.out.println(getGroupingAnagram(List.of("14", "141", "3", "41")));
    }

    public static List<Integer> getListsIntersection(List<Integer> list1, List<Integer> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return List.of();
        }

        Map<Integer, Integer> counterForList1 = new LinkedHashMap<>();
        for (Integer integer : list1) {
            counterForList1.put(integer, counterForList1.getOrDefault(integer, 0) + 1);
        }

        Map<Integer, Integer> counterForList2 = new LinkedHashMap<>();
        for (Integer integer : list2) {
            counterForList2.put(integer, counterForList2.getOrDefault(integer, 0) + 1);
        }

        List<Integer> intersections;
        if (counterForList1.size() > counterForList2.size()) {
            intersections = getListsIntersection(counterForList1, counterForList2);
        } else {
            intersections = getListsIntersection(counterForList2, counterForList1);
        }

        return intersections;
    }

    private static List<Integer> getListsIntersection(Map<Integer, Integer> counter1, Map<Integer, Integer> counter2) {
        List<Integer> intersections = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counter1.entrySet()) {
            Integer value = entry.getValue();
            Integer key = entry.getKey();
            if (counter2.getOrDefault(key, Integer.MAX_VALUE) <= value) {
                intersections.add(key);
            }
        }

        return intersections;
    }

    public static List<String> removeDuplicates(List<String> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        Map<String, Integer> map = new LinkedHashMap<>();
        for (String s : list) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        return map.entrySet()
                .stream()
                .filter(entry -> entry.getValue() < 2)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<String, Integer> countElements(List<String> list) {
        if (list == null || list.isEmpty()) {
            return Map.of();
        }

        Map<String, Integer> map = new HashMap<>();
        for (String s : list) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        return map;
    }

    public static boolean isSameContent(List<String> list1, List<String> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }

        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        Map<String, Integer> map1 = getCountingMap(list1);
        Map<String, Integer> map2 = getCountingMap(list2);
        return map1.equals(map2);
    }

    private static Map<String, Integer> getCountingMap(List<String> list) {
        List<String> copy = list.stream()
                .map(String::toLowerCase)
                .toList();

        Map<String, Integer> map = new HashMap<>();
        for (String s : copy) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        return map;
    }

    public static List<List<String>> getGroupingAnagram(List<String> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        Map<String, List<String>> map = new HashMap<>();
        for (String s : list) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            map.computeIfAbsent(sorted, v -> new ArrayList<>());
            map.get(sorted).add(s);
        }

        return new ArrayList<>(map.values());
    }
}
