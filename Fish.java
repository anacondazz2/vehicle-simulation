import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Fish that swims across the river.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public abstract class Fish extends SuperSmoothMover
{
    protected double speed;
    protected double maxSpeed;
    protected double reelingSpeed;
    protected int direction;
    protected boolean isCaught;
    protected double scale;
    protected int originalRotation; // fish's rotation will change when it's caught

    public Fish(int direction) {
        this.direction = direction;
        originalRotation = direction == 1 ? 90 : -90;
        
        setRotation(originalRotation);
    }

    public void act() {
        if (!isCaught) {
            swim();          
        }   
        checkSwimmingUnderObjects();
        checkEdges();
    }

    public void swim() {
        setLocation(getX(), getY() + (speed*direction));
    }
    
    /**
     * Checks if fish is swimming underneath vehicles.
     * Turns more transparent when doing so.
     */
    public void checkSwimmingUnderObjects() {
        if (isTouching(Vehicle.class)) {
            getImage().setTransparency(70);
        }
        else {
            getImage().setTransparency(120);
        }
    }
    
    /**
     * Checks if fish has fully swam to the other side of the river.
     */
    public void checkEdges() {
        VehicleWorld world = (VehicleWorld)getWorld();
        int[] lanePositionsY = world.getLanePositionsY();
        int laneCount = world.getLaneCount();
        int laneHeight = world.getLaneHeight();

        if (direction == -1 && getY() < lanePositionsY[0] - laneHeight / 2) {
            getWorld().removeObject(this);
        } else if (direction == 1 && getY() > lanePositionsY[laneCount - 1] + laneHeight / 2) {
            getWorld().removeObject(this);
        }
    }
    
    // GETTERS
    public boolean getIsCaught() {
        return isCaught;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public double getReelingSpeed() {
        return reelingSpeed;
    }
    
    public int getOriginalRotation() {
        return originalRotation;
    }
    
    // SETTERS
    public void setCaughtStatus(boolean status) {
        isCaught = status;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
