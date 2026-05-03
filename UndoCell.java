package tyrann3laby;

public class UndoCell {
    private final int previousValue;
    private final int value;
    private final int i;
    private final int j;
    private UndoCell previousCell;
    private UndoCell nextCell;

    private boolean expandedWidth;
    private int previousWidth;
    private int width;
    private boolean expandedHeight;
    private int previousHeight;
    private int height;

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

    public void expandWidth(int newWidth, int oldWith) {
        expandedWidth = true;
        width = newWidth;
        previousWidth = oldWith;
    }


    public void expandHeight(int newHeight, int oldHeight) {
        expandedHeight = true;
        height = newHeight;
        previousHeight = oldHeight;
    }

    public boolean isExpandedWidth() {
        return expandedWidth;
    }

    public int getPreviousWidth() {
        return previousWidth;
    }

    public int getWidth() {
        return width;
    }

    public boolean isExpandedHeight() {
        return expandedHeight;
    }

    public int getPreviousHeight() {
        return previousHeight;
    }

    public int getHeight() {
        return height;
    }
}
