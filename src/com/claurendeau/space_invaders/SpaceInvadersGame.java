package com.claurendeau.space_invaders;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.*;
import java.util.Random;
public class SpaceInvadersGame {

    private Canon canon;
    private Monster[] monster;
    private Missile[] missile;
    private Missile[] monsterMissile;

    private String gameState;
    private int monsterColumns;
    private int monsterRows;
    private int lives;
    private int missileCount;
    private int soundChoice;
    private int highScore;
    private int direction;
    private int score;
    private int scoreOnKill = 20;
    private int monsterCount;

    private int MONSTER_WIDTH = 35;
    private int MONSTER_HEIGHT = 35;

    private int CANON_WIDTH= 35;
    private int CANON_HEIGHT = 35;

    private int MISSILE_WIDTH = 7;
    private int MISSILE_HEIGHT = 15;

    private boolean isWinner = false;

    private Monster lastMonster;
    private Monster firstMonster;

    private Clip clip = null;

    public SpaceInvadersGame(int width, int height) {
        gameState = "innactive";
    }

    public int getHighScore(){
        return highScore;
    }

    public String getGameState(){
        return gameState;
    }

    public void setGameState(String gameState){
        this.gameState = gameState;
    }

    public Missile[] getMonsterMissile(){
        return monsterMissile;
    }

    public void newGame(int width, int height){
        canon = new Canon(width/2 - CANON_WIDTH/2, SpaceInvaders.HEIGHT - CANON_WIDTH*2, CANON_WIDTH, CANON_HEIGHT);
        missile = new Missile[3];

        gameState = "active";

        lives = 3;
        direction = 1;
        score = 0;
        soundChoice = 0;
        monsterRows = 5;
        monsterColumns = 10;
        monsterCount = monsterColumns*monsterRows;

        monsterMissile = new Missile[monsterColumns];

        createMonsters();
        loadHighScore();
    }

    public void loadHighScore() {
        try {
            FileReader fr = new FileReader("highscore.dat");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            if(line != null) {
                highScore = Integer.parseInt(line);
            }else{
                highScore = 0;
            }
            fr.close();
        }catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    public int getScore(){
        return score;
    }

    public int getMonsterCount(){
        return monsterCount;
    }

    // https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
    public synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void createMonsters(){
        int increment = 0;
        monster = new Monster[monsterRows * monsterColumns];

        for(int i = 0; i < monsterColumns; i++){
            for(int j = 0; j < monsterRows; j++){
                monster[increment] = new Monster(i * (MONSTER_WIDTH + 10) + SpaceInvaders.WIDTH/2 - (monsterColumns * MONSTER_WIDTH - monsterColumns * 10) + MONSTER_WIDTH, j * (MONSTER_HEIGHT + 10) + MONSTER_HEIGHT*2, MONSTER_WIDTH, MONSTER_HEIGHT);
                increment++;
            }
        }
    }

    public void createMissile(){
        if(missileCount + 1 > 3) {return;}

        playSound("resources/sounds/shoot.wav");

        for(int i = 0; i < missile.length; i++){
            if(missile[i] == null){
                missile[i] = new Missile(canon.getX() + canon.getWidth()/2 - MISSILE_WIDTH/2, canon.getY() - canon.getWidth()/2 - MISSILE_HEIGHT/2 , MISSILE_WIDTH, MISSILE_HEIGHT);
                missileCount++;
                break;
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public Monster[] getMonsters(){
        return monster;
    }

    public Canon getCanon(){
        return canon;
    }

    public Missile[] getMissile(){
        return missile;
    }

    public void step() {
        moveMissile();
        checkCollision();
        checkMonsterPosition();
        moveMonsterMissile();
        if(monsterCount <= 0){
            setGameState("winner");
        }
    }

    public void createMonsterMissile(){
        int increment = monsterColumns*monsterRows-1;
        int column = new Random().nextInt(10);
        int random = new Random().nextInt(5);
        if(random != 0){return;}

        loop:
        for(int i = monsterColumns-1; i >= 0; i--){
            for(int j = monsterRows-1; j >= 0; j--){
                if(column == i){
                    if(monster[increment].isAlive()){
                        for(int z = 0; z < monsterMissile.length; z++){
                            if(monsterMissile[z] == null){
                                monsterMissile[z] = new Missile(monster[increment].getX() + monster[increment].getWidth()/2 - MISSILE_WIDTH/2, monster[increment].getY() + monster[increment].getHeight() + MISSILE_HEIGHT/2, MISSILE_WIDTH, MISSILE_HEIGHT);
                                playSound("resources/sounds/shoot.wav");
                                break loop;
                            }
                        }
                    }
                }

                increment--;
            }
        }
    }

    public void moveMonsterMissile(){
        for(int i = 0; i < monsterMissile.length; i++){
            if(monsterMissile[i] != null) {
                if(monsterMissile[i].getY() < SpaceInvaders.HEIGHT) {
                    monsterMissile[i].setY(monsterMissile[i].getY() + monsterMissile[i].getHeight()/2);
                }else{
                    monsterMissile[i] = null;
                }
            }
        }
    }

    public void resetGame(){
        for(int i = 0; i < monster.length; i++){
            monsterCount = monsterColumns*monsterRows;
            monster[i].setAlive(true);
            resetMonster();
            gameState = "active";
        }
    }

    public void resetMissile(){
        for(int i = 0; i < monsterMissile.length; i++){
            monsterMissile[i] = null;
        }
    }

    public void saveScore(){
        try {
            if(score > highScore){
                FileWriter fw = new FileWriter("highscore.dat");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(Integer.toString(score));
                bw.close();
            }
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
    }

    public void checkMonsterPosition(){
        if(monster[monster.length-1].getY() + monster[monster.length-1].getHeight()+10 >= canon.getY()){
           killPlayer();
        }
    }

    public void killPlayer(){
        canon.setAlive(false);
        lives--;
        resetMonster();
        resetMissile();
        playSound("resources/sounds/explosion.wav");
        gameState = "dead";
        if(lives == 0){
            gameState = "loser";
            saveScore();
        }
    }

    public void resetMonster(){
        int increment = 0;
        for(int i = 0; i < monsterColumns; i++){
            for(int j = 0; j < monsterRows; j++) {
                monster[increment].setX(i * (monster[increment].getWidth() + 10) + SpaceInvaders.WIDTH/2 - (monsterColumns * MONSTER_WIDTH - monsterColumns * 10) + MONSTER_WIDTH);
                monster[increment].setY(j * (monster[increment].getHeight() + 10) + monster[increment].getHeight()*2);
                monster[increment].resetAnimation();
                increment++;
            }
        }
    }

    public void moveMissile(){
        for(int i = 0; i < missile.length; i++) {
            if(missile[i] != null) {
                missile[i].setY(missile[i].getY() - missile[i].getHeight());
                if(missile[i].getY() < 0) {
                    removeMissile(i);
                }
            }
        }
    }

    public void moveMonster(){
        firstMonster = monster[0];
        lastMonster = monster[monster.length - 1];
        boolean change = false;

        changeMonsterIcon();

        if(direction == 1 && lastMonster.getX() + lastMonster.getHeight() + lastMonster.getDeplacement() > SpaceInvaders.WIDTH || direction == 2 && firstMonster.getX() - firstMonster.getDeplacement() < 0 ) {
            changeDirection();
            change = true;
        }

        for(int i = 0;i < monster.length;i++) {
            if(!change) {
                monster[i].forward(direction);
            }else{
                monster[i].down();
            }

            monster[i].animateMonster();
        }
    }

    public void changeMonsterIcon(){
        for(int i = 0; i < monster.length; i++){
            if(monster[i].isExploded()){
                monster[i].setExploded(false);
                monster[i].assignIcon();
            }
        }
    }

    public void playMonsterSound(){
        playSound("resources/sounds/fastinvader"+(soundChoice+1)+".wav");
        soundChoice++;

        if(soundChoice > 3){ soundChoice = 0; }
    }


    public void checkCollision(){
        Rectangle missileBounds;
        Rectangle monsterBounds;
        Rectangle canonBounds;

        for(int i = 0; i < missile.length; i++){
            if(missile[i] != null) {
                missileBounds = new Rectangle(missile[i].getX(), missile[i].getY(), missile[i].getWidth(), missile[i].getHeight());
                for(int j = 0; j < monster.length; j++){
                    if(monster[j].isAlive()) {
                        monsterBounds = new Rectangle(monster[j].getX(), monster[j].getY(), monster[j].getWidth(), monster[j].getHeight());
                        if(missileBounds.intersects(monsterBounds)) {
                            invaderKilled(j, i);
                            System.out.println(j);
                            break;
                        }
                    }
                }
            }
        }

        canonBounds = new Rectangle(canon.getX(), canon.getY(), canon.getWidth(), canon.getHeight());
        for(int i = 0; i < monsterMissile.length; i++){
            if(monsterMissile[i] != null){
                missileBounds = new Rectangle(monsterMissile[i].getX(), monsterMissile[i].getY(), monsterMissile[i].getWidth(), monsterMissile[i].getHeight());
                if(canonBounds.intersects(missileBounds)){
                    monsterMissile[i] = null;
                    killPlayer();
                    break;
                }
            }
        }
    }

    public void invaderKilled(int monsterID, int missileID){
        monster[monsterID].setIcon("resources/images/explosion.png");
        monster[monsterID].setExploded(true);
        monster[monsterID].setAlive(false);
        monsterCount--;
        playSound("resources/sounds/invaderkilled.wav");
        removeMissile(missileID);
        score+=scoreOnKill;
    }

    public void removeMissile(int i){
        missile[i] = null;
        missileCount--;
    }

    public void changeDirection(){
        if(direction == 1){
            direction = 2;
        }else{
            direction = 1;
        }
    }

    public void setWinner(boolean winner){
        this.isWinner = winner;
    }

    public boolean isWinner(){
        return isWinner;
    }

    public String toString(){
        return "";
    }
}
