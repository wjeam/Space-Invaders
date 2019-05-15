package com.claurendeau.williamjeanmireault.space_invaders;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class SpaceInvadersGame {

    private Canon canon;
    private Monster[] monster;
    private Missile missile;
    private Missile[] monsterMissile;
    private Soucoupe soucoupe;

    private String gameState;

    private int monsterColumns;
    private int monsterRows;

    private int lives;

    private int soundChoice;

    private int direction;

    private int score;
    private int highScore;

    private int monsterLeft;
    private int monsterCount;

    private int MONSTER_WIDTH = 35;
    private int MONSTER_HEIGHT = 35;

    private int CANON_WIDTH = 35;
    private int CANON_HEIGHT = 35;

    private int MISSILE_WIDTH = 7;
    private int MISSILE_HEIGHT = 15;

    private Monster lastMonster;
    private Monster firstMonster;

    Rectangle missileBounds;
    Rectangle monsterBounds;
    Rectangle canonBounds;
    Rectangle soucoupeBounds;

    private Clip clip = null;

    public SpaceInvadersGame(int width, int height) {
        gameState = "innactive";
    }


    public void newGame(int width, int height){
        canon = new Canon(width/2 - CANON_WIDTH/2, SpaceInvaders.HEIGHT - CANON_WIDTH*2, CANON_WIDTH, CANON_HEIGHT);

        gameState = "active";
        lives = 3;
        direction = 1;
        score = 0;
        soundChoice = 0;
        monsterRows = 5;
        monsterColumns = 10;
        monsterCount = monsterColumns*monsterRows;
        monsterLeft = monsterCount;

        monsterMissile = new Missile[monsterColumns];

        createMonsters();
        loadHighScore();
    }

    public int getHighScore(){
        return highScore;
    }

    public void loadHighScore() {
        try {
            FileReader fr = new FileReader("data/highscore.dat");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            if(line != null) {
                highScore = Integer.parseInt(line);
            }else{
                highScore = 0;
            }
            fr.close();
        }catch(Exception e){
            System.out.println("File not found, therefore no highscore data was found. Creating one at the end of the game...");
        }
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

    public int getMonsterCount(){
        return monsterCount;
    }

    public void setMonsterSpeed(){
        for(int i = 0; i < monster.length; i++){
            monster[i].setDeplacement(13+(monsterCount-monsterLeft*35/monsterCount));
        }
    }

    public void createMonsters(){
        int increment = 0;
        monster = new Monster[monsterRows * monsterColumns];

        for(int i = 0; i < monsterColumns; i++){
            for(int j = 0; j < monsterRows; j++){
                monster[increment] = new Monster(i * (MONSTER_WIDTH + 10) + SpaceInvaders.WIDTH/2 - (monsterColumns * MONSTER_WIDTH - monsterColumns * 10) + MONSTER_WIDTH, j * (MONSTER_HEIGHT + 10) + MONSTER_HEIGHT*3, MONSTER_WIDTH, MONSTER_HEIGHT);
                increment++;
            }
        }
    }

    public void checkMonsterPosition(){
        for(int i = 0; i < monster.length; i++ ){
            if(monster[i].getY() + monster[monster.length-1].getHeight()+10 >= canon.getY() && monster[i].isAlive()) {
                killPlayer();
                break;
            }
        }
    }

    public void createMonsterMissile(){
        int increment = monsterColumns*monsterRows-1;
        int column = new Random().nextInt(10);
        int random = new Random().nextInt(5);
        if(random != 0){return;}

        nestedLoop:
        for(int i = monsterColumns-1; i >= 0; i--){
            for(int j = monsterRows-1; j >= 0; j--){
                if(column == i){
                    if(monster[increment].isAlive()){
                        for(int z = 0; z < monsterMissile.length; z++){
                            if(monsterMissile[z] == null){
                                monsterMissile[z] = new Missile(monster[increment].getX() + monster[increment].getWidth()/2 - MISSILE_WIDTH/2, monster[increment].getY() + monster[increment].getHeight() + MISSILE_HEIGHT/2, MISSILE_WIDTH, MISSILE_HEIGHT);
                                playSound("resources/sounds/shoot.wav");
                                break nestedLoop;
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

    public void moveMonster(){
        firstMonster = monster[0];
        lastMonster = monster[monster.length - 1];
        boolean change = false;

        changeMonsterIcon();

        if(direction == 1 && lastMonster.getX() + lastMonster.getWidth() + lastMonster.getDeplacement() > SpaceInvaders.WIDTH || direction == 2 && firstMonster.getX() - firstMonster.getDeplacement() < 0 ) {
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

    public int getMonsterLeft(){
        return monsterLeft;
    }

    public void createSoucoupe(){
        int random;
        if(soucoupe == null) {
            random = new Random().nextInt(150);
            if(random == 0) {
                soucoupe = new Soucoupe(0, 35*2,60,35);
                soucoupe.setRandomPosition();
                playSound("resources/sounds/ufo_lowpitch.wav");
            }
        }
    }

    public void removeSoucoupe(){
        soucoupe = null;
    }


    public void moveSoucoupe(){
        if(soucoupe != null){
            if(soucoupe.getPosition().equals("left")){
                soucoupe.setX(soucoupe.getX()+5);
            }else{
                soucoupe.setX(soucoupe.getX()-5);
            }

            if(soucoupe.getX() < 0 || soucoupe.getX()+soucoupe.getWidth() > SpaceInvaders.WIDTH){
                soucoupe = null;
            }
        }
    }

    public Soucoupe getSoucoupe(){
        return soucoupe;
    }

    public void saveHighScore(){
        try {
            if(score > highScore){
                FileWriter fw = new FileWriter("data/highscore.dat");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(Integer.toString(score));
                bw.close();
            }
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
    }

    public int getScore(){
        return score;
    }

    // https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
    public synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            private String os = System.getProperty("os.name").toLowerCase();

            public void run() {
                if(os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    playSoundLinux(url);
                }else{
                    playSoundWindows(url);
                }
            }
        }).start();
    }

    public void playSoundLinux(String url){
        Clip clip = null;
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
            DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    public void playSoundWindows(String url){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void createMissile(){
        if(missile == null) {
            playSound("resources/sounds/shoot.wav");
            missile = new Missile(canon.getX() + canon.getWidth() / 2 - MISSILE_WIDTH / 2,canon.getY() - canon.getWidth() / 2 - MISSILE_HEIGHT / 2,MISSILE_WIDTH,MISSILE_HEIGHT);
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

    public Missile getMissile(){
        return missile;
    }

    public void step() {
        moveMissile();
        checkCollision();
        checkMonsterPosition();
        moveMonsterMissile();
        if(monsterLeft <= 0){
            setGameState("winner");
        }
    }

    public void resetGame(){
        monsterRows++;
        createMonsters();

        monsterLeft = monsterColumns*monsterRows;
        gameState = "active";
    }

    public void resetMissile(){
        for(int i = 0; i < monsterMissile.length; i++){
            monsterMissile[i] = null;
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
            saveHighScore();
        }
    }

    public void resetMonster(){
        int increment = 0;
        for(int i = 0; i < monsterColumns; i++){
            for(int j = 0; j < monsterRows; j++) {
                monster[increment].setX(i * (monster[increment].getWidth() + 10) + SpaceInvaders.WIDTH/2 - (monsterColumns * MONSTER_WIDTH - monsterColumns * 10) + MONSTER_WIDTH);
                monster[increment].setY(j * (monster[increment].getHeight() + 10) + monster[increment].getHeight()*3);
                monster[increment].resetAnimation();
                increment++;
            }
        }
    }

    public void moveMissile(){
        if(missile != null) {
            missile.setY(missile.getY() - missile.getHeight());
            if(missile.getY() < 0) {
                removeMissile();
            }
        }
    }

    public void playMonsterSound(){
        playSound("resources/sounds/fastinvader"+(soundChoice+1)+".wav");
        soundChoice++;

        if(soundChoice > 3){ soundChoice = 0; }
    }


    public void checkCollision(){
        canonBounds = new Rectangle(canon.getX(), canon.getY(), canon.getWidth(), canon.getHeight());

        missileMonsterCollision();
        missileSoucoupeCollision();
        missileCanonCollision();
    }

    public void missileCanonCollision(){
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

    public void missileMonsterCollision(){
        if(missile != null) {
            missileBounds = new Rectangle(missile.getX(), missile.getY(), missile.getWidth(), missile.getHeight());
            for(int i = 0; i < monster.length; i++){
                if(monster[i].isAlive()) {
                    monsterBounds = new Rectangle(monster[i].getX(), monster[i].getY(), monster[i].getWidth(), monster[i].getHeight());
                    if(missileBounds.intersects(monsterBounds)) {
                        invaderKilled(i);
                        break;
                    }
                }
            }
        }
    }

    public void missileSoucoupeCollision(){
        if(soucoupe != null && missile != null) {
            soucoupeBounds = new Rectangle(soucoupe.getX(),soucoupe.getY(),soucoupe.getWidth(),soucoupe.getHeight());
            missileBounds = new Rectangle(missile.getX(), missile.getY(), missile.getWidth(), missile.getHeight());
            if(missileBounds.intersects(soucoupeBounds)) {
                score += soucoupe.getValue();
                playSound("resources/sounds/explosion.wav");
                removeMissile();
                removeSoucoupe();
            }
        }
    }

    public void invaderKilled(int monsterID){
        score+=monster[monsterID].getValue();
        monster[monsterID].setIcon("resources/images/explosion.png");
        monster[monsterID].setExploded(true);
        monster[monsterID].setAlive(false);
        monsterLeft--;
        playSound("resources/sounds/invaderkilled.wav");
        removeMissile();
    }

    public void removeMissile(){
        missile = null;
    }

    public void changeDirection(){
        if(direction == 1){
            direction = 2;
        }else{
            direction = 1;
        }
    }

    public String toString(){
        return "";
    }
}
