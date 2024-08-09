import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A Fisherman that will periodically stop at sea to wait for fish.
 * After catching a fish fisherman will continue moving forward, but he might choose to wait for more fish again.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class Fisherman extends Vehicle
{
    private boolean isFishing;
    private boolean isReeling;
    private Fish caughtFish;
    private GreenfootImage fishingImg;
    private GreenfootImage notFishingImg;
    protected int fishingRate; // the random number passed into Greenfoot.getRandomNumber() used to determine whether the fisherman should fish
    protected boolean isPrimeTime;
    protected static GreenfootSound reel = new GreenfootSound("./sounds/reel.wav");

    public Fisherman(VehicleSpawner origin) {
        super(origin);
        maxSpeed = 0.9 + ((Math.random() * 30) / 50);
        speed = maxSpeed;
        yOffset = 0;
        actCount = 0;
        scale = 0.17;
        isFishing = false;
        isReeling = false;
        fishingRate = 250;
        
        fishingImg = new GreenfootImage("./images/fishing-boat-fishing.png");
        notFishingImg = new GreenfootImage("./images/fishing-boat-not-fishing.png");
        fishingImg.scale((int)(fishingImg.getWidth() * scale), (int)(fishingImg.getHeight() * scale));
        notFishingImg.scale((int)(notFishingImg.getWidth() * scale), (int)(notFishingImg.getHeight() * scale));
        if (direction == -1) {
            fishingImg.mirrorHorizontally();
            notFishingImg.mirrorHorizontally();
        }
        setImage(notFishingImg);
        reel.setVolume(45);
    }

    public void act() {
        actCount++;
        if (!isChangingLanes) {
            chanceToFish();   
        }
        if (!isFishing) {
            setImage(notFishingImg);
            drive();
        }
        if (isFishing && !isReeling) {
            waitForFish();
        }
        else if (isReeling) {
            reel(caughtFish);
        }
        if (checkEdges()) {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Rolls random numbers for a chance to start fishing.
     */
    public void chanceToFish() {
        if (isPrimeTime) {
            fishingRate = 100;
        }
        else {
            fishingRate = 250;
        }
        
        if (!isFishing && !isTouching(VehicleSpawner.class) && getX() > 40 && getX() < getWorld().getWidth() - 40) {
            if (Greenfoot.getRandomNumber(fishingRate) == 0 && !isFishing) {
                isFishing = true;
                setImage(fishingImg);
                speed = 0;
            }
        }
    }
    
    /**
     * Constantly checks for any fish that enter its radius. 
     * After a catching a fish fisherman will stop fishing and continue moving forward.
     */
    public void waitForFish() {
        VehicleWorld vw = (VehicleWorld)getWorld();
        ArrayList<Fish> fish = (ArrayList)getObjectsInRange(150, Fish.class);
        for (Fish f : fish) {
            if (vw.getIsRaining() && !(f instanceof Lobster)) {
                continue; // if it's raining prioritize catching lobsters and ignore salmon
            }
            else if (!f.getIsCaught()) {
                caughtFish = f;
                caughtFish.setCaughtStatus(true);
                isReeling = true;
                break; // fisherman can only catch one fish at a time
            }
        }
    }
    
    /**
     * Makes fish slowly travel towards it.
     */
    public void reel(Fish fish) {
        reel.playLoop();
        fish.turnTowards(this);
        if (actCount % 6 == 0) {
            fish.move(fish.getReelingSpeed());    
        }

        if (intersects(caughtFish)) { // fully reeled
            isReeling = false;
            isFishing = false;
            reel.stop();
            if (fish.getWorld() != null && getWorld() != null) {
                caughtFish = null;
                getWorld().removeObject(fish);
            }
        }        
    }
    
    public void checkHitVehicle() {
        // fishing boat collision interaction is handled by Dinghy's checkHitVehicle() method
    }
    
    public Fish getCaughtFish() {
        return caughtFish;
    }
    
    public void takeAdvantage() {
        isPrimeTime = true;
    }
    
    public void resumeNormalFishingRate() {
        isPrimeTime = false;
    }
    
    public static void stopSounds() {
        if (reel.isPlaying()) {
            reel.stop();   
        }
    }
}
