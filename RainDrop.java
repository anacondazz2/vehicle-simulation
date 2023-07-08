import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A singular rain drop.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class RainDrop extends SuperSmoothMover
{
    private int actCount;
    private double scale;
    private double speed;
    private boolean isFading;
    
    public RainDrop() {
        actCount = 0;
        scale = 0.7;
        speed = 2.5;
        isFading = false;
        setRotation(125);
        
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
    }
    
    public void act() {
        actCount++;
        move(2); // make rain drop fall
        if (!isFading && Greenfoot.getRandomNumber(70) == 0) { // let it fade randomly
            isFading = true;
        }
        if (isFading) {
            fade(); 
        }
    }
    
    /**
     * Slowly fades out the raindrop and removes it from the world
     */
    public void fade() {
        int currentTransparency = getImage().getTransparency();
        
        if (currentTransparency == 0) {
            getWorld().removeObject(this);
            return;
        }
        else if (actCount % 5 == 0) {
            getImage().setTransparency(currentTransparency - 15);
        }
    }
}
