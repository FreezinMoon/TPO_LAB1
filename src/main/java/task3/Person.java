package main.java.task3;

public class Person {
    private String name;
    private PersonPosition position;

    public Person(String name) {
        this.name = name;
        this.position = PersonPosition.FAR; // Допустим, изначально персонаж далеко
    }

    public String getName() {
        return name;
    }

    public PersonPosition getPosition() {
        return position;
    }

    /**
     * Приблизиться к телу (если персонаж еще FAR).
     * Если уже NEAR - логика может быть разной: либо ничего не делаем, либо бросаем исключение.
     */
    public void approach(Body body) {
        if (this.position == PersonPosition.NEAR) {
            // Допустим, выбросим исключение, раз он уже рядом
            throw new IllegalStateException("Персонаж уже рядом с телом!");
        }
        // Если тело что-то держит (килобац), мы просто сближаемся.
        // Пока ничего особого не делаем: "он подошел вплотную".
        this.position = PersonPosition.NEAR;
    }

    /**
     * Поставить ногу на указанный Kilobac.
     * Условия:
     *  - Персонаж должен быть NEAR
     *  - Если тело двигается (MOVING), например, мы запретим
     *  - Иначе устанавливаем kilobac.setSteppedOn(true)
     */
    public void placeFootOn(Kilobac kilobac, Body body) {
        if (this.position != PersonPosition.NEAR) {
            throw new IllegalStateException("Персонаж слишком далеко, чтобы поставить ногу на предмет!");
        }
        if (body.getState() == BodyState.MOVING) {
            throw new IllegalStateException("Тело двигается, слишком опасно ставить ногу на предмет!");
        }
        // Допустим, тело не отпускает предмет автоматически.
        // Но мы всё равно "придавливаем" kilobac.
        kilobac.setSteppedOn(true);

        // Если хотите "освободить" предмет из рук тела при нажатии, можно так:
        // kilobac.setHeldByBody(false);
        // body.setItem(null);

        // Иная логика по желанию.
    }
}