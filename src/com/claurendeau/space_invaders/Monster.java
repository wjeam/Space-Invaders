package com.claurendeau.space_invaders;

import java.util.Random;

public class Monster extends Entity {

    private int state;
    private int type;

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
            x+=5;
        }else{
            x-=5;
        }
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
