package practice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeLogic extends JPanel implements ActionListener {

    private final int SIZE = 400;
    private final int DOT_SIZE = 16;
    private final int DOT_COUNT = 400;

    private Image dot;
    private Image apple;

    private int appleX;
    private int appleY;

    private int[] x = new int[DOT_COUNT];
    private int[] y = new int[DOT_COUNT];

    private int dots;

    private Timer timer;

    private  boolean left = false;
    private  boolean right = true;
    private  boolean up = false;
    private  boolean down = false;
    private  boolean isInGame = true;

    public SnakeLogic() {
        setBackground(Color.GRAY);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48- i*DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createHookah();
    }

    public void createHookah() {
        appleX = new Random().nextInt(20)*DOT_SIZE;
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon hookah = new ImageIcon("bunker.png");
        apple = hookah.getImage();
        ImageIcon me = new ImageIcon("dot.png");
        dot = me.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isInGame)  {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str = "Game Over";
            Font f = new Font("Arial", Font.BOLD, 18);
            g.setColor(Color.WHITE);
            g.setFont(f);
            g.drawString(str, 150, SIZE/2);
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if (left)
            x[0] -= DOT_SIZE;
        if (right)
            x[0] += DOT_SIZE;
        if (up)
            y[0] -= DOT_SIZE;
        if (down)
            y[0] += DOT_SIZE;
    }

    public void checkHookah() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createHookah();
        }
    }

    public void checkBorders() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i])
                isInGame = false;
        }

        if (x[0] > SIZE)
            isInGame = false;
        if (x[0] < 0)
            isInGame = false;
        if (y[0] > SIZE)
            isInGame = false;
        if (y[0] < 0)
            isInGame = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isInGame) {
            checkHookah();
            checkBorders();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            } if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            } if (key == KeyEvent.VK_UP && !down) {
                right = false;
                up = true;
                left = false;
            } if (key == KeyEvent.VK_DOWN && !up) {
                right = false;
                left = false;
                down = true;
            }
        }
    }
}
