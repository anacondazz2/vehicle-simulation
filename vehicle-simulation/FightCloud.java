import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A fighting cloud gif displayed when a NavyShip and PirateShip fight.
 * 
 * @author Patrick Hu 
 * @version October 2022
 */
public class FightCloud extends Gif
{  
    public FightCloud() {
        scale = 0.2;
        gif = new GifImage("./gifs/fight-cloud.gif");
        
        for (GreenfootImage img : gif.getImages()) {
            img.scale((int)(img.getWidth() * scale), (int)(img.getHeight() * scale));
        }
    }

    public void act() {
        super.act();
    }
}
