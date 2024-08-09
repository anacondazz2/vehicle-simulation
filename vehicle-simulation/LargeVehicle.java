import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Abstract superclass for large vehicles such as PirateShips and NavyShips.
 * Contains functionality common to each such as a crew label.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public abstract class LargeVehicle extends Vehicle
{
    protected int actCount = 0;
    protected CrewLabel crewLabel;
    protected int crewSize;
    protected int crewLabelOffsetY, crewLabelOffsetX;
    protected boolean isFighting;

    public LargeVehicle(VehicleSpawner origin) {
        super(origin);
        isFighting = false;
    }

    public void moveCrewLabel() {
        crewLabel.setLocation(getX() + crewLabelOffsetX, getY() + crewLabelOffsetY);    
    }

    public void updateCrewLabel() {
        crewLabel.update(crewSize);
    }

    public void removeCrewLabel() {
        getWorld().removeObject(crewLabel);
    }

    public void checkHitStrandedFisherman() {
        StrandedFisherman sf = (StrandedFisherman)getOneObjectAtOffset(direction * (int)(speed + getImage().getWidth()/2 + 10), 0, StrandedFisherman.class);
        if (sf != null) {
            crewSize++;
            getWorld().removeObject(sf);
        }
    }

    /**
     * Removes the ship from the world along with its crew label.
     */
    public void die() {
        removeCrewLabel();
        getWorld().removeObject(this);    
    }

    public int getRandomNumberInRange(int start, int end) {
        int a = Greenfoot.getRandomNumber(end-start+1);
        return start + a;
    }
}
