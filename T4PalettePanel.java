package tyrann3laby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static tyrann3laby.T4DrawingPanel.CELL_SIZE;

public class T4PalettePanel extends JPanel {
    private final T4DrawingPanel drawingPanel;

    public T4PalettePanel(T4DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(CELL_SIZE*10+10, CELL_SIZE*20+10));
        setBackground(Color.BLACK);
    }

    public void mousePressed(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / CELL_SIZE;
        i = (evt.getX() - 5) / CELL_SIZE;
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*20 + j;
        if (val < nbTuiles)
            drawingPanel.setCurrentValue(i*20 + j);
        else
            drawingPanel.selectMode(null);
        repaint();
    }



    public void mouseReleased(MouseEvent evt) {
    }

    public void refresh() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int j = 0;
        int i = 0;
        for(; i< nbTuiles; i++) {
            drawingPanel.drawTile(g, 5+j* CELL_SIZE, 5+(i%20)* CELL_SIZE, i);
            if ((i%20) == 19) {
                j++;
            }
        }
        // draw a red empty rectangle
        g.setColor(Color.RED);
        g.drawRect(5+j* CELL_SIZE, 5+(i%20)* CELL_SIZE, CELL_SIZE-1, CELL_SIZE-1);
    }


    public void mouseEntered(MouseEvent evt) {
        int i, j;
        j = (evt.getY() - 5) / CELL_SIZE;
        i = (evt.getX() - 5) / CELL_SIZE;
        int nbTuiles = drawingPanel.getNbTuiles()/4;
        int val = i*20 + j;
        if (val < nbTuiles)
            this.setToolTipText(String.valueOf(val));
    }

    public void mouseMoved(MouseEvent e) {
        mouseEntered(e);
    }
}
