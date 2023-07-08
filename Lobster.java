import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Lobsters only come out when it's raining during a RainStorm effect.
 * They have a slower reeling speed than Salmon.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class Lobster extends Fish
{
    public Lobster(int direction) {
        super(direction);
        maxSpeed = Math.random() * 1.5 + 0.51;
        speed = maxSpeed;
        scale = 0.4;
        reelingSpeed = 1.5;
        
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
    }
    
    public void act() {
        super.act();
    }
}