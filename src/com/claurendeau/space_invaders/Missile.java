package com.claurendeau.space_invaders;

public class Missile extends Entity {
    public Missile(int x, int y, int width, int height) {
        super(x, y, width, height);
        setIcon("resources/images/missile.png");
    }
}
