import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An image of a lily pad.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class LilyPad extends Actor
{
    private double scale;
    
    public LilyPad(double scale) {
        this.scale = scale;
        setImage("./lilypad.png");
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
    }
    
    public void act() {
        
    }
}
