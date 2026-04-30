/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tyrann3laby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author torguet
 */
public class T4DrawingPanel extends JPanel {

    private static final int HEIGHT = 128;
    private static final int WIDTH = 128;
    private static final int CELL_SIZE = 24;
    private static final int MAX_FONT = 1024;
    private int laby[][] = new int[HEIGHT][WIDTH];
    private int couleurPair = 0;
    private int couleurImpair = 0;
    private int quartTuiles[] = new int[MAX_FONT];
    private int nbQuartTuiles = 0;
    private int tuiles[] = new int[MAX_FONT];
    private int nbTuiles = 0;

    private int currentValue = 1;

    public T4DrawingPanel() {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,HEIGHT*CELL_SIZE+CELL_SIZE));
        clear();
        setBackground(Color.BLACK);
    }
    
    public void clear() {
        for (int[] laby1 : laby) {
            for (int j = 0; j < laby1.length; j++) {
                laby1[j] = 0;
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < laby.length; i++) {
            for (int j = 0; j < laby[i].length; j++) {
                if (j==0) g.drawString(""+(i+1), 10, i*CELL_SIZE+20+CELL_SIZE);
                if (i==0) g.drawString(""+(j+1), j*CELL_SIZE+10+CELL_SIZE, 20);
                drawTile(g,j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, laby[i][j]);
            }
        }          
    }

    private void drawTile(Graphics g, int x, int y, int tile) {
        int tileIndex = tile*4;
        drawQuartTile(g, x,y, tuiles[tileIndex++]);
        drawQuartTile(g, x+6,y, tuiles[tileIndex++]);
        drawQuartTile(g, x,y+6, tuiles[tileIndex++]);
        drawQuartTile(g, x+6,y+6, tuiles[tileIndex]);
    }

    private void drawQuartTile(Graphics g, int x, int y, int tileIndex) {
        int quartTuileIndex = tileIndex*6;
        for(int i=0; i<6; i++)
            drawQuartTileLine(g, x, y+i, quartTuiles[quartTuileIndex]);
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

        final int pixelSize = 2;
        if (inverse) {
            if ((y/pixelSize)%2==0) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.YELLOW);
            }
        } else {
            if ((y/pixelSize)%2==0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
        }

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
        i = (int) ((evt.getY() - CELL_SIZE) / CELL_SIZE);
        j = (int) ((evt.getX() - CELL_SIZE) / CELL_SIZE);
        if(i < HEIGHT && j < WIDTH)
            laby[i][j] = currentValue;
        repaint();
    }

    void mouseReleased(MouseEvent evt) {
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
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
            file.print(""+(1000+i)+" DATA ");
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
        int i = 0; int j = 0;
        boolean readingColors = false;
        int readingIndex = 0;
        boolean readingQuartTuiles = false;
        boolean readingTiles = false;
        boolean readingMap = false;
        while((line = file.readLine()) != null) {
            if (line.startsWith("hires_et_atributs")) {
                readingColors = true;
            } else if (readingColors) {
                if(line.contains("lda")) {
                    String [] tab = line.strip().split(" ");
                    int couleur;
                    if (tab[1].startsWith("#$")) {
                        couleur = Integer.parseInt(tab[1].substring(2));
                    } else if (tab[1].startsWith("#")) {
                        couleur = Integer.parseInt(tab[1].substring(1));
                    } else {
                        System.err.println("Wrong color at line : "+line);
                        couleur = 3;
                    }
                    if (readingIndex==0) {
                        couleurPair = couleur;
                        readingIndex++;
                    } else {
                        couleurImpair = couleur;
                        readingColors = false;
                    }
                }
            } else if(line.startsWith("_L00")) {
                readingMap = true;
                i = 0;
                j = 0;
            } else if (readingMap) {
                if (line.contains(".byt")) {
                    String [] tab = line.strip().split("[$ ,]");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(tab));
                    // System.out.println(list);
                    list.removeAll(Arrays.asList("", null));
                    // System.out.println(list);
                    for(String oct : list) {
                        if (oct.equalsIgnoreCase(".byt"))
                            continue;
                        try {
                            laby[i][j] = Integer.parseInt(oct,16);
                            j++;
                        } catch (NumberFormatException e) {
                        }
                    }
                    i++;
                    j=0;
                } else if (line.contains("ptr_Lignes")) {
                    readingMap = false;
                }
            } else if(line.startsWith("dta_car_redef_p1")) {
                readingQuartTuiles = true;
                readingIndex = 0;
            } else if (readingQuartTuiles) {
                if (line.contains(".byt")) {
                    String[] tab = line.strip().split("[$ ,\t]");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(tab));
                    // System.out.println(list);
                    list.removeAll(Arrays.asList("", null));
                    // System.out.println(list);
                    for (String oct : list) {
                        if (oct.equalsIgnoreCase(".byt"))
                            continue;
                        try {
                            quartTuiles[readingIndex++] = Integer.parseInt(oct, 16);
                            break; // 1 seul par ligne
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (line.startsWith("_t00")) {
                    readingTiles = true;
                    readingQuartTuiles = false;
                    nbQuartTuiles = readingIndex-1;
                    readingIndex = 0;
                }
            }  else if (readingTiles) {
                if (line.contains(".byt")) {
                    String[] tab = line.strip().split("[$ ,]");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(tab));
                    // System.out.println(list);
                    list.removeAll(Arrays.asList("", null));
                    // System.out.println(list);
                    for (String oct : list) {
                        if (oct.equalsIgnoreCase(".byt"))
                            continue;
                        try {
                            tuiles[readingIndex++] = Integer.parseInt(oct, 16);
                        } catch (NumberFormatException e) {
                        }
                    }
                } else if (line.startsWith("ptr_t")) {
                    nbTuiles = readingIndex-1;
                    file.close();
                    repaint();
                    return;
                }
            }
        }
    }

}
