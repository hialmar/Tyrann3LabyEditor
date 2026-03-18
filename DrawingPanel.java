/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tyrann3laby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JPanel;

/**
 *
 * @author torguet
 */
public class DrawingPanel extends JPanel {
    
    private static final int HEIGHT = 22;
    private static final int WIDTH = 32;
    private static final int CELL_SIZE = 30;
    private int laby[][] = new int[HEIGHT][WIDTH];

    private int currentValue = 1;
    
    public DrawingPanel() {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(WIDTH*CELL_SIZE+CELL_SIZE,HEIGHT*CELL_SIZE+CELL_SIZE));
        clear();
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
                if (laby[i][j]==0) {
                } else if (laby[i][j]==1) {
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (laby[i][j]==2) {
                        g.setColor(Color.decode("0x8d6532"));
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (laby[i][j]>2 && laby[i][j]<7) {
                        g.setColor(Color.decode("0xad9562"));
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (laby[i][j]>=7 && laby[i][j]<=10 || laby[i][j]==51 || laby[i][j]==52) {
                        g.setColor(Color.CYAN);
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (laby[i][j]>30 && laby[i][j]<51) {
                        g.setColor(Color.RED);
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (laby[i][j]==99) {   
                        g.setColor(Color.GREEN);
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                        g.setColor(Color.YELLOW);
                        g.fillRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j*CELL_SIZE+CELL_SIZE, i*CELL_SIZE+CELL_SIZE, CELL_SIZE, CELL_SIZE);
                if (laby[i][j]>1) 
                    g.drawString(""+laby[i][j], j*CELL_SIZE+10+CELL_SIZE, i*CELL_SIZE+20+CELL_SIZE);
            }
        }          
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
        while((line = file.readLine()) != null) {
            String [] tab = line.split("[ ,]");
            if (tab.length > 2 && tab[1].equals("DATA")) {
                for(int iTab = 2; iTab < tab.length; iTab++) {
                    // check if it contains a REM
                    int pos = tab[iTab].indexOf('\'');
                    if(pos != -1) {
                        // check if there is a value just before the '
                        try {
                            laby[i][j] = Integer.parseInt(tab[iTab].substring(0, pos));
                            j++;
                        } catch(NumberFormatException e) {
                        }
                        // skip the comment
                        break;
                    } else {
                        // try to read the value
                        try {
                            laby[i][j] = Integer.parseInt(tab[iTab]);
                            j++;
                        } catch(NumberFormatException e) {
                        }
                    }
                }
                i++;
                j=0;
            }
        }
        file.close();
        repaint();
    }

}
