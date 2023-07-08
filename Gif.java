import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for gifs.
 * 
 * @author Patrick Hu 
 * @version October 2022
 */
public class Gif extends Actor
{
    protected GifImage gif;
    protected double scale;
    
    public Gif() {
        
    }
    
    public void act() {
        setImage(gif.getCurrentImage());
    }
}
