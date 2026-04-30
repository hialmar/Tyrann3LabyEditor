package tyrann3laby;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

class XORModeDisplayPanel extends JPanel
{
    public XORModeDisplayPanel()
    {
        setBackground(Color.BLACK);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Font font = new Font("sansserif", Font.BOLD, 16);
        g.setFont(font);
        g.setColor(Color.WHITE);

        g.drawString("Paint mode:", 5, 20);
        paintInMode(g, 0, 30, false); // Use paint mode

        g.setColor(Color.WHITE);
        g.fillRect(5, 170, 220, 3);

        g.drawString("XOR mode:", 5, 200);
        paintInMode(g, 0, 210, true); // Use XOR mode
    }

    private void paintInMode(Graphics g, int x, int y, boolean useXOR)
    {
        // Ensure we start in paint mode.
        g.setPaintMode();

        // Draw a red filled rectangle
        g.setColor(Color.RED);
        g.fillRect(x + 10, y + 10, 80, 30);

        // Draw a green filled rectangle, overlapping the red rectangle
        g.setColor(Color.GREEN);
        g.fillRect(x + 50, y + 20, 80, 30);

        // Draw a blue filled rectangle, overlapping the green rectangle
        g.setColor(Color.BLUE);
        g.fillRect(x + 130, y + 40, 80, 30);

        if (useXOR)
        {
            // Set XOR mode, with a green XOR color
            g.setXORMode(Color.GREEN);
        }

        // Draw another blue filled rectangle, offset from the previous blue one.
        g.fillRect(x + 90, y + 30, 80, 30);

        if (useXOR)
        {
            // Return to paint mode.
            g.setPaintMode();
        }

        // Draw a red filled rectangle
        g.setColor(Color.RED);
        g.fillRect(x + 10, y + 80, 80, 30);

        if (useXOR)
        {
            // Go into XOR mode, with an XOR color of red
            g.setXORMode(Color.RED);
        }

        // Draw a blue filled rectangle, overlapping the red rectangle
        g.setColor(Color.BLUE);
        g.fillRect(x + 50, y + 90, 80, 30);

        // Draw another blue filled rectangle slightly offset from the previous
        // blue rectangle.
        g.fillRect(x + 55, y + 95, 80, 30);
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