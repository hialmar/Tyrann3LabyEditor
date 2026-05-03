/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tyrann3laby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author torguet
 */
public class T4DrawingPanel extends JPanel {

    public static final int HEIGHT = 64;
    public static final int WIDTH = 64;
    public static final int CELL_SIZE = 24;
    private static final int MAX_FONT = 1024;
    public static final int pixelSize = 2;
    private final int[][] laby = new int[HEIGHT][WIDTH];
    private int largeurLaby = 0;
    private int hauteurLaby = 0;
    private int couleurPair = 0;
    private int couleurImpair = 0;
    private final int[] quartTuiles = new int[MAX_FONT*4];
    private int nbQuartTuiles = 0;
    private final int[] tuiles = new int[MAX_FONT];
    private int nbTuiles = 0;

    private int currentValue = 1;

    private int readingIndex = 0;
    private boolean readingColors = false;
    private boolean readingQuartTuiles = false;
    private boolean readingTiles = false;
    private boolean readingMap = false;

    private UndoCell firstUndo;
    private UndoCell lastUndo;
    private UndoCell currentUndo;

    private boolean selectMode;
    private int selectStartI = -1;
    private int selectStartJ = -1;
    private int selectEndI = -1;
    private int selectEndJ = -1;

    private final int[][] copiedValues = new int[HEIGHT][WIDTH];
    private int copiedStartI = -1;
    private int copiedStartJ = -1;
    private int copiedEndI = -1;
    private int copiedEndJ = -1;

    public T4DrawingPanel() {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,HEIGHT*CELL_SIZE+CELL_SIZE));
        clear();
        setBackground(Color.BLACK);
    }

    public int getNbTuiles() {
        return nbTuiles;
    }

    public int getNbQuartTuiles() {
        return nbQuartTuiles;
    }

    public int[] getTuiles() {
        return tuiles;
    }

    public int[] getQuartTuiles() {
        return quartTuiles;
    }

    public int getCouleurImpair() {
        return couleurImpair;
    }

    public int getCouleurPair() {
        return couleurPair;
    }

    public Color[] getColors() {
        return colors;
    }

    public void clear() {
        for (int[] laby1 : laby) Arrays.fill(laby1, 0);
        Arrays.fill(tuiles, 0);
        Arrays.fill(quartTuiles, 0);
        nbQuartTuiles = 0;
        nbTuiles = 0;
        readingIndex = 0;
        readingColors = false;
        readingQuartTuiles = false;
        readingTiles = false;
        readingMap = false;
        largeurLaby = 0;
        hauteurLaby = 0;
        firstUndo = lastUndo = currentUndo = null;
        selectMode = false;
        selectStartI = -1;
        selectStartJ = -1;
        selectEndI = -1;
        selectEndJ = -1;
        copiedStartI = -1;
        copiedStartJ = -1;
        copiedEndI = -1;
        copiedEndJ = -1;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < laby.length; i++) {
            for (int j = 0; j < laby[i].length; j++) {
                g.setColor(Color.BLUE);
                if (j==0) g.drawString(""+(i+1), 5, i*CELL_SIZE+15+CELL_SIZE);
                if (i==0) g.drawString(""+(j+1), j*CELL_SIZE+5+CELL_SIZE, 15);
                if (j<largeurLaby && i < hauteurLaby) {
                    drawTile(g, j * CELL_SIZE + CELL_SIZE + 4, i * CELL_SIZE + CELL_SIZE + 4, laby[i][j]);
                    if (selectMode) {
                        if (i>=selectStartI && i <= selectEndI && j>=selectStartJ && j<= selectEndJ) {
                            g.setColor(Color.RED);
                            g.drawRect(j * CELL_SIZE + CELL_SIZE + 4, i * CELL_SIZE + CELL_SIZE + 4, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }
            }
        }
    }

    public void drawTile(Graphics g, int x, int y, int tile) {
        int tileIndex = tile*4;
        drawQuartTile(g, x,y, tuiles[tileIndex++]);
        drawQuartTile(g, x+6*pixelSize,y, tuiles[tileIndex++]);
        drawQuartTile(g, x,y+6*pixelSize, tuiles[tileIndex++]);
        drawQuartTile(g, x+6*pixelSize,y+6*pixelSize, tuiles[tileIndex]);
    }

    public void drawQuartTile(Graphics g, int x, int y, int tileIndex) {
        int quartTuileIndex = tileIndex*6;
        for(int i=0; i<6; i++)
            drawQuartTileLine(g, x, y+i*pixelSize, quartTuiles[quartTuileIndex+i]);
    }

    /*

NUMBER	STANDARD COLOR	INVERTED COLOR
0	    BLACK	        WHITE
1	    RED	            CYAN
2	    GREEN	        MAGENTA
3	    YELLOW	        BLUE
4	    BLUE	        YELLOW
5	    MAGENTA	        GREEN
6	    CYAN	        RED
7	    WHITE	        BLACK

*/
    Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.WHITE};

    Color[] invertedColors = {Color.WHITE, Color.CYAN, Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED, Color.BLACK};

    private void switchColor(Graphics g, int y, boolean inverted) {
        if (inverted) {
            if ((y/pixelSize)%2==0) {
                g.setColor(invertedColors[couleurPair]);
            } else {
                g.setColor(invertedColors[couleurImpair]);
            }
        } else {
            if ((y/pixelSize)%2==0) {
                g.setColor(colors[couleurPair]);
            } else {
                g.setColor(colors[couleurImpair]);
            }
        }
    }



    private void drawQuartTileLine(Graphics g, int x, int y, int val) {
        // Ensure we start in paint mode.
        g.setPaintMode();

        // découpage bits
        boolean inverse = (val & 0x80) > 0;
        // bit 6
        boolean bit6 = (val & 0x20) > 0;
        // bit 5
        boolean bit5 = (val & 0x10) > 0;
        // bit 4
        boolean bit4 = (val & 0x8) > 0;
        // bit 3
        boolean bit3 = (val & 0x4) > 0;
        // bit 2
        boolean bit2 = (val & 0x2) > 0;
        // bit 1
        boolean bit1 = (val & 0x1) > 0;

        switchColor(g, y, inverse);

        if (bit6)
            g.fillRect(x, y, pixelSize, pixelSize);
        if (bit5)
            g.fillRect(x+pixelSize, y, pixelSize, pixelSize);
        if (bit4)
            g.fillRect(x+2*pixelSize, y, pixelSize, pixelSize);
        if (bit3)
            g.fillRect(x+3*pixelSize, y, pixelSize, pixelSize);
        if (bit2)
            g.fillRect(x+4*pixelSize, y, pixelSize, pixelSize);
        if (bit1)
            g.fillRect(x+5*pixelSize, y, pixelSize, pixelSize);
    }



void mousePressed(MouseEvent evt) {
        int i, j;
        i = (evt.getY() - CELL_SIZE - 4) / CELL_SIZE;
        j = (evt.getX() - CELL_SIZE - 4) / CELL_SIZE;

        if(i < HEIGHT && j < WIDTH) {
            if (selectMode) {
                selectStartI = selectEndI = i;
                selectStartJ = selectEndJ = j;
            } else {
                UndoCell newCell = new UndoCell(laby[i][j], currentValue, i, j);
                if (firstUndo == null) {
                    currentUndo = lastUndo = firstUndo = newCell;
                } else {
                    lastUndo.setNextCell(newCell);
                    newCell.setPreviousCell(lastUndo);
                    currentUndo = lastUndo = newCell;
                }

                laby[i][j] = currentValue;

                if (i > hauteurLaby) {
                    newCell.expandHeight(i, hauteurLaby);
                    hauteurLaby = i;
                }
                if (j > largeurLaby) {
                    newCell.expandWidth(j, largeurLaby);
                    largeurLaby = j;
                }
            }
        }
        repaint();
    }

    public void selectMode(ActionEvent evt) {
        selectMode = true;
    }

    public void mouseDragged(MouseEvent evt) {
        if (selectMode) {
            int i, j;
            i = (evt.getY() - CELL_SIZE - 4) / CELL_SIZE;
            j = (evt.getX() - CELL_SIZE - 4) / CELL_SIZE;

            if(i < HEIGHT && j < WIDTH) {
                selectEndI = i;
                selectEndJ = j;

                repaint();
            }
        } else {
            mousePressed(evt);
        }
    }


    void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
        int i, j;
        i = (evt.getY() - CELL_SIZE - 4) / CELL_SIZE;
        j = (evt.getX() - CELL_SIZE - 4) / CELL_SIZE;
        if(i>=0 && i < HEIGHT && j >=0 && j < WIDTH) {
            this.setToolTipText(String.valueOf(laby[i][j]));
        }
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }


    public void copy() {
        for(int i = 0; i<selectEndI-selectStartI+1;i++) {
            for(int j = 0; j<selectEndJ-selectStartJ+1; j++) {
                copiedValues[i][j] = laby[selectStartI+i][selectStartJ+j];
            }
        }
        copiedStartI = selectStartI;
        copiedStartJ = selectStartJ;
        copiedEndI = selectEndI;
        copiedEndJ = selectEndJ;
    }

    public void paste() {
        if (copiedEndJ-copiedStartJ<selectEndJ-selectStartJ ||
            copiedEndI-copiedStartI<selectEndI-selectStartI)
        {
            JOptionPane.showMessageDialog(this,
                    "The destination region should be smaller or equal to the copied region of size : ("+
                            (copiedEndJ-copiedStartJ+1)+","+(copiedEndI-copiedStartI+1)+")",
                    "Region Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            RegionUndoCell newCell = new RegionUndoCell(selectStartI, selectStartJ, selectEndI, selectEndJ, laby, copiedValues);
            if (firstUndo == null) {
                currentUndo = lastUndo = firstUndo = newCell;
            } else {
                lastUndo.setNextCell(newCell);
                newCell.setPreviousCell(lastUndo);
                currentUndo = lastUndo = newCell;
            }
            newCell.redoLabyChanges(laby);
            repaint();
        }
    }


    void undo() {
        if (currentUndo != null) {
            // undo
            if (currentUndo instanceof RegionUndoCell) {
                RegionUndoCell regionUndoCell = (RegionUndoCell) currentUndo;
                regionUndoCell.undoLabyChanges(laby);
            } else {
                laby[currentUndo.getI()][currentUndo.getJ()] = currentUndo.getPreviousValue();
                if (currentUndo.isExpandedWidth()) {
                    largeurLaby = currentUndo.getPreviousWidth();
                }
                if (currentUndo.isExpandedHeight()) {
                    hauteurLaby = currentUndo.getPreviousHeight();
                }
            }
            repaint();
            // move
            if (currentUndo.getPreviousCell() != null)
                currentUndo = currentUndo.getPreviousCell();
        }
    }

    void redo() {
        if (currentUndo != null) {
            // undo
            if (currentUndo instanceof RegionUndoCell) {
                RegionUndoCell regionUndoCell = (RegionUndoCell) currentUndo;
                regionUndoCell.redoLabyChanges(laby);
            } else {
                laby[currentUndo.getI()][currentUndo.getJ()] = currentUndo.getValue();
                if (currentUndo.isExpandedWidth()) {
                    largeurLaby = currentUndo.getWidth();
                }
                if (currentUndo.isExpandedHeight()) {
                    hauteurLaby = currentUndo.getHeight();
                }
            }
            repaint();
            // move
            if (currentUndo.getNextCell() != null)
                currentUndo = currentUndo.getNextCell();
        }
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        this.selectMode = false;
    }

    public int[][] getLaby() {
        return laby;
    }
    
    public void saveLaby(String fileName) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter(fileName));
        
        // first find the maxI and maxJ values
        int maxI = 0;
        int maxJ = 0;
        for (int i = 0; i < laby.length; i++) {
            int tempoMaxJ=0;
            for (int j = 0; j < laby[i].length; j++) {
                if(laby[i][j]>0) {
                    if (i>maxI) maxI = i;
                    if (j>tempoMaxJ) tempoMaxJ = j;
                }
            }
            if(tempoMaxJ > maxJ) maxJ = tempoMaxJ;
        }
        
        // save the laby
        for (int i = 0; i < maxI+1; i++) {
            file.print((1000+i)+" DATA ");
            for (int j = 0; j < maxJ+1; j++) {
                if (laby[i][j] < 10)
                    file.print(" ");
                file.print(laby[i][j]);
                if(j+1<maxJ+1)
                    file.print(",");
            }
            file.println();
        }
        file.close();
    }



    public void loadLaby(String fileName) throws IOException {
        clear();
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        // read the laby
        String line;
        while((line = file.readLine()) != null) {
            if (line.startsWith("hires_et_atributs")) {
                readingColors = true;
            } else if(line.startsWith("_L00")) {
                readingMap = true;
                System.out.println("Début Map");
            } else if(line.startsWith("dta_car_redef_p1")) {
                endReadingTuiles();
                readingQuartTuiles = true;
                System.out.println("Début Quarts de Tuiles");
                readingIndex = 0;
            }
            else if (line.startsWith("_t00")) {
                endReadingQuartTuiles();
                readingTiles = true;
                readingIndex = 0;
                System.out.println("Début Tuiles");
            }
            else if (readingMap) {
                if (line.contains(".byt")) {
                    String [] tab = line.strip().split("[$ ,]");
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
                    // System.out.println(list);
                    list.removeAll(Arrays.asList("", null));
                    // System.out.println(list);
                    largeurLaby=0;
                    for(String oct : list) {
                        if (oct.equalsIgnoreCase(".byt"))
                            continue;
                        try {
                            laby[hauteurLaby][largeurLaby] = Integer.parseInt(oct,16);
                            largeurLaby++;
                        } catch (NumberFormatException e) {
                        }
                    }
                    hauteurLaby++;
                } else if (line.contains("ptr_Lignes")) {
                    endReadingMap();
                }
            }
            else if (readingColors) {
                readingColors(line);
            }  else if (readingQuartTuiles) {
                readingQuartTuile(line);
            }
            else if (readingTiles) {
                if (line.contains(".byt")) {
                    readingTuile(line);
                } else if (line.startsWith("ptr_t")) {
                    nbTuiles = readingIndex;
                    System.out.println("Tuiles total : "+nbTuiles);
                    System.out.println("Tuiles div 4 : "+(nbTuiles/4));

                    if (nbQuartTuiles > 0) {
                        // on a tout lu
                        file.close();
                        repaint();
                        return;
                    }
                }
            }
        }
        endReadingQuartTuiles();
        endReadingTuiles();
        file.close();
        repaint();
    }

    private void readingTuile(String line) {
        String[] tab = line.strip().split("[$ ,\t]");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
        // System.out.println(list);
        list.removeAll(Arrays.asList("", null));
        // System.out.println(list);
        for (String oct : list) {
            if (oct.startsWith(";")) {// comment
                break;
            } else {
                try {
                    tuiles[readingIndex] = Integer.parseInt(oct, 16);
                    readingIndex++;
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private void readingQuartTuile(String line) {
        if (line.contains(".byt")) {
            String[] tab = line.strip().split("[$ ,\t]");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(tab));
            // System.out.println(list);
            list.removeAll(Arrays.asList("", null));
            // System.out.println(list);
            for (String oct : list) {
                if (oct.startsWith(";")) { // comment
                    break;
                } else {
                    try {
                        quartTuiles[readingIndex] = Integer.parseInt(oct, 16);
                        readingIndex++;
                        break; // 1 seul par ligne
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    private void endReadingMap() {
        System.out.println("Fin Map");
        System.out.println("Nb Colonnes "+(largeurLaby-1));
        System.out.println("Nb Lignes "+(hauteurLaby-1));
        readingMap = false;
    }

    private void readingColors(String line) {
        if(line.contains("lda")) {
            String [] tab = line.strip().split(" ");
            int couleur;
            if (tab[1].startsWith("#$")) {
                couleur = Integer.parseInt(tab[1].substring(2),16);
                System.out.println("Couleur Trouvée : "+couleur);
            } else if (tab[1].startsWith("#")) {
                couleur = Integer.parseInt(tab[1].substring(1));
                System.out.println("Couleur Trouvée : "+couleur);
            } else {
                System.err.println("Wrong color at line : "+line);
                couleur = 3;
            }
            if (readingIndex==0) {
                couleurPair = couleur;
                System.out.println("Couleur Paire : "+couleur);
                readingIndex++;
            } else {
                couleurImpair = couleur;
                System.out.println("Couleur Impaire : "+couleur);
                readingColors = false;
            }
        }
    }

    private void endReadingTuiles() {
        if (readingTiles) {
            nbTuiles = readingIndex;
            System.out.println("Tuiles total : "+nbTuiles);
            System.out.println("Tuiles div 4 : "+(nbTuiles/4));
            readingTiles = false;
        }
    }

    private void endReadingQuartTuiles() {
        if (readingQuartTuiles) {
            nbQuartTuiles = readingIndex;
            System.out.println("Quart de tuiles total : " + nbQuartTuiles);
            System.out.println("Quart de tuiles div 6 : " + (nbQuartTuiles / 6));
            readingQuartTuiles = false;
        }
    }

}
