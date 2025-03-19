package test.java.task2;

import main.java.task2.LeftistHeap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class LeftistHeapTest {

    /**
     * 1. Тест базовой вставки и сравнения трассировки (как раньше).
     */
    @Test
    public void testInsertTrace() {
        LeftistHeap heap = new LeftistHeap();

        // Вставляем 10
        heap.insert(10);
        // Проверяем трассировку: merge(null, newNode) -> "A"
        assertEquals(List.of("A"), heap.getTrace(),
                "После вставки первого элемента (10) должна быть точка A");

        // Вставляем 5
        heap.insert(5);
        // Ожидаем (C, D, A, E, F) — может немного отличаться
        List<String> expectedTrace5 = List.of("C", "D", "A", "E", "F");
        assertEquals(expectedTrace5, heap.getTrace(),
                "Неверная трассировка при вставке 5");

        // Вставляем 7
        heap.insert(7);
        // Примерная ожидаемая последовательность
        List<String> expectedTrace7 = List.of("C", "D", "A", "E", "F");
        assertEquals(expectedTrace7, heap.getTrace(),
                "Неверная трассировка при вставке 7");
    }

    /**
     * 2. Тест удаления минимума, включая трассировку при removeSmallest().
     */
    @Test
    public void testRemoveSmallestTrace() {
        LeftistHeap heap = new LeftistHeap();
        heap.insert(10); // => A
        heap.insert(5);  // => C, D, A, E, F
        heap.insert(7);  // => C, D, B, E, F (пример)

        // Очищаем трассировку перед удалением, чтобы смотреть только на цепочку removeSmallest().
        heap.clearTrace();

        int min = heap.removeSmallest();
        assertEquals(5, min, "Минимальным должен был быть 5");

        // Примерная цепочка: "C","D","B","E","F"
        List<String> expectedTrace = List.of("C", "D", "A", "E", "F");
        assertEquals(expectedTrace, heap.getTrace(),
                "Неверная трассировка при удалении минимума");
    }

    /**
     * 3. Тест очистки кучи clearHeap().
     * Проверяем, что в трассировке ничего не появляется.
     */
    @Test
    public void testClearHeapTrace() {
        LeftistHeap heap = new LeftistHeap();
        heap.insert(10);
        heap.insert(5);

        // Сбрасываем лог
        heap.clearTrace();
        // Вызываем очистку
        heap.clearHeap();

        assertTrue(heap.getTrace().isEmpty(),
                "При clearHeap() не должно быть характерных точек (не вызывается merge())");
        assertTrue(heap.isEmpty(),
                "После clearHeap() куча должна быть пустая");
    }

    /**
     * 4. Тест вставки нескольких элементов подряд (в возрастающем порядке),
     * проверяем, что куча корректно выдаёт самый маленький при removeSmallest().
     */
    @Test
    public void testInsertIncreasingAndRemove() {
        LeftistHeap heap = new LeftistHeap();
        int[] values = {1, 2, 3, 4, 5};

        for (int v : values) {
            heap.insert(v);
        }
        // Проверим, что минимальный — 1
        assertEquals(1, heap.removeSmallest());
        // Следующий — 2
        assertEquals(2, heap.removeSmallest());
        // и т.д.
        assertEquals(3, heap.removeSmallest());
        assertEquals(4, heap.removeSmallest());
        assertEquals(5, heap.removeSmallest());
        assertTrue(heap.isEmpty());
    }

    /**
     * 5. Тест вставки в убывающем порядке (5..1), чтобы посмотреть,
     * корректно ли обрабатывается порядок и npl.
     * Тут трассировку можно проверить, но
     * в основном смотрим, что всё удаляется в порядке возрастания.
     */
    @Test
    public void testInsertDecreasingAndRemove() {
        LeftistHeap heap = new LeftistHeap();
        int[] values = {5, 4, 3, 2, 1};

        for (int v : values) {
            heap.insert(v);
        }
        // Удаляем все, ожидая 1..5 по возрастанию
        for (int i = 1; i <= 5; i++) {
            int min = heap.removeSmallest();
            assertEquals(i, min,
                    "Очередь не выдаёт ожидаемый минимум при вставке в убывающем порядке");
        }
        assertTrue(heap.isEmpty());
    }

    /**
     * 6. Проверка на корректную работу при попытке удалить из пустой кучи.
     * Ожидается выброс исключения (IllegalStateException).
     */
    @Test
    public void testRemoveFromEmpty() {
        LeftistHeap heap = new LeftistHeap();
        assertThrows(IllegalStateException.class, heap::removeSmallest,
                "removeSmallest() должен бросать исключение, если куча пустая");
    }

    /**
     * 7. Очистка кучи несколько раз подряд (проверяем идемпотентность).
     */
    @Test
    public void testClearTwice() {
        LeftistHeap heap = new LeftistHeap();
        heap.insert(1);
        heap.insert(2);
        heap.clearHeap();
        // После первого clearHeap куча пустая
        assertTrue(heap.isEmpty());

        // Второй clearHeap не должен приводить к ошибкам
        heap.clearHeap();
        // Всё ещё пустая
        assertTrue(heap.isEmpty());
    }

    /**
     * 8. Случайный тест в диапазоне [1..100]:
     * - Генерируем 10 случайных чисел
     * - Сохраняем в массив
     * - Вставляем в кучу
     * - Удаляем все в порядке возрастания
     * - Сравниваем с отсортированным массивом
     */
    @Test
    public void testRandomInsertAndRemove() {
        LeftistHeap heap = new LeftistHeap();

        // Генерируем случайные числа
        Random rand = new Random();
        int testSize = 10;
        int[] arr = new int[testSize];
        for (int i = 0; i < testSize; i++) {
            arr[i] = rand.nextInt(100) + 1; // [1..100]
            heap.insert(arr[i]);
        }

        // Сортируем копию массива, чтобы знать ожидаемый порядок
        int[] sorted = arr.clone();
        java.util.Arrays.sort(sorted);

        // Удаляем из кучи, сравниваем
        for (int i = 0; i < testSize; i++) {
            int minFromHeap = heap.removeSmallest();
            assertEquals(sorted[i], minFromHeap,
                    "removeSmallest() вернул неверное значение при случайной последовательности");
        }
        assertTrue(heap.isEmpty());
    }
}
