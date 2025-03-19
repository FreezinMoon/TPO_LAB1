package main.java.task2;

import java.util.ArrayList;
import java.util.List;

public class LeftistHeap {
    // Лог для трассировки "A, B, C, D, E, F"
    private final List<String> trace = new ArrayList<>();
    private Node root;

    /**
     * Вставка элемента.
     */
    public void insert(int key) {
        // По желанию очищаем трассировку, чтобы фиксировать исключительно текущее действие.
        clearTrace();

        Node newNode = new Node(key);
        root = merge(root, newNode);
    }

    /**
     * Удаление минимального элемента (головы кучи).
     */
    public int removeSmallest() {
        if (root == null) {
            throw new IllegalStateException("Heap is empty");
        }
        // Сбрасываем трассировку, чтобы смотреть именно на удаление.
        clearTrace();

        int minKey = root.key;
        root = merge(root.left, root.right);
        return minKey;
    }

    /**
     * Очистка кучи: делаем её пустой.
     */
    public void clearHeap() {
        // Можно очистить трассировку для консистентности.
        clearTrace();
        root = null;
    }

    /**
     * Проверка, что куча пуста.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Получить ключ корня (минимальный), не удаляя его.
     */
    public int getRootKey() {
        if (root == null) {
            throw new IllegalStateException("Heap is empty");
        }
        return root.key;
    }

    /**
     * Возвращает копию текущей трассировки.
     */
    public List<String> getTrace() {
        return List.copyOf(trace);
    }

    /**
     * Явно очищает трассировку (если хотим вручную контролировать).
     */
    public void clearTrace() {
        trace.clear();
    }

    // ===========================
    // Внутренняя реализация merge
    // ===========================
    private Node merge(Node h1, Node h2) {
        // Точка A: если первая куча пуста
        if (h1 == null) {
            trace.add("A");
            return h2;
        }
        // Точка B: если вторая куча пуста
        if (h2 == null) {
            trace.add("B");
            return h1;
        }

        // Точка C: сравнение корней (перестановка, если h1.key > h2.key)
        trace.add("C");
        if (h1.key > h2.key) {
            Node tmp = h1;
            h1 = h2;
            h2 = tmp;
        }

        // Точка D: рекурсивное слияние правого поддерева
        trace.add("D");
        h1.right = merge(h1.right, h2);

        // Точка E: проверка npl, возможно swap(left, right)
        trace.add("E");
        if (npl(h1.left) < npl(h1.right)) {
            Node tmp = h1.left;
            h1.left = h1.right;
            h1.right = tmp;
        }

        // Точка F: пересчитываем npl
        trace.add("F");
        h1.npl = npl(h1.right) + 1;

        return h1;
    }

    /**
     * Вспомогательный метод для получения npl (null path length).
     */
    private int npl(Node node) {
        return (node == null) ? -1 : node.npl;
    }

    /**
     * Узел левосторонней кучи.
     */
    private static class Node {
        int key;
        Node left;
        Node right;
        int npl; // null path length

        Node(int key) {
            this.key = key;
            this.npl = 0;
        }
    }
}