package com.claurendeau.space_invaders;
import javax.swing.*;

public class SpaceInvaders extends JFrame {

    static final int WIDTH = 750;
    static final int HEIGHT = 750;

    public SpaceInvaders(int width, int height, SpaceInvadersGamePanel gamePanel) {
        super("Space Invaders");
        add(gamePanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SpaceInvadersGame game = new SpaceInvadersGame(WIDTH, HEIGHT);
        SpaceInvadersGamePanel gamePanel = new SpaceInvadersGamePanel(WIDTH, HEIGHT);
        new SpaceInvaders(WIDTH, HEIGHT, gamePanel);

        while(!game.isGagnant()){
            game.step();
            gamePanel.repaint();
            sleep(1000);
        }
    }

    private static void sleep(int millis){
        try{
            Thread.sleep(millis);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
