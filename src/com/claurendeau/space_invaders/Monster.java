package com.claurendeau.space_invaders;

import java.util.Random;

public class Monster extends Entity {

    private int state;
    private int type;
    private boolean exploded = false;

    public Monster(int x, int y) {
        super(x, y);

        state = 1;

        assignIcon();
    }

    public void assignIcon() {
        type = new Random().nextInt(3)+1;
        setIcon("resources/images/monstre"+type+"_1.png");
    }

    public void avance(int direction){
        if(direction == 1){
            x+=15;
        }else{
            x-=15;
        }
    }

    public void setExploded(boolean exploded){
        this.exploded = exploded;
    }

    public boolean isExploded(){
        return exploded;
    }

    public void changeState(){
        if(state == 1){
            state = 2;
        }else{
            state = 1;
        }

        setIcon("resources/images/monstre"+type+"_"+state+".png");
    }
}
