package com.claurendeau.space_invaders;

import java.util.Random;

public class Soucoupe extends Entity {

    String position;

    public Soucoupe(int x, int y, int width, int height){
        super(x, y, width, height);
        setValue(200);
        setIcon("resources/images/si_soucoupe.png");
    }

    public void setRandomPosition(){
        int random = new Random().nextInt(2);
        if(random == 1){
            position = "left";
            this.setX(0);
        }else{
            position = "right";
            this.setX(SpaceInvaders.WIDTH-getWidth());
        }
    }

    public String getPosition(){
        return position;
    }
}
