package com.claurendeau.space_invaders;

import java.util.Random;

public class Monster extends Entity {

    private int direction;
    private int state;
    private int type;

    public Monster(int x, int y) {
        super(x, y);

        direction = 1;
        state = 1;

        assignIcon();
    }

    public void assignIcon() {
        type = new Random().nextInt(2)+1;

        setIcon("resources/images/monstre" + type + "_1.png");
    }

    public void avance(){
        if(direction == 1){
            x+=+20;
        }else{
            x-=20;
        }

        changeType();
    }

    public void changeType(){
        if(type == 1){
            type = 2;
        }else{
            type = 1;
        }

        setIcon("resources/images/monstre"+state+"_"+type+".png");
    }
}
