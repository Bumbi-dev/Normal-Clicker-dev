import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Item extends JPanel {

    JLabel desc;
    Color butonColor;
    Color borderColor = Culori.border;

    Border border;
    ClickableSquare buton;
    MyConstants m = new MyConstants();

    String name = "";
    boolean isBought = false;
    int price;
    int x, y, width, height;

    public Item(Color color, int prais, String name) {
        setLayout(null);
        this.butonColor = color;
        this.name = name;
        this.price = prais;

        if(name.equals("")) {
            prais = 0;//makes price invisible
            borderColor = Culori.backround;
        }

        border =  new Border();

        if(prais != 0)
            buton = new ClickableSquare(String.valueOf(prais), color);
        else
            buton = new ClickableSquare(color);

        desc = new JLabel(name); desc.setFont(new Font("Montserrat", Font.PLAIN, 20));
        desc.setHorizontalAlignment(SwingConstants.CENTER);

        if(!name.equals("")) {
            add(buton);
            add(border);
        }
        if(!name.equals("bonus"))
            add(desc);

        /**** Functionalitate ****/
        buton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;
                if (!butonColor.equals(Culori.backround))// pushing effect
                    buton.recolor(butonColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {buton.recolor(butonColor);}
        });
    }

    public void recolor(Color color) {
        this.butonColor = color;
        buton.recolor(color);
        repaint();
    }

    public void setPrice(int price) {
        buton.setText(String.valueOf(price));
        this.price = price;
    }

    public void addText(String text) {
        text = desc.getText() + text;
        desc.setText(text);
        add(desc);
        name = text;
        repaint();
    }

    public void setText(String text) {
        desc.setText(text);
        add(desc);
        name = text;
        repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x - x % 4 + 3;//Algorithm for matching outline
        this.y = y - y % 4 + 1;
        this.width = width - width % 4 + 1;
        this.height = height - height % 4 + 1;

        reshape(this.x, this.y , this.width, this.height);

        border.setBounds(0, 30, width, height - 30); //2 pixels width
        buton.setBounds(2, 32, width - 5, height - 35);
        desc.setBounds(0, 0, width, 30);

        if(name.equals("bonus")) {
            remove(desc);
            desc.setVisible(false);
            border.setBounds(0, 0, width, height - 29);
            buton.setBounds(2, 3, width - 5, height - 34);
        }
    }

    public void update(int x1, int y1) {//when resizing the screen the item doesn't move
        super.setBounds(m.panelVariableX + x - x1, m.panelVariableY + y - y1, width, height);
    }
}