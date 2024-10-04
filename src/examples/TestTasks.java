package examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        System.out.println("----------------------------------------");
        collectionMiddleTasks();
        System.out.println("----------------------------------------");
        streamMiddleTasks();
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
            } else if (current < max && current > res) {
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

    private static List<List<String>> getGroupingAnagram(List<String> list) {
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

    public static void collectionMiddleTasks() {
        // Есть список List<Integer> numbers, который может содержать дубликаты. Необходимо написать код, который
        // удаляет дубликаты, но при этом сохраняет порядок элементов.
        // Пример:
        // Input: [4, 5, 6, 7, 4, 5, 9]
        // Output: [4, 5, 6, 7, 9]
        System.out.println(deleteDuplicates(List.of(4, 5, 6, 7, 4, 5, 9)));
        System.out.println("##########################");
        // Дан список чисел. Найдите первое число, которое повторяется. Если такого числа нет, верните -1.
        // Input: [5, 3, 4, 3, 5, 6]
        // Output: 3
        // Input: [1, 2, 3, 4]
        // Output: -1
        System.out.println(findFirstDuplicate(List.of(5, 3, 4, 3, 5, 6)).orElse(-1));
        System.out.println(findFirstDuplicate(List.of(1, 2, 3, 4)).orElse(-1));
        System.out.println("##########################");
        // Дан массив целых чисел int[] nums и целое число k. Нужно вернуть список из k наиболее часто встречающихся
        // элементов.
        // Input: nums = [1,1,1,2,2,3], k = 2
        // Output: [1, 2]
        System.out.println(Arrays.toString(findTopK(new int[] {1, 1, 1, 2, 2, 3}, 2)));
        System.out.println(Arrays.toString(findTopK(new int[] {1, 1, 1, 2, 2}, 3)));
        System.out.println("##########################");
        // Напиши метод, который находит пересечение двух списков, включая кратность элементов (если элемент
        // встречается несколько раз в обоих списках, он должен быть в результате столько же раз).
        // Input: list1 = [1, 2, 2, 3], list2 = [2, 2, 3]
        // Output: [2, 2, 3]
        System.out.println(findIntersections(List.of(1, 2, 2, 3), List.of(2, 2, 3)));
        System.out.println(findIntersections(List.of(3, 4, 5, 1, 2, 2), List.of(2, 3, 3, 3, 1)));
        System.out.println("##########################");
        // Проверь, является ли строка палиндромом, игнорируя пробелы, регистр и небуквенные символы. Для этого
        // используй коллекции типа Deque.
        // Input: "A man, a plan, a canal, Panama"
        // Output: true
        System.out.println(isPalindrome("A man, a plan, a canal, Panama"));
        System.out.println(isPalindrome("A man, a plan, a canal, Porsche"));
    }

    private static <T> Set<T> deleteDuplicates(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Set.of();
        }

        return new LinkedHashSet<>(list);
    }

    private static <T> Optional<T> findFirstDuplicate(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }

        Set<T> seen = new HashSet<>();
        for (T t : list) {
            if (seen.contains(t)) {
                return Optional.of(t);
            }
            seen.add(t);
        }

        return Optional.empty();
    }

    private static int[] findTopK(int[] array, int k) {
        if (array == null || array.length == 0) {
            return new int[0];
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int num : array) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        Queue<Integer> priority = new PriorityQueue<>((num1, num2) -> map.get(num2) - map.get(num1));
        priority.addAll(map.keySet());
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            if (!priority.isEmpty()) {
                result[i] = priority.poll();
            } else {
                return new int[0];
            }
        }

        return result;
    }

    private static <T> List<T> findIntersections(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return List.of();
        }

        Map<T, Integer> map1 = new HashMap<>();
        for (T elem : list1) {
            map1.put(elem, map1.getOrDefault(elem, 0) + 1);
        }

        Map<T, Integer> map2 = new HashMap<>();
        for (T elem : list2) {
            map2.put(elem, map2.getOrDefault(elem, 0) + 1);
        }

        List<T> intersections = new ArrayList<>();
        for (Map.Entry<T, Integer> entry : map1.entrySet()) {
            T key = entry.getKey();
            Integer value = entry.getValue();
            Integer count = map2.get(key);
            if (count != null) {
                count = count > value ? value : count;
                for (int i = 0; i < count; i++) {
                    intersections.add(key);
                }
            }
        }

        return intersections;
    }

    private static boolean isPalindrome(String str) {
        if (str == null || str.isBlank()) {
            return true;
        }

        Deque<Character> stack = new ArrayDeque<>();
        Deque<Character> queue = new ArrayDeque<>();
        for (char ch : str.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                Character lower = Character.toLowerCase(ch);
                stack.push(lower);
                queue.add(lower);
            }
        }

        for (Character ch : stack) {
            if (!ch.equals(queue.remove())) {
                return false;
            }
        }

        return true;
    }

    public static void streamMiddleTasks() {
        // У тебя есть список объектов Employee с полями name, age, department и salary. Необходимо найти сотрудника
        // с самой высокой зарплатой среди тех, кто работает в департаменте "IT" и старше 25 лет.
        List<Employee> employees = List.of(
                new Employee("Stas", 26, "IT", 37_000),
                new Employee("Sava", 30, "HR", 35_000),
                new Employee("Slava", 21, "IT", 87_000),
                new Employee("Dima", 33, "Sales", 135_000),
                new Employee("Bogdan", 39, "IT", 300_000),
                new Employee("Kirill", 45, "IT", 250_000)
        );
        System.out.println(employees.stream()
                .filter(emp -> emp.department().equals("IT") && emp.age() > 25)
                .max(Comparator.comparingInt(Employee::salary))
                .orElseThrow());
        System.out.println("##########################");
        // Дана строка текста. Нужно разделить строку на отдельные слова, отфильтровать слова длиной менее 4 символов,
        // затем найти количество уникальных слов, игнорируя регистр.
        List<String> strings = List.of("AqA", "aQa", "Equals", "Aqua", "Qq", "qq");
        System.out.println(strings.stream()
                .filter(str -> str.length() < 4)
                .map(String::toLowerCase)
                .distinct()
                .toList());
        System.out.println("##########################");
        // Есть список объектов Person с полями name, age, и city. Нужно сгруппировать людей по городам, а затем для
        // каждого города найти самого младшего человека.
        List<Person> persons = List.of(
                new Person("Oleg", 26, "NY"),
                new Person("Fatima", 56, "LA"),
                new Person("John", 20, "Chicago"),
                new Person("Anton", 76, "NY"),
                new Person("Lera", 35, "LA"),
                new Person("Nadya", 12, "Chicago"),
                new Person("Niraz", 50, "NY"),
                new Person("Sveta", 19, "LA"),
                new Person("Dima", 24, "Chicago")
        );
        Map<String, List<Person>> groupByCities = persons.stream()
                .collect(Collectors.groupingBy(Person::city));
        groupByCities.forEach((k, v) -> System.out.println(k + ": " + getYoungestPerson(v)));
        System.out.println("##########################");
        // Есть список объектов Product с полями id, name и price. Нужно создать Map<Integer, String>, где ключом
        // будет id, а значением – имя продукта, при этом оставить только те продукты, у которых цена больше 1000.
        List<Product> products = List.of(
                new Product(1L, "Key", 1_000),
                new Product(2L, "Car", 12_000),
                new Product(3L, "Board", 25_000),
                new Product(4L, "Table", 5_000),
                new Product(5L, "Boat", 1_000),
                new Product(6L, "SAP", 500),
                new Product(7L, "Barbie", 200)
        );
        products.stream()
                .filter(product -> product.price() > 1000)
                .collect(Collectors.toMap(Product::id, Product::name))
                .forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("##########################");
        // Дан список строк, представляющих транзакции в формате "<name>,<amount>". Нужно преобразовать этот список в
        // Map<String, Double>, где ключом будет имя, а значением – общая сумма транзакций для этого человека, при этом
        // учитывать только транзакции, превышающие 500.
        // Input:
        // List<String> transactions = List.of(
        //     "John,600",
        //     "Alice,450",
        //     "John,300",
        //     "Alice,700",
        //     "Bob,800",
        //     "John,900",
        //     "Bob,200",
        //     "Alice,1000"
        // );
        // Output:
        // {
        //     "John" : 1500.0,
        //     "Alice" : 1700.0,
        //     "Bob" : 800.0
        // }
        List<String> transactions = List.of(
                "John,600",
                "Alice,450",
                "John,300",
                "Alice,700",
                "Bob,800",
                "John,900",
                "Bob,200",
                "Alice,1000"
        );
        transactions.stream()
                .map(str -> str.split(","))
                .filter(arr -> Integer.parseInt(arr[1]) > 500)
                .collect(Collectors.toMap(arr -> arr[0], arr -> Double.parseDouble(arr[1]), Double::sum))
                .forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("##########################");
        // Дан список целых чисел List<Integer> numbers и целое число targetSum. Нужно найти все уникальные подмножества
        // чисел, которые в сумме дают targetSum. Каждое число может быть использовано только один раз.
        // Пример:
        // Input: numbers = [1, 2, 3, 4, 5], targetSum = 5
        // Output: [[1, 4], [2, 3], [5]]
        System.out.println(findSubsets(List.of(1, 2, 3, 4, 5), 5));
        System.out.println("##########################");
        // Даны два списка объектов Employee с полями name, age, department и salary. Нужно объединить эти два списка,
        // отфильтровать сотрудников из департамента "Sales", а затем отсортировать по возрасту в порядке возрастания и
        // по зарплате в порядке убывания для одинакового возраста.
        // Пример:
        // Вход: Два списка сотрудников
        // Выход: Список сотрудников отсортирован по возрасту и зарплате, без сотрудников из "Sales".
        List<Employee> employees1 = List.of(
                new Employee("Stas", 25, "Sales", 500_000),
                new Employee("Oleg", 40, "Sales", 250_000),
                new Employee("Sergey", 20, "IT", 20_000),
                new Employee("Dima", 45, "IT", 65_000),
                new Employee("Misha", 24, "HR", 180_000)
        );
        List<Employee> employees2 = List.of(
                new Employee("Ramzan", 45, "Cleaning", 15_000),
                new Employee("Dasha", 20, "IT", 190_000),
                new Employee("Sveta", 23, "Sales", 250_000)
        );
        Stream.concat(employees1.stream(), employees2.stream())
                .filter(emp -> !emp.department().equals("Sales"))
                .sorted(Comparator.comparing(Employee::age)
                        .thenComparing(Comparator.comparing(Employee::salary)
                                .reversed()))
                .forEach(System.out::println);
        System.out.println("##########################");
        // Дан список строк List<String> strings. Нужно найти первую строку, которая является палиндромом, не учитывая
        // пробелы и регистр, и вернуть её. Если таких строк нет, вернуть Optional.empty().
        // Пример:
        // Input: strings = ["Hello", "A man a plan a canal Panama", "abc", "level"]
        // Output: "A man a plan a canal Panama"
        List<String> strings1 = List.of("Hello", "A man a plan a canal Panama", "abc", "level");
        Optional<String> optional = strings1.stream()
                .filter(TestTasks::isPalindromic)
                .findFirst();
        System.out.println(optional.orElseThrow());
        System.out.println("##########################");
        // Дан список строк List<String> strings. Нужно сгруппировать строки по их длине, а затем для каждой группы
        // строк посчитать общее количество гласных букв (a, e, i, o, u). Возвращаемый результат должен быть картой
        // Map<Integer, Long>, где ключ – это длина строки, а значение – общее количество гласных в строках такой длины.
        // Пример:
        // Input: strings = ["apple", "banana", "kiwi", "orange", "grape"]
        // Output: {5=3, 6=6}
        List<String> fruits = List.of("apple", "banana", "kiwi", "orange", "grape");
        Map<Integer, List<String>> fruitsLengthMap = fruits.stream()
                .collect(Collectors.groupingBy(String::length));
        fruitsLengthMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> countAvgVowels(entry.getValue())))
                .forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("##########################");
        // Дан список объектов Review с полями productId, rating (оценка от 1 до 5) и reviewText. Нужно найти продукт,
        // который получил наибольшее количество оценок 5, и вернуть его productId. Если таких продуктов несколько,
        // вернуть любой из них. Если нет ни одной оценки 5, вернуть Optional.empty().
        // Пример:
        // Input: Список отзывов с оценками
        // Output: productId, который чаще всего получал оценку 5.
        List<Review> reviews = List.of(
                new Review(1L, 5, ""),
                new Review(1L, 5, ""),
                new Review(1L, 4, ""),
                new Review(1L, 3, ""),
                new Review(1L, 5, ""),
                new Review(2L, 1, ""),
                new Review(2L, 2, ""),
                new Review(2L, 3, ""),
                new Review(2L, 5, ""),
                new Review(2L, 5, ""),
                new Review(3L, 5, ""),
                new Review(3L, 5, ""),
                new Review(3L, 1, ""),
                new Review(3L, 1, ""),
                new Review(3L, 2, ""),
                new Review(3L, 5, ""),
                new Review(3L, 5, "")
        );
        Map<Long, Integer> productsWithRatings = reviews.stream()
                .filter(review -> review.rating == 5)
                .collect(Collectors.toMap(Review::productId, Review::rating, Integer::sum));
        System.out.println(productsWithRatings.entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                        .map(Map.Entry::getKey)
                .orElseThrow());
    }

    private static Person getYoungestPerson(List<Person> list) {
        return list.stream()
                .min(Comparator.comparingInt(Person::age))
                .orElseThrow();
    }

    private static List<List<Integer>> findSubsets(List<Integer> numbers, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        findSubsetsRecursive(numbers, 0, targetSum, new ArrayList<>(), result);
        return result;
    }

    private static void findSubsetsRecursive(List<Integer> numbers, int start, int targetSum,
                                             List<Integer> current, List<List<Integer>> result) {
        if (targetSum == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (targetSum < 0) {
            return;
        }

        for (int i = start; i < numbers.size(); i++) {
            current.add(numbers.get(i));
            findSubsetsRecursive(numbers, i + 1, targetSum - numbers.get(i), current, result);
            current.remove(current.size() - 1);
        }
    }

    private static boolean isPalindromic(String str) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : str.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                stack.push(Character.toLowerCase(ch));
            }
        }

        while (stack.size() > 1) {
            Character front = stack.removeFirst();
            Character back = stack.removeLast();
            if (!front.equals(back)) {
                return false;
            }
        }

        return true;
    }

    private static double countAvgVowels(List<String> strs) {
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');
        List<Integer> countVowels = new ArrayList<>();
        int count = 0;
        for (String str : strs) {
            for (char ch : str.toCharArray()) {
                if (vowels.contains(Character.toLowerCase(ch))) {
                    count++;
                }
            }
            countVowels.add(count);
            count = 0;
        }

        return countVowels.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElseThrow();
    }

    private record Employee(String name, Integer age, String department, Integer salary) {}

    private record Person(String name, Integer age, String city) {}

    private record Product(Long id, String name, Integer price) {}

    private record Review(Long productId, Integer rating, String reviewText) {}
}
