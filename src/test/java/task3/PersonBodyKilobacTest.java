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
        Kilobac kilobac = new Kilobac(true);
        Body body = new Body(BodyState.STILL, kilobac);
        Person person = new Person("Hero");


        assertEquals(PersonPosition.FAR, person.getPosition());
        assertTrue(body.isHoldingItem(), "Тело должно держать предмет");


        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition(), "Персонаж должен оказаться рядом");


        person.placeFootOn(kilobac, body);


        assertTrue(kilobac.isSteppedOn(), "Килобац должен быть придавлен ногой");

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

        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition());

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

        person.approach(body);

        person.placeFootOn(kilobac, body);

        assertTrue(kilobac.isSteppedOn(), "Kilobac должен быть придавлен ногой");
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

        person.approach(body);
        assertEquals(PersonPosition.NEAR, person.getPosition());

        assertThrows(IllegalStateException.class, () -> {
            person.approach(body);
        }, "Повторный подход не должен быть разрешён");
    }
}