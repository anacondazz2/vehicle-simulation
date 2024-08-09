import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A PirateShip that moves at a relatively fast speed.
 * Picks up stranded fisherman and burglars on dinghies to join their crew.
 * Is hunted by navy ships who generally have more crew members than it.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class PirateShip extends LargeVehicle
{
    protected Dinghy caughtDinghy;
    protected boolean isRecruiting;
    private int actMark;
    protected LaughingSkull laughingSkull;
    protected GreenfootSound shipAhoy; // sound effect when pirate ship encounters dinghy (burgalar) and recruits them
    
    public PirateShip(VehicleSpawner origin) {
        super(origin);
        maxSpeed = 1.1 + ((Math.random() * 40) / 50);
        speed = maxSpeed;
        yOffset = 0;
        scale = 0.13;
        crewSize = getRandomNumberInRange(8, 13);
        crewLabel = new CrewLabel(crewSize);
        crewLabelOffsetY = -65;
        if (direction == 1) {
            crewLabelOffsetX = 5;    
        }
        else {
            crewLabelOffsetX = 15;
        }
        laughingSkull = new LaughingSkull();
        shipAhoy = new GreenfootSound("./sounds/ship-ahoy.wav");

        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
        shipAhoy.setVolume(80);
    }
    
    public void act() {
        actCount++;
        if (actCount == 1) { // cannot use addedToWorld() for this because it will override Vehicle's addedToWorld()
            getWorld().addObject(crewLabel, 0, 0);   
        } 
        if (!isFighting && !isRecruiting) {
            drive();   
        }
        if (!isChangingLanes) {
            checkHitVehicle();   
        }
        moveCrewLabel();
        updateCrewLabel();
        checkHitStrandedFisherman();
        if (checkEdges()) {
            die();
        }
    }

    /**
     * Checks if a Dinghy is in front of it. Recruits the burglar and increases its crew size.
     */
    public void checkHitVehicle() {
        // if pirate ship encounters a dinghy, they recruit the burglar into their crew
        Dinghy dinghy = (Dinghy)getOneObjectAtOffset(direction * (int)(speed + getImage().getWidth() / 2 + 10), 0, Dinghy.class);
        if (dinghy != null && !dinghy.isChangingLanes && !isRecruiting) {
            shipAhoy.play();
            caughtDinghy = dinghy;
            caughtDinghy.beingRecruited = true;
            dinghy.speed = 0;
            isRecruiting = true;
            speed = 0;
            actMark = actCount + 300;
            
            int midpoint = getX() + direction * (getImage().getWidth() / 2 + 5);
            getWorld().addObject(laughingSkull, midpoint, getY());
        }
        
        if (caughtDinghy != null && actCount == actMark) { // recruiting finished
            getWorld().removeObject(laughingSkull);
            getWorld().removeObject(caughtDinghy);
            isRecruiting = false;
            crewSize++;
            caughtDinghy = null;
        }
    }
}
