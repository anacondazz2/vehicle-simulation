import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An image of a rock.
 * 
 * @author Patrick Hu 
 * @version October 2022
 */
public class Rock extends Actor
{
    private double scale = 0.15;
    
    public Rock(double scale) {
        this.scale = scale;
        setImage("./rock.png");
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
    }
    
    public void act() {

    }
}
