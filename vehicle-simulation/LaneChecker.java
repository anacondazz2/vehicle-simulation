import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An invisble lane checker with the exact same dimensions as its corresponding vehicle but a little wider to ensure 
 * lane change is safe. Checks if vehicles are touching it to make sure a lane change is possible.
 * 
 * @author Patrick Hu 
 * @version October 2022
 */
public class LaneChecker extends SuperSmoothMover
{
    private GreenfootImage image;
    private VehicleWorld vw;
    private Vehicle vehicle;
    private boolean aboveVehicle; // whether lane checker is above or below the vehicle
    private int laneOfVehicle;
    private int x, y; // x and y position of the lane checker

    public LaneChecker(VehicleWorld vw, Vehicle vehicle, int laneOfVehicle, int width, int height, boolean aboveVehicle) {
        this.vw = vw;
        this.vehicle = vehicle;
        this.laneOfVehicle = laneOfVehicle;
        this.aboveVehicle = aboveVehicle;
        image = new GreenfootImage(width + 125, height);
        x = vehicle.getX();
        y = calculateYPos();

        setImage(image);
    }

    /**
     * Calculates the y-position of a lane checker. If the lane checker is supposed to be above the vehicle, set its y-position to be 
     * the y-pos of the lane above it, else the y-pos of the lane below it.
     */
    public int calculateYPos() {        
        if (aboveVehicle) {
            return vw.getLaneY(laneOfVehicle - 1);
        }
        else {
            return vw.getLaneY(laneOfVehicle + 1);
        }
    }

    public boolean isTouchingVehicle() {
        return this.isTouching(Vehicle.class);
    }

    // GETTERS
    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }
}
