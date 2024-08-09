import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A laughing skull gif displayed when a PirateShip recruits a burglar from a Dinghy.
 * 
 * @author Patrick Hu
 * @version October 2022    
 */
public class LaughingSkull extends Gif
{
    public LaughingSkull() {
        scale = 0.2;
        gif = new GifImage("./gifs/laughing-skull.gif");
        
        for (GreenfootImage img : gif.getImages()) {
            img.scale((int)(img.getWidth() * scale), (int)(img.getHeight() * scale));
        }
    }
    
    public void act() {
        super.act();
    }
}
