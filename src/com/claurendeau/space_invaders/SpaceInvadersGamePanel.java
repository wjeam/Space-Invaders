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

    private final int COEUR_WIDTH = 15;
    private final int COEUR_HEIGHT = 15;

    private BufferedImage coeur;
    private BufferedImage monstre1;
    private BufferedImage monstre2;
    private BufferedImage monstre3;
    private BufferedImage soucoupe;

    private Timer monsterTimer;

    public SpaceInvadersGamePanel(int width, int height) {
        this.width = width;
        this.height = height;

        game = new SpaceInvadersGame(width, height);

        loadResources();

        // Timer principal
        Timer timer = new Timer(20, this);
        timer.start();

        // Timer pour cadencer la vitesse des monstres
        monsterTimer = new Timer (1000, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(game.getGameState().equals("active")) {
                    game.moveMonster();
                    game.getMonsters();
                    game.createMonsterMissile();
                    game.createMonsterMissile();
                    game.playMonsterSound();
                    game.createSoucoupe();
                    changeSpeed(1000-(game.getMonsterCount()-game.getMonsterLeft())*700/game.getMonsterCount());
                }
            }
        });

        monsterTimer.start();

        this.addKeyListener(this);
        setFocusable(true);
    }

    public void changeSpeed(int speed){
        monsterTimer.setDelay(speed);
        game.setMonsterSpeed();
    }

    public void loadResources(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            coeur = ImageIO.read(new File("resources/images/coeur.png"));
            monstre1 = ImageIO.read(new File("resources/images/monstre1_2.png"));
            monstre2 = ImageIO.read(new File("resources/images/monstre2_1.png"));
            monstre3 = ImageIO.read(new File("resources/images/monstre3_1.png"));
            soucoupe = ImageIO.read(new File("resources/images/si_soucoupe.png"));
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

        if(game.getGameState().equals("innactive")){
            drawMenu(g);
        }else if (game.getGameState().equals("active")) {
            drawMissiles(g);
            drawMonsters(g);
            drawInformation(g);
            drawCanon(g);
            drawLives(g);
            drawMonsterMissile(g);
            drawSoucoupe(g);
        }else if(game.getGameState().equals("dead")){
            drawDead(g);
        }else if(game.getGameState().equals("winner")){
            drawWinner(g);
        }else if(game.getGameState().equals("loser")){
            drawLoser(g);
        }
    }

    public void drawSoucoupe(Graphics g){
        if(game.getSoucoupe() != null) {
            g.drawImage(game.getSoucoupe().getIcon(),game.getSoucoupe().getX(),game.getSoucoupe().getY(),game.getSoucoupe().getWidth(),game.getSoucoupe().getHeight(),null);
        }
    }

    public void drawMonsterMissile(Graphics g){
        for(int i = 0; i < game.getMonsterMissile().length; i++){
            if(game.getMonsterMissile()[i] != null) {
                g.drawImage(game.getMonsterMissile()[i].getIcon(), game.getMonsterMissile()[i].getX(), game.getMonsterMissile()[i].getY(), game.getMonsterMissile()[i].getWidth(), game.getMonsterMissile()[i].getHeight(),null);
            }
        }
    }

    public void drawDead(Graphics g){
        drawCenteredText(g, "VOUS ETES MORT", "Cosmic Alien", 25, Color.white);
        drawCenteredOffsetText(g, "IL VOUS RESTE "+game.getLives()+" VIES", "Cosmic Alien", 20, Color.white, 0, 30);
    }

    public void drawLoser(Graphics g){
        drawCenteredText(g, "VOUS AVEZ PERDU", "Cosmic Alien", 25, Color.white);
        drawCenteredOffsetText(g, "APPUYER SUR ESPACE POUR REJOUER", "Cosmic Alien", 20, Color.white, 0, 30);
        drawCenteredOffsetText(g, "APPUYER SUR ESCAPE POUR QUITTER", "Cosmic Alien", 15, Color.white, 0, 45);
    }

    public void drawMenu(Graphics g){
        drawCenteredOffsetText(g, "SPACE INVADERS", "Cosmic Alien",40, Color.red, 0, -45);
        drawCenteredText(g, "APPUYER SUR ESPACE POUR DEBUTER", "Cosmic Alien" , 25, Color.white);
        g.drawImage(monstre1, width/2-125, height/2+10, 35, 35, null);
        drawCenteredOffsetText(g," = 10 PTS", "Cosmic Alien",25, Color.green, 0, 45);
        g.drawImage(monstre2, width/2-125, height/2+55, 35, 35, null);
        drawCenteredOffsetText(g," = 20 PTS", "Cosmic Alien",25, Color.blue, 0, 90);
        g.drawImage(monstre3, width/2-125, height/2+100, 35, 35, null);
        drawCenteredOffsetText(g," = 30 PTS", "Cosmic Alien",25, Color.yellow, 0, 135);
        g.drawImage(soucoupe, width/2-140, height/2+145, 60, 35, null);
        drawCenteredOffsetText(g," = ??? PTS", "Cosmic Alien",25, Color.red, 10, 180);
    }

    public void drawWinner(Graphics g){
        drawCenteredText(g, "BRAVO VOUS AVEZ GAGNE!", "Cosmic Alien" , 25, Color.white);
        drawCenteredOffsetText(g, "APPUYER SUR ESPACE POUR CONTINUER", "Cosmic Alien", 20, Color.white, 0, 30);
        drawCenteredOffsetText(g, "APPUYER SUR ESCAPE QUITTER", "Cosmic Alien", 15, Color.white, 0, 45);
    }

    public void drawCenteredText(Graphics g, String textContent, String fontName, int size, Color color){
        Font font = new Font(fontName, Font.PLAIN, size);
        String text = textContent;
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, width/2 - metrics.stringWidth(text)/2, height/2 - metrics.getAscent()/2);
    }

    public void drawCenteredOffsetText(Graphics g, String textContent, String fontName, int size, Color color, int x, int y){
        Font font = new Font(fontName, Font.PLAIN, size);
        String text = textContent;
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, width/2 - metrics.stringWidth(text)/2 + x, height/2 - metrics.getAscent()/2 + y);
    }

    public void drawCanon(Graphics g){
        if(game.getCanon().isAlive()) {
            g.drawImage(game.getCanon().getIcon(),game.getCanon().getX(),height - 100, game.getCanon().getWidth(), game.getCanon().getHeight(),null);
        }
    }

    public void drawMissiles(Graphics g){
        if(game.getMissile() != null) {
            g.drawImage(game.getMissile().getIcon(), game.getMissile().getX(), game.getMissile().getY(), game.getMissile().getWidth(), game.getMissile().getHeight(),null);
        }
    }

    public void drawInformation(Graphics g){
        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 20));
        g.setColor(Color.white);
        g.drawString("Score: "+game.getScore()+" pts ", 5, 20);

        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Vie ", 10, SpaceInvaders.HEIGHT-50+12);

        g.setFont(new Font("Cosmic Alien", Font.PLAIN, 20));
        g.setColor(Color.white);
        g.drawString("High Score: "+game.getHighScore()+" pts", 5, 40);
    }

    public void drawMonsters(Graphics g){
        for(int i = 0; i < game.getMonsters().length; i++){
            if(game.getMonsters()[i].isAlive() && !game.getMonsters()[i].isExploded() || !game.getMonsters()[i].isAlive() && game.getMonsters()[i].isExploded()){
                g.drawImage(game.getMonsters()[i].getIcon(), game.getMonsters()[i].getX(), game.getMonsters()[i].getY(), game.getMonsters()[i].getWidth(), game.getMonsters()[i].getHeight(), null);
            }
        }
    }

    public void drawLives(Graphics g){
        for(int i = 0; i < 3; i++){
            if(game.getLives() >= i+1){
                g.drawImage(coeur, 50+(i*20), height-50, COEUR_WIDTH, COEUR_HEIGHT, null );
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
        if(game.getGameState().equals("active")) {
            game.step();
            game.moveSoucoupe();
            repaint();
        }
    }

    @Override public void keyTyped(KeyEvent e) {
        // Not using
    }

    @Override public void keyPressed(KeyEvent e) {
        if(game.getGameState().equals("active")) {
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(game.getCanon().getX() + game.getCanon().getWidth() + 20 < width) {
                    game.getCanon().setX(game.getCanon().getX() + 20);
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(game.getCanon().getX() - 20 >= 0) {
                    game.getCanon().setX(game.getCanon().getX() - 20);
                }
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            switch(game.getGameState()){
            case "innactive":
                game.newGame(width, height);
                break;
            case "active":
                game.createMissile();
                break;
            case "dead":
                game.setGameState("active");
                game.getCanon().setAlive(true);
                break;
            case "loser":
                game.newGame(width, height);
                break;
            case "winner":
                game.resetGame();
                break;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if(game.getGameState().equals("winner") || game.getGameState().equals("loser")){
                game.saveHighScore();
                System.exit(0);
            }
        }
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {
        // Not using
    }
}
