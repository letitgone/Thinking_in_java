package section_11;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * @Author ZhangGJ
 * @Date 2019/10/07
 */
class Spots {
}


public class Frog {
    private int jumps;
    private Color color;
    private Spots spots;
    private boolean jmpr;

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int newJumps) {
        jumps = newJumps;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public Spots getSpots() {
        return spots;
    }

    public void setSpots(Spots newSpots) {
        spots = newSpots;
    }

    public boolean isJumper() {
        return jmpr;
    }

    public void setJumper(boolean j) {
        jmpr = j;
    }

    public void addActionListener(ActionListener l) {
        //...
    }

    public void removeActionListener(ActionListener l) {
        // ...
    }

    public void addKeyListener(KeyListener l) {
        // ...
    }

    public void removeKeyListener(KeyListener l) {
        // ...
    }

    // An "ordinary" public method:
    public void croak() {
        System.out.println("Ribbet!");
    }
}