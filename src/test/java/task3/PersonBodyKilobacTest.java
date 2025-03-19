package test.java.task3;

import main.java.task3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonBodyKilobacTest {

    /**
     * 1. Тест базовой ситуации из текста:
     *    - Тело лежит тихо (STILL), держит Kilobac
     *    - Персонаж издалека подходит (FAR -> NEAR)
     *    - Ставит ногу на Kilobac
     */
    @Test
    public void testBasicScenario() {
        Kilobac kilobac = new Kilobac(true); // heldByBody = true
        Body body = new Body(BodyState.STILL, kilobac);
        Person person = new Person("Hero");

        // Изначально
        assertEquals(PersonPosition.FAR, person.getPosition());
        assertTrue(body.isHoldingItem(), "Тело должно держать предмет");

        // Шаг 1: Подходим к телу
        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition(), "Персонаж должен оказаться рядом");

        // Шаг 2: Ставим ногу на Kilobac
        person.placeFootOn(kilobac, body);

        // Проверяем результаты
        assertTrue(kilobac.isSteppedOn(), "Килобац должен быть придавлен ногой");
        // По умолчанию не освобождаем из рук тела (если не прописано иное):
        assertTrue(body.isHoldingItem(), "Тело всё ещё держит предмет (по нашей логике)");
    }

    /**
     * 2. Тест: Персонаж пытается поставить ногу на предмет, будучи FAR.
     *    Ожидаем исключение (слишком далеко).
     */
    @Test
    public void testPlaceFootWhileFar() {
        Kilobac kilobac = new Kilobac(true);
        Body body = new Body(BodyState.STILL, kilobac);
        Person person = new Person("Tester");

        // Не вызываем approach, то есть остаёмся FAR.
        // Попытаемся placeFootOn
        assertThrows(IllegalStateException.class, () -> {
            person.placeFootOn(kilobac, body);
        }, "Должно быть исключение, так как персонаж слишком далеко");
    }

    /**
     * 3. Тест: Тело двигается (MOVING), персонаж уже рядом, но попытка придавить предмет — ошибка.
     */
    @Test
    public void testBodyMoving() {
        Kilobac kilobac = new Kilobac(true);
        Body body = new Body(BodyState.MOVING, kilobac);
        Person person = new Person("Tester");

        // Сначала подойдём
        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition());

        // Попробуем placeFootOn
        assertThrows(IllegalStateException.class, () -> {
            person.placeFootOn(kilobac, body);
        }, "Тело двигается, нельзя безопасно поставить ногу");
    }

    /**
     * 4. Тест: Тело не держит предмет (kilobac.heldByBody = false),
     *    он свободно лежит на земле. Персонаж спокойно придавливает.
     */
    @Test
    public void testKilobacFree() {
        Kilobac kilobac = new Kilobac(false);
        Body body = new Body(BodyState.STILL, kilobac);
        Person person = new Person("Tester");

        // Подходим
        person.approach(body);
        // Ставим ногу на свободный предмет
        person.placeFootOn(kilobac, body);

        assertTrue(kilobac.isSteppedOn(), "Kilobac должен быть придавлен ногой");
        // Тело считалось имеющим item, но 'heldByBody=false' => фактически тело не держит предмет
        assertFalse(body.isHoldingItem());
    }

    /**
     * 5. Тест: Персонаж уже NEAR, повторно approach() => исключение.
     */
    @Test
    public void testApproachTwice() {
        Kilobac kilobac = new Kilobac(true);
        Body body = new Body(BodyState.STILL, kilobac);
        Person person = new Person("Tester");

        // Первый вызов - ок
        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition());

        // Второй вызов - ждём исключения, т.к. уже NEAR
        assertThrows(IllegalStateException.class, () -> {
            person.approach(body);
        }, "Повторный подход не должен быть разрешён");
    }
}