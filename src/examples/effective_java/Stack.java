package examples.effective_java;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<T> implements Iterable<T> {
    private StackNode<T> head;

    public void push(T t) {
        head = new StackNode<>(t, head);
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }

        StackNode<T> ex = head;
        head = ex.next;
        return ex.value;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // Из-за инвариантности дженериков важно подставить наследование типов: Number != Integer, к примеру
    public void pushAll(Iterable<? extends T> src) {
        for (T t : src) {
            push(t);
        }
    }

    // Из-за инвариантности дженериков важно подставить наследование типов: Number != Object, к примеру
    public void popAll(Collection<? super T> collection) {
        while (!isEmpty()) {
            collection.add(pop());
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private StackNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Stack is empty");
                }

                T value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        return String.format("Stack{head=%s}", head);  // Конкатенация строк дешевле, но мне лень складывать несколько
        // строк в одну
    }

    private static class StackNode<T> {
        T value;
        StackNode<T> next;

        StackNode(T value, StackNode<T> next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return String.format("StackNode{value=%s, next=%s}", value, next);
        }
    }
}
