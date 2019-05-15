package com.claurendeau.williamjeanmireault.space_invaders;

import java.util.Random;

public class Monster extends Entity {
    private int animation;
    private int type;
    private boolean exploded = false;
    private int deplacement;

    public Monster(int x, int y, int width, int height) {
        super(x, y, width, height);

        animation = 1;
        deplacement = 15;

        assignIcon();
    }

    public void assignIcon() {
        type = new Random().nextInt(3)+1;
        setValue(type*10);
        setIcon("resources/images/monstre"+type+"_1.png");
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }

    public int getDeplacement(){
        return deplacement;
    }

    public void forward(int direction){
        if(direction == 1){
            setX(getX() + deplacement);
        }else{
            setX(getX() - deplacement);
        }
    }

    public void down(){
        setY(getY() + getHeight());
    }

    public void setExploded(boolean exploded){
        this.exploded = exploded;
    }

    public boolean isExploded(){
        return exploded;
    }

    public void resetAnimation(){
        animation = 1;
        setIcon("resources/images/monstre"+type+"_"+animation+".png");
    }

    public void animateMonster(){
        switch(animation) {
        case 1:
            animation = 2;
            break;
        case 2:
            animation = 1;
            break;
        }

        setIcon("resources/images/monstre"+type+"_"+animation+".png");
    }
}
