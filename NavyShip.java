import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The fastest vehicle.
 * Saves stranded fishermen, allowing them to join their crew.
 * Fights pirate ships if encountered head on.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class NavyShip extends LargeVehicle
{
    protected PirateShip currentOpponent;
    protected FightCloud fightCloud;
    private int actMark;
    protected static GreenfootSound shipFight = new GreenfootSound("./sounds/ship-fight.wav");

    public NavyShip(VehicleSpawner origin) {
        super(origin);
        maxSpeed = 1.3 + ((Math.random() * 40) / 40);
        speed = maxSpeed;
        yOffset = 0;
        scale = 0.22;
        crewSize = getRandomNumberInRange(10, 16);
        crewLabel = new CrewLabel(crewSize);
        crewLabelOffsetY = -65;
        if (direction == 1) {
            crewLabelOffsetX = 10;    
        }
        else {
            crewLabelOffsetX = 15;
        }
        fightCloud = new FightCloud();
        
        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
        shipFight.setVolume(50);
    }

    public void act() {
        actCount++;
        if (actCount == 1) { // cannot use addedToWorld() for this because it will override Vehicle's addedToWorld()
            getWorld().addObject(crewLabel, 0, 0); 
        }
        if (!isFighting) {
            drive(); 
        }
        else {
            shipFight.play();
        }
        if (!isChangingLanes) {
            checkHitVehicle();   
        }
        if (getWorld() == null) { // if navy ship lost to the pirate ship
            return;
        }
        moveCrewLabel();
        updateCrewLabel();
        checkHitStrandedFisherman();
        if (checkEdges()) {
            die();
        }
    }

    /**
     * Checks for PirateShip in front of it. Starts a fight for a relatively long time with the pirate ship.
     * The ship with more crew members wins.
     */
    public void checkHitVehicle() {
        // if encounters a pirate ship head on fight it for 2 seconds
        // the ship with more crew members win
        // pirate ships that encounter navy ships head on choose not to pick a fight
        PirateShip pirateShip = (PirateShip)getOneObjectAtOffset(direction * (int)(speed + getImage().getWidth() / 2 + 10), 0, PirateShip.class);    
        
        if (pirateShip != null && !pirateShip.isChangingLanes && !isFighting) {
            currentOpponent = pirateShip;
            isFighting = true;
            speed = 0;
            currentOpponent.isFighting = true;
            currentOpponent.speed = 0;
            actMark = actCount + 400; 

            int midpoint = getX() + direction * (getImage().getWidth() / 2 + 5);
            getWorld().addObject(fightCloud, midpoint, getY());
        }

        if (currentOpponent != null && actCount == actMark) { // fight is over
            getWorld().removeObject(fightCloud);
            if (crewSize >= currentOpponent.crewSize) {
                isFighting = false;
                currentOpponent.die();
            }
            else {
                currentOpponent.isFighting = false;
                die();
            }

            currentOpponent = null;
            shipFight.stop();
        }
    }
    
    public static void stopSounds() {
        if (shipFight.isPlaying()) {
            shipFight.stop();    
        }
    }
}
