package tyrann3laby;

public class RegionUndoCell extends UndoCell {
    private int selectStartI = -1;
    private int selectStartJ = -1;
    private int selectEndI = -1;
    private int selectEndJ = -1;

    private final int[][] previousValues = new int[T4DrawingPanel.HEIGHT][T4DrawingPanel.HEIGHT];
    private final int[][] currentValues = new int[T4DrawingPanel.HEIGHT][T4DrawingPanel.HEIGHT];


    public RegionUndoCell(int selectStartI, int selectStartJ, int selectEndI, int selectEndJ,
                          int[][] laby, int[][] copiedValues) {
        super(0, 0, -1, -1);
        this.selectStartI = selectStartI;
        this.selectStartJ = selectStartJ;
        this.selectEndI = selectEndI;
        this.selectEndJ = selectEndJ;

        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                previousValues[i][j] = laby[selectStartI+i][selectStartJ+j];
                currentValues[i][j] = copiedValues[i][j];
            }
        }
    }

    void undoLabyChanges(int[][] laby) {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                laby[selectStartI+i][selectStartJ+j] = previousValues[i][j];
            }
        }
    }

    void redoLabyChanges(int[][] laby) {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                laby[selectStartI+i][selectStartJ+j] = currentValues[i][j];
            }
        }
    }
}
