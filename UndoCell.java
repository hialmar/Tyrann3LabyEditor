package tyrann3laby;

public class UndoCell {
    private final int previousValue;
    private final int value;
    private final int i;
    private final int j;
    private UndoCell previousCell;
    private UndoCell nextCell;

    public UndoCell(int previousValue, int value, int i, int j) {
        this.previousValue = previousValue;
        this.value = value;
        this.i = i;
        this.j = j;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    public int getValue() {
        return value;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public UndoCell getNextCell() {
        return nextCell;
    }

    public void setNextCell(UndoCell nextCell) {
        this.nextCell = nextCell;
    }

    public UndoCell getPreviousCell() {
        return previousCell;
    }

    public void setPreviousCell(UndoCell previousCell) {
        this.previousCell = previousCell;
    }
}
