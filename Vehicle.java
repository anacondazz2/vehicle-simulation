import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the superclass for Vehicles. Contains methods for driving and lane changing.
 * 
 * @author Patrick Hu and Jordan Cohen
 * @version October 2022
 */
public abstract class Vehicle extends SuperSmoothMover
{
    protected int actCount;
    protected double scale; // amount to scale image by
    protected VehicleSpawner origin;
    protected double maxSpeed;
    protected double speed;
    protected int direction; // 1 = right, -1 = left
    protected int yOffset;
    protected boolean moving;
    protected boolean slowed, spedUp;
    protected double dx, dy; // dx is vehicle's horizonal displacement (speed * direction), dy is vehicle's vertical displacement when changing lanes
    protected double finalY; // final y position after a lane change
    protected boolean isChangingLanes;

    public abstract void checkHitVehicle();

    public Vehicle(VehicleSpawner origin) {
        this.origin = origin;
        actCount = 0;
        moving = true;
        slowed = false;

        if (origin.facesRightward()) {
            direction = 1;
        } else {
            direction = -1;
            getImage().mirrorHorizontally();
        }        
    }

    /**
     * Sets the correct location for a vehicle using the coordinates given in its VehicleSpawner.
     */
    public void addedToWorld(World world) {
        setLocation(origin.getX() - (direction * 10), origin.getY() - yOffset);
    }

    /**
     * Method that deals with movement. Speed can be set by individual subclasses in their constructors.
     * If a vehicle is behind a slower one it will attempt to change lanes to an available one.
     */
    public void drive() {
        // ahead is a generic vehicle - we don't know what type BUT
        // since every Vehicle "promises" to have a getSpeed() method,
        // we can call that on any vehicle to find out it's speed
        Vehicle ahead = (Vehicle)getOneObjectAtOffset(direction * (int)(speed + getImage().getWidth()/2 + 10), 0, Vehicle.class);
        if (ahead != null) {
            speed = ahead.getSpeed();
            if (!isChangingLanes) {
                isChangingLanes = attemptLaneChange();
            }
        }
        else {
            if (slowed) {
                speed = maxSpeed / 1.3;
                if (speed <= 0.5) { // if speed <= 0.5 object will not move
                    speed = 0.51;
                }
            }
            else if (spedUp) {
                speed = maxSpeed * 2;
            }
            else {
                speed = maxSpeed;
            }
        }
        if (isChangingLanes) {
            checkIfLaneChangeComplete();    
        }
    
        dx = speed * direction;
        if (getX() <= 0 || getX() >= getWorld().getWidth() - 1) {
            move(dx); // setLocation() fails to set positions outside the world boundaries
        }
        else {
            setLocation(getX() + dx, getY() + dy);   
        }
    }

    /**
     * Checks whether a lane change is possible using LaneCheckers.
     * If possible, sets the value for a dy which will cause the vehicle to change lanes by moving vertically.
     * 
     * @return boolean Whether a lane change is possible.
     */
    public boolean attemptLaneChange() {
        VehicleWorld vw = (VehicleWorld)getWorld();
        boolean splitAtCentre = vw.getHasSplitAtCentre();
        boolean twoWayTraffic = vw.getIsTwoWayTraffic();
        int laneCount = vw.getLaneCount();
        int lane = vw.getLane(getY());
        LaneChecker lcAbove = null, lcBelow = null;        

        if (twoWayTraffic && !splitAtCentre) {
            return false; // cannot change lanes
        }
        else if (twoWayTraffic && splitAtCentre) {
            if (lane == 0 || lane == laneCount / 2) { // topmost lane or lane just below centre split
                lcBelow = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), false);
                vw.addObject(lcBelow, lcBelow.getXPos(), lcBelow.getYPos());
            }
            else if (lane == laneCount - 1 || lane == laneCount / 2 - 1) { // bottom-most lane or lane just above centre split
                lcAbove = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), true);
                vw.addObject(lcAbove, lcAbove.getXPos(), lcAbove.getYPos());
            }
            else { // a lane in the middle of the road
                lcAbove = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), true);
                lcBelow = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), false);
                vw.addObject(lcAbove, lcAbove.getXPos(), lcAbove.getYPos());
                vw.addObject(lcBelow, lcBelow.getXPos(), lcBelow.getYPos());
            }   
        }
        else { // one way traffic
            if (lane == 0) {
                lcBelow = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), false);
                vw.addObject(lcBelow, lcBelow.getXPos(), lcBelow.getYPos());
            }
            else if (lane == laneCount - 1) {
                lcAbove = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), true);
                vw.addObject(lcAbove, lcAbove.getXPos(), lcAbove.getYPos());
            }
            else { 
                lcAbove = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), true);
                lcBelow = new LaneChecker(vw, this, lane, this.getImage().getWidth(), this.getImage().getHeight(), false);
                vw.addObject(lcAbove, lcAbove.getXPos(), lcAbove.getYPos());
                vw.addObject(lcBelow, lcBelow.getXPos(), lcBelow.getYPos());
            }
        }
        // change lanes
        boolean successfulLaneChange = false;
        if (lcAbove != null && !lcAbove.isTouchingVehicle()) {
            dy = -0.6;
            finalY = lcAbove.getYPos();
            successfulLaneChange = true;
        }
        else if (lcBelow != null && !lcBelow.isTouchingVehicle()) {
            dy = 0.6;   
            finalY = lcBelow.getYPos();
            successfulLaneChange = true;
        }

        // remove lane checkers
        if (lcAbove != null) vw.removeObject(lcAbove);
        if (lcBelow != null) vw.removeObject(lcBelow);

        return successfulLaneChange;
    }

    /**
     * Checks if vehicle has finished changing lanes by comparing its y-pos to its resultant lane's y-pos.
     */
    public void checkIfLaneChangeComplete() {
        if (getY() == finalY) {
            dy = 0;
            isChangingLanes = false;
        }
    }

    public void slowDown() {
        slowed = true;
    }
    
    public void speedUp() {
        spedUp = true;
    }

    public void returnToNormalSpeed() {
        slowed = false;
        spedUp = false;
    }

    /**
     * A method used by all Vehicles to check if they are at the edge.
     */
    protected boolean checkEdges() {
        if (direction == 1) {
            if (getX() >= getWorld().getWidth() + getImage().getWidth() / 2) {
                return true;
            }
        } else {
            if (getX() <= 0 - getImage().getWidth() / 2) {
                return true;
            }
        }

        return false;
    }

    // GETTERS
    /**
     * An accessor that can be used to get this Vehicle's speed. Used, for example, when a vehicle wants to see
     * if a faster vehicle is ahead in the lane.
     */
    public double getSpeed() {
        return speed;
    }
}
