package com.claurendeau.williamjeanmireault.space_invaders;

import javax.swing.*;

public class SpaceInvaders extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public SpaceInvaders(int width, int height, SpaceInvadersGamePanel gamePanel) {
        super("Space Invaders");
        add(gamePanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args){
        SpaceInvadersGamePanel gamePanel = new SpaceInvadersGamePanel(WIDTH, HEIGHT);
        new SpaceInvaders(WIDTH, HEIGHT, gamePanel);
    }
}
