package com.claurendeau.space_invaders;
import java.awt.*;

public class SpaceInvadersGame {

    private Canon canon;
    private Monster[] monster;
    private Missile[] missile;

    private int lives;

    private int missileCount;

    private boolean isWinner = false;
    private int direction;

    public SpaceInvadersGame(int width, int height) {
        canon = new Canon(width/2 - 35/2, height - 50);

        missile = new Missile[3];

        lives = 3;
        direction = 1;

        createMonsters();
    }

    public void createMonsters(){
        int increment = 0;
        monster = new Monster[50];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 5; j++){
                monster[increment] = new Monster(i * 45 + 35, j * 45 + 35);
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
                missile[i].setY(missile[i].getY() - 75);
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

        if(direction == 1 && lastMonster.getX() + 15 * 2 > SpaceInvaders.WIDTH || direction == 2 && firstMonster.getX() - 15 < 0 ) {
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

    public void checkCollision(){
        Rectangle missileBounds;
        Rectangle monsterBounds;

        for(int i = 0; i < missile.length; i++){
            if(missile[i] != null) {
                missileBounds = new Rectangle(missile[i].getX() - 7/2, missile[i].getY() - 20/2, 7,20);
                for(int j = 0; j < monster.length; j++){
                    if(monster[j].isAlive()) {
                        monsterBounds = new Rectangle(monster[j].getX() - 35/2, monster[j].getY() - 35/2,35,35);
                        if(missileBounds.intersects(monsterBounds)) {
                            System.out.println(i+" : "+j);
                            monster[j].setAlive(false);
                            removeMissile(i);
                            break;
                        }
                    }
                }
            }
        }
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
            monster[i].setY(monster[i].getY() + 35);
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
