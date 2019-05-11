package com.claurendeau.space_invaders;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;

public class SpaceInvadersGame {

    private Canon canon;
    private Monster[] monster;
    private Missile[] missile;

    private int lives;
    private int missileCount;
    private int soundChoice = 0;
    private int direction;
    private int score = 0;

    private boolean isWinner = false;

    private Rectangle missileBounds;
    private Rectangle monsterBounds;

    private final int SCORE_ON_KILL = 20;
    private final int MONSTER_ROWS = 5;
    private final int MONSTER_COLUMNS = 10;

    private Clip clip = null;

    public SpaceInvadersGame(int width, int height) {
        canon = new Canon(width/2 - 35/2, height - 50);

        missile = new Missile[3];

        lives = 3;
        direction = 1;
        createMonsters();
    //  newGame();
    }

    public void newGame(){

    }

    public int getScore(){
        return score;
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
        monster = new Monster[MONSTER_COLUMNS*MONSTER_ROWS];

        for(int i = 0; i < MONSTER_ROWS; i++){
            for(int j = 0; j < MONSTER_COLUMNS; j++){
                monster[increment] = new Monster((j * 45)+45, (i * 45)+65);
                increment++;
            }
        }
    }

    public void createMissile(){
        if(missileCount + 1 > 3) {return;}
        for(int i = 0; i < missile.length; i++){
            if(missile[i] == null){
                missile[i] = new Missile(canon.getX() + 35/2 - 10/2, canon.getY() - 35*2);
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
    }

    public void moveMissile(){
        for(int i = 0;i < missile.length;i++) {
            if(missile[i] != null) {
                missile[i].setY(missile[i].getY() - 35);
                if(missile[i].getY() < 0) {
                    removeMissile(i);
                }
            }
        }
    }

    public void moveMonster(){
        Monster firstMonster = monster[0];
        Monster lastMonster = monster[monster.length - 1];
        boolean change = false;

        changeMonsterIcon();

        if(direction == 1 && lastMonster.getX() + 35 + 15 > SpaceInvaders.WIDTH || direction == 2 && firstMonster.getX() - 15 < 0 ) {
            changeDirection();
            change = true;
        }

        for(int i = 0;i < monster.length;i++) {
            if(!change) {
                monster[i].avance(direction);
            }

            monster[i].changeState();
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
        for(int i = 0; i < missile.length; i++){
            if(missile[i] != null) {
                missileBounds = new Rectangle(missile[i].getX(), missile[i].getY(), 7,20);
                for(int j = 0; j < monster.length; j++){
                    if(monster[j].isAlive()) {
                        monsterBounds = new Rectangle(monster[j].getX(), monster[j].getY(),35,35);
                        if(missileBounds.intersects(monsterBounds)) {
                            invaderKilled(j, i);
                            System.out.println(j);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void invaderKilled(int monsterID, int missileID){
        monster[monsterID].setIcon("resources/images/explosion.png");
        monster[monsterID].setExploded(true);
        monster[monsterID].setAlive(false);
        playSound("resources/sounds/invaderkilled.wav");
        removeMissile(missileID);
        score+=SCORE_ON_KILL;
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

        shiftDownMonsters();
    }

    public void shiftDownMonsters(){
        for(int i = 0; i < monster.length; i++){
            monster[i].setY(monster[i].getY() + 45);
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
