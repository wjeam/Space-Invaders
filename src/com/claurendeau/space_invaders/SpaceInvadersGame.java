package com.claurendeau.space_invaders;

public class SpaceInvadersGame {

    public static Canon canon;
    public static Monster[] monster;
    private boolean isGagnant = false;

    public SpaceInvadersGame(int width, int height) {
        canon = new Canon(width/2, height - 50);

        createMonsters();
    }

    public void createMonsters(){
        int increment = 0;
        monster = new Monster[20];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 2; j++){
                monster[increment] = new Monster(i * 45, j * 45 + 20);
                increment++;
            }
        }
    }

    public void step(){
        int increment = 0;

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 2; j++){
                monster[increment].avance();
                increment++;
            }
        }

        System.out.println();
    }

    public void setGagnant(boolean gagnant){
        this.isGagnant = gagnant;
    }

    public boolean isGagnant(){
        return isGagnant;
    }

    public String toString(){
        return "";
    }
}
