import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A rainstorm effect that randomly occurs during the simulation.
 * An actor that is just a black image with opacity lowered to create a dark overlay.
 * Continuously spawns Raindrops.
 * Slows down all LargeVehicles while making Dinghys(burglars) move faster in order to dodge the rain.
 * Fisherman will allows lobsters to spawn during its effect.
 * 
 * @author Patrick Hu
 * @version October 2022
 */
public class RainStorm extends Effect
{
    private int duration;
    protected static GreenfootSound rain = new GreenfootSound("./sounds/rain.wav"); // longer sounds are static so VehicleWorld can access them and stop them when the program is paused

    public RainStorm(int duration) {
        this.duration = duration;
    }

    public void addedToWorld(World world) {
        VehicleWorld vw = (VehicleWorld)world;

        // set image as a dark overlay
        setImage("./black.jpg");
        getImage().scale(getWorld().getWidth(), getWorld().getHeight());
        getImage().setTransparency(100);
        
        vw.rain();
        rain.setVolume(76);
    }

    public void act() {
        VehicleWorld vw = (VehicleWorld)getWorld();
        rain.playLoop();
        
        // slow down LargeVehicles, speed up Dinghies, and increase fishingRate for Fisherman
        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>)vw.getObjects(Vehicle.class);
        for (Vehicle v : vehicles) {
            if (v instanceof LargeVehicle) {
                v.slowDown();
            }
            else if (v instanceof Dinghy) {
                v.speedUp();
            }
            else if (v instanceof Fisherman) {
                Fisherman f = (Fisherman)v;
                f.takeAdvantage();
            }
        }
        
        // spawn raindrops
        int randX = Greenfoot.getRandomNumber(getWorld().getWidth() + getWorld().getWidth() / 2);
        if (Greenfoot.getRandomNumber(10) == 0) {
            RainDrop raindrop = new RainDrop();
            getWorld().addObject(raindrop, randX, 10);
        }
        
        duration--;
        if (duration == 0) {
            for (Vehicle v : vehicles) {
                v.returnToNormalSpeed();
                if (v instanceof Fisherman) {
                    Fisherman f = (Fisherman)v;
                    f.resumeNormalFishingRate();
                }
            }
            vw.stopRaining();
            rain.stop();
            getWorld().removeObject(this);
        }
    }
    
    public static void stopSounds() {
        if (rain.isPlaying()) {
            rain.stop();
        }
    }
}
