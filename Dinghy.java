import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Dinghy vehicle that is piloted by a burglar.
 * Burglars want to steal fishermen's fish, leaving them stranded at sea if they encounter one.
 * Burglars will be recruited by more dominant PirateShips who want them to join their crew.
 * The slowest moving vehicle.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class Dinghy extends Vehicle
{
    protected boolean beingRecruited;
    protected GreenfootSound clash;
    
    public Dinghy(VehicleSpawner origin) {
        super(origin);
        maxSpeed = 0.8 + ((Math.random() * 30) / 60);
        speed = maxSpeed;
        yOffset = 0;
        scale = 0.15;
        clash = new GreenfootSound("./sounds/clash.wav");

        getImage().scale((int)(getImage().getWidth() * scale), (int)(getImage().getHeight() * scale));
        clash.setVolume(70);
    }

    public void act() {
        if (!beingRecruited) {
            drive();    
        }
        checkHitVehicle();
        if (checkEdges()) {
            getWorld().removeObject(this);
        }
    }

    /**
     * Checks for a fisherman in front of it and displaces them.
     */
    public void checkHitVehicle() {
        // burglars on dinghies will displace fisherman (and steal their fish), leaving them stranded at sea
        Fisherman Fisherman = (Fisherman)getOneObjectAtOffset(direction * (int)(speed + getImage().getWidth()/ 2 + 10), 0, Fisherman.class);
        if (Fisherman != null) {
            StrandedFisherman sf = new StrandedFisherman();
            getWorld().addObject(sf, Fisherman.getX(), Fisherman.getY());
            clash.play();

            Fish f = Fisherman.getCaughtFish();
            if (f != null) { // if fisherman is currently catching a fish
                f.setCaughtStatus(false); // set the fish free
                f.setRotation(f.getOriginalRotation()); // return fish to its normal swimming rotation
                f.setSpeed(f.getSpeed() * 2); // make fish swim faster
            }
            getWorld().removeObject(Fisherman); // burglar displaces fisherman and sinks his boat
        }
    }
}
