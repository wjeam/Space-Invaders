package com.claurendeau.space_invaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity  {
    // On aurait pu utiliser protected
    private int x;
    private int y;
    private boolean alive;
    private BufferedImage icon;
    private int width;
    private int height;
    private int value;

    public Entity(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        alive = true;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setIcon(String path){
        try {
            icon = ImageIO.read(new File(path));
        }
        catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
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
