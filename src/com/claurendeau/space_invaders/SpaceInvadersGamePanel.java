package com.claurendeau.space_invaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpaceInvadersGamePanel extends JPanel implements KeyListener, ActionListener {

    private int width;
    private int height;

    private SpaceInvadersGame game;

    private final int CANON_WIDTH = 35;
    private final int CANON_HEIGHT = 35;
    private final int MONSTER_WIDTH = 35;
    private final int MONSTER_HEIGHT = 35;
    private final int MISSILE_WIDTH = 7;
    private final int MISSILE_HEIGHT = 20;
    private final int COEUR_WIDTH = 15;
    private final int COEUR_HEIGHT = 15;

    private boolean active;

    private BufferedImage coeur;

    public SpaceInvadersGamePanel(int width, int height) {
        this.width = width;
        this.height = height;

        game = new SpaceInvadersGame(width, height);
        active = true;

        loadResources();

        Timer timer = new Timer(20, this);
        timer.start();

        new Timer(200, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
               game.moveMonster();
            }
        }).start();


        this.addKeyListener(this);
        setFocusable(true);
    }

    public void loadResources(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            coeur = ImageIO.read(new File("resources/images/coeur.png"));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ca.ttf")));
        } catch (Exception e) {
            System.out.println("Error: "+e);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Black background
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        if(active == true) {
            drawMissiles(g);
            drawMonsters(g);
            drawText(g);
            drawCanon(g);
            drawLives(g);
        } else {

        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void drawCanon(Graphics g){
        g.drawImage(game.getCanon().getIcon(), game.getCanon().getX(), SpaceInvaders.HEIGHT - 100, CANON_WIDTH, CANON_HEIGHT, null);
    }

    public void drawMissiles(Graphics g){
        for(int i = 0; i < game.getMissile().length; i++){
            if(game.getMissile()[i] != null) {
                g.drawImage(game.getMissile()[i].getIcon(), game.getMissile()[i].getX(), game.getMissile()[i].getY(),MISSILE_WIDTH,MISSILE_HEIGHT,null);
            }
        }
    }

    public void drawText(Graphics g){
        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 20));
        g.setColor(Color.white);
        g.drawString("Score: 0 pts ", 5, 20);

        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Vie ", 10, SpaceInvaders.HEIGHT-50+12);
    }

    public void drawMonsters(Graphics g){
        for(int i = 0; i < game.getMonsters().length; i++){
            if(game.getMonsters()[i].isAlive()){
                g.drawImage(game.getMonsters()[i].getIcon(), game.getMonsters()[i].getX(), game.getMonsters()[i].getY(), CANON_WIDTH, CANON_HEIGHT, null);
            }
        }
    }

    public void drawLives(Graphics g){
        for(int i = 0; i < 3; i++){
            if(game.getLives() >= i+1){
                g.drawImage(coeur, 50+(i*20), SpaceInvaders.HEIGHT-50, COEUR_WIDTH, COEUR_HEIGHT, null );
            }
        }
    }

    public SpaceInvadersGame getGame(){
        return game;
    }

    public void setGame(SpaceInvadersGame game){
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.step();
        repaint();
    }

    @Override public void keyTyped(KeyEvent e) {

    }

    @Override public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(game.getCanon().getX() + CANON_WIDTH + 15 < SpaceInvaders.WIDTH) {
                game.getCanon().setX(game.getCanon().getX() + 15);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(game.getCanon().getX() - 15 >= 0) {
                game.getCanon().setX(game.getCanon().getX() - 15);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            game.createMissile();
        }

        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {

    }
}
