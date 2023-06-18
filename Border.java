import javax.swing.*;
import java.awt.*;

public class Border extends JPanel {
    Color color;

    public Border(Color color) {
        setLayout(null);
        setVisible(true);

        this.color = color;
    }

    public Border(ClickableSquare cs, Color color) {
        setLayout(null);
        setVisible(true);

        this.color = color;

        int x = cs.x - 1;
        int y = cs.y - 1;
        int width = cs.width + 2;
        int height = cs.height + 2;

        setBounds(x, y , width, height);
    }

    public Border(ClickableSquare cs, int nr1, int nr2, int nr3, int nr4, Color color) {
        //in caz ca ii ceva in neregula se fac ajustari manuale la grosime
        setLayout(null);
        setVisible(true);

        this.color = color;

        int x = cs.x - nr1 - 1;
        int y = cs.y - nr2 - 1;
        int width = cs.width + nr3 + 2;
        int height = cs.height + nr4 + 2;

        setBounds(x, y , width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
