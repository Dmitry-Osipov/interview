package examples.effective_java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnjoyGenericsWithCollection {
    public static void main(String[] args) {
        Stack<Number> numStack = new Stack<>();
        Iterable<Integer> ints = List.of(1, 2, 3, 4, 5);
        numStack.pushAll(ints);
        System.out.println(numStack);

        Collection<Object> objects = new ArrayList<>();
        numStack.popAll(objects);
        System.out.println(objects);

        // В этих примерах важно понимать PECS (producer extends - consumer super). Правило мнемонически связано с Get
        // and Put Principle. При взятии к себе внешняя коллекция является продюсером, соответственно, по принципу Get
        // подтип должен наследоваться от некоторого типа. А вот уже при передаче консьюмеру (другой коллекции) мы
        // должны указать super для типов консьюмера. Также нужно понимать, что все сравнения и компараторы (интерфейсы
        // Comparable и Comparator) являются консьюмерами
    }
}
