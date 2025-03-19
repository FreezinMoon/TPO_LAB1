package main.java.task3;

public class Kilobac {
    private boolean heldByBody;
    private boolean steppedOn;

    public Kilobac(boolean heldByBody) {
        this.heldByBody = heldByBody;
        this.steppedOn = false;
    }

    public boolean isHeldByBody() {
        return heldByBody;
    }

    public void setHeldByBody(boolean heldByBody) {
        this.heldByBody = heldByBody;
    }

    public boolean isSteppedOn() {
        return steppedOn;
    }

    public void setSteppedOn(boolean steppedOn) {
        this.steppedOn = steppedOn;
    }
}