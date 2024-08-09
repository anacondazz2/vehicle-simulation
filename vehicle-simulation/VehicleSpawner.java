import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Spawner object is a place where a Vehicle can spawn. Each spawner is
 * able to check if there is already a Vehicle in the spot to avoid spawning
 * multiple Vehicles on top of each other. 
 * 
 * @author Jordan Cohen
 * @version October 2022
 */
public class VehicleSpawner extends Actor
{
    public static final Color TRANSPARENT_RED = new Color (255, 0, 0, 128);
    
    public static final int DIST_BETWEEN_CARS = 150;
    
    private GreenfootImage image;
    
    private boolean rightward;
    private boolean visible;
    private int height, width;
    
    public VehicleSpawner(boolean rightward, int laneHeight)
    {
        this.rightward = rightward;
        this.height = (int)(laneHeight * 0.75);
        width = DIST_BETWEEN_CARS; // width of spawner
        // set this to true to see the Spawners - might help with understanding of how this works:
        visible = false;
        image = new GreenfootImage(width, height);
        
        if (visible) {
            image.setColor(TRANSPARENT_RED);
            image.fillRect(0, 0, width - 1, height - 1);
        }
        setImage(image);
    }
    
    public boolean facesRightward() {
        return rightward;
    }
    
    public boolean isTouchingVehicle() {
        return this.isTouching(Vehicle.class);
    }
}