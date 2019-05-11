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

    private BufferedImage coeur;

    public SpaceInvadersGamePanel(int width, int height) {
        this.width = width;
        this.height = height;

        game = new SpaceInvadersGame(width, height);

        loadResources();

        // Timer principal
        Timer timer = new Timer(20, this);
        timer.start();

        // Timer pour cadencer la vitesse des monstres
        new Timer(1000, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
               game.moveMonster();
               game.getMonsters();
               game.playMonsterSound();
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

        drawMissiles(g);
        drawMonsters(g);
        drawText(g);
        drawCanon(g);
        drawLives(g);

        Toolkit.getDefaultToolkit().sync();
    }

    public void drawCanon(Graphics g){
        if(game.getCanon().isAlive()) {
            g.drawImage(game.getCanon().getIcon(),game.getCanon().getX(),SpaceInvaders.HEIGHT - 100,CANON_WIDTH,CANON_HEIGHT,null);
        }
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
        g.drawString("Score: "+game.getScore()+" pts ", 5, 20);

        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Vie ", 10, SpaceInvaders.HEIGHT-50+12);

        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 20));
        g.setColor(Color.white);
        g.drawString("High Score: 1000 pts", 5, 40);
    }

    public void drawMonsters(Graphics g){
        for(int i = 0; i < game.getMonsters().length; i++){
            if(game.getMonsters()[i].isAlive() && !game.getMonsters()[i].isExploded() || !game.getMonsters()[i].isAlive() && game.getMonsters()[i].isExploded()){
                g.drawImage(game.getMonsters()[i].getIcon(), game.getMonsters()[i].getX(), game.getMonsters()[i].getY(), MONSTER_WIDTH, MONSTER_HEIGHT, null);
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
            game.playSound("resources/sounds/shoot.wav");
        }

        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {

    }
}
