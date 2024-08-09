import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Salmon are the most basic fish type. They exist both when it's raining and when it's not.
 * Relatively fast reeling speed.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class Salmon extends Fish
{
    public Salmon(int direction) {
        super(direction);
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        reelingSpeed = 3.5;
        scale = 0.1;
        
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
    }

    public void act() {
        super.act();
    }
}
