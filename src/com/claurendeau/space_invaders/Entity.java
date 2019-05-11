package com.claurendeau.space_invaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity  {
    protected int x;
    protected int y;
    protected boolean alive;
    protected BufferedImage icon;

    public Entity(int x, int y){
        this.x = x;
        this.y = y;
        alive = true;
    }

    public Entity(int x, int y, int speed){
        this.x = x;
        this.y = y;
    }

    public void setIcon(String path){
        try {
            icon = ImageIO.read(new File(path));
        }
        catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    public BufferedImage getIcon(){
        return icon;
    }

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
