package com.claurendeau.space_invaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpaceInvadersGamePanel extends JPanel implements KeyListener {

    private int width;
    private int height;

    public SpaceInvadersGamePanel(int width, int height){
        this.width = width;
        this.height = height;

        this.addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.red);

        for(int i = 0; i < SpaceInvadersGame.monster.length; i++){
           g.drawImage(SpaceInvadersGame.monster[i].getIcon(), SpaceInvadersGame.monster[i].getX(), SpaceInvadersGame.monster[i].getY(), 35, 35, null);
        }
    }

    @Override public void keyTyped(KeyEvent e) {

    }

    @Override public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){

        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {

        }

        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {

    }
}
