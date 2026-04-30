package tyrann3laby;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

class XORModeDisplayPanel extends JPanel
{
    private final int pixelSize = 2;
    public XORModeDisplayPanel()
    {
        setBackground(Color.BLACK);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        paintInMode(g, 0, 5,0xd5); // Use paint mode
        paintInMode(g, 0, 5+pixelSize,0x6a);
        paintInMode(g, 0, 5+2*pixelSize,0x40);
        paintInMode(g, 0, 5+3*pixelSize,0x6a);
        paintInMode(g, 0, 5+4*pixelSize,0xd5);
        paintInMode(g, 0, 5+5*pixelSize,0x6a);
    }

    /*
    .byt $d5	;1,1,0,1,0,1,0,1
	.byt $6a	;0,1,1,0,1,0,1,0
	.byt $40	;0,1,0,0,0,0,0,0
	.byt $6a	;0,1,1,0,1,0,1,0
	.byt $d5	;1,1,0,1,0,1,0,1
	.byt $6a	;0,1,1,0,1,0,1,0
     */


    private void paintInMode(Graphics g, int x, int y, int val)
    {
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
}

class XORModeDisplayFrame extends JFrame
{
    public XORModeDisplayFrame()
    {
        setTitle("XOR Mode Display");
        setSize(250, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.add(new XORModeDisplayPanel());
    }
}

public class TestXOR
{
    public static void main(String[] args)
    {
        XORModeDisplayFrame f = new XORModeDisplayFrame();
        f.setVisible(true);
    }
}