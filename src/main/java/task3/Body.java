package main.java.task3;

public class Body {
    private BodyState state;
    private Kilobac item; // Если тело что-то держит

    public Body(BodyState state, Kilobac item) {
        this.state = state;
        this.item = item;
    }

    public BodyState getState() {
        return state;
    }

    public void setState(BodyState state) {
        this.state = state;
    }

    public Kilobac getItem() {
        return item;
    }

    public void setItem(Kilobac item) {
        this.item = item;
    }

    /**
     * Проверка: тело реально "держит" предмет (если item != null и heldByBody = true).
     */
    public boolean isHoldingItem() {
        return (item != null && item.isHeldByBody());
    }
}