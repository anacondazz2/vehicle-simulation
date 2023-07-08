import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <h1>The new and vastly improved 2022 Vehicle Simulation Assignment.</h1>
 * <p> This is the first redo of the 8 year old project. Lanes are now drawn dynamically, allowing for
 *     much greater customization. Pedestrians can now move in two directions. The graphics are better
 *     and the interactions smoother.</p>
 * <p> The Pedestrians are not as dumb as before (they don't want straight into Vehicles) and the Vehicles
 *     do a somewhat better job detecting Pedestrians.</p>
 *     
 * Credits:
 * Images -
 * https://www.pngegg.com/en/png-mqkku
 * https://pngtree.com/freepng/wooden-boat-icon-flat-style_5254897.html (author: Ylivdesign)
 * https://www.freepik.com/premium-vector/military-ship-with-guns-navy-force-boat-icon-isolated-white-background_22142104.htm#query=destroyer%20ship&position=31&from_view=keyword
 * https://www.pinterest.ca/pin/717972365570385879/ (author: Fried Shrimp)
 * https://opengameart.org/content/low-poly-rocks-pack (author: Proxy Games);
 * https://www.seekpng.com/ipng/u2w7q8r5i1y3t4t4_lily-lilypad-png/
 * https://fineartamerica.com/featured/desert-sunset-bg-boyd.html?product=greeting-card (author: BG Boyd)
 * https://www.codester.com/items/8027/robber-2d-game-character-sprites (author: Dionie Pasilan)
 * https://www.dreamstime.com/stock-illustration-salmon-fish-isolated-flat-simple-vector-image-image90951691 (author: Dzmitryshashko)
 * https://www.vecteezy.com/vector-art/9016979-lobster-icons-set-vector-flat (author: Ylivdesign)
 * https://www.dreamstime.com/stock-illustration-rain-drops-pattern-vector-seamless-colorful-background-nature-raindrop-abstract-stylish-weather-design-graphic-blue-water-image81503398 (author: Vector Moon)
 * 
 * Sounds - 
 * https://www.youtube.com/watch?v=huhd08q5Xp8 - reeling sound effect
 * https://www.youtube.com/watch?v=zUAMYNS1rYo - pirate ship recruiting burglar sound effect
 * https://www.youtube.com/watch?v=MaCjw6sm8m0 - heavy rain sound effect
 * https://www.youtube.com/watch?v=jkLRith2wcc - ambient water sound effect
 * https://www.youtube.com/watch?v=Gr7MeXmZESc - burglar displacing fisherman sound effect
 * https://www.youtube.com/watch?v=mfrPnKqgDHs - navy and pirate ship fighting sound effect
 * 
 * 
 * Description of Features:
 * This is a modified version of Mr. Cohen's original vehicle simulation. Instead of the traditional cars, buses, and ambulances, the theme has been changed
 * to be in a river setting with fisherman, pirate ships, navy vessels, and burglars travelling in dinghies. 2 types of fish, the salmon and lobster, act as the
 * "Pedestrians" of the simulation. They freely swim underneath all the vehicles and each other, becoming more transparent when doing so. Starting with the vehicles, 
 * Fisherman will randomly stop in a lane to wait for fish. Once a fish enters their radius, they will reel it in and then continue moving. If it's raining, lobsters 
 * will spawn in addition to the salmon. Fisherman will prioritize catching lobsters during this world effect and ignore any salmon. The RainStorm also causes dinghies
 * to speed up in fright of the rain (as they have no cover) while pirate and navy ships will slow down. More fish will also spawn. Moving on, the next vehicle is the 
 * Dinghy, which is the slowest moving vehicle whose interaction is to displace Fisherman upon meeting them, leaving them stranded at sea. If a navy or pirate ship 
 * chances upon a stranded fisherman, they will pick up, increasing their crew size. The PirateShip is a relatively fast vehicle that has between 8-13 crew members.
 * If PirateShips meet a dinghy, they will happily recruit the burglar into their crew. Lastly, the fastest and largest vehicle is the navy ship, containing between
 * 10-16 crew members. Navy ships seek to elmiminate pirates, starting up long fights is they encounter them head on. Whichever ship has more crew members will win
 * the fight. PirateShips want to avoid fighting the navy,and so will not start fights if they encounter the marines first (head on).
 * 
 * 
 * @author Patrick Hu and Jordan Cohen
 * @version October 2022
 */
public class VehicleWorld extends World
{
    private GreenfootImage background;

    // Color Constants
    public static Color GREEN_BORDER = new Color(50,205,50);
    public static Color WATER = new Color(36, 136, 213);

    // Instance variables / Objects
    private boolean twoWayTraffic, splitAtCentre;
    private int laneHeight, laneCount, spaceBetweenLanes;
    private int[] lanePositionsY;
    private VehicleSpawner[] laneSpawners;
    private boolean isRaining;
    private int fishSpawnRate; // the argument for Greenfoot.getRandomNumber() used to spawn fish. The lower this value the higher the chance to spawn a fish
    
    // Ambient water sound
    private GreenfootSound ambientWater;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public VehicleWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1, false); 

        setPaintOrder(Effect.class, CrewLabel.class, Gif.class, Fish.class, Vehicle.class, LaneChecker.class); // classes listed first are painted ontop

        // set up background
        background = new GreenfootImage("./sunset.png");
        setBackground(background);

        // Set critical variables
        laneCount = 5;
        laneHeight = 60;
        spaceBetweenLanes = 10;
        splitAtCentre = true;
        twoWayTraffic = true;
        isRaining = false;  
        fishSpawnRate = 100;

        // Init lane spawner objects 
        laneSpawners = new VehicleSpawner[laneCount];

        // Prepare lanes method - draws the lanes
        lanePositionsY = prepareLanes (this, background, laneSpawners, 95, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCentre);
        
        // Init ambient water sound
        ambientWater = new GreenfootSound("./sounds/ambient-water.wav");
        ambientWater.setVolume(50);
    }

    public void act() {
        ambientWater.playLoop(); // // this should be in act() so that if the program is paused then resumed, "ambientWater" will keep on playing
        spawn();
    }

    public void rain() {
        isRaining = true;
        fishSpawnRate = 50;
    }

    public void stopRaining() {
        isRaining = false;
        fishSpawnRate = 100;
    }

    private void spawn() {
        // Chance to spawn a vehicle
        if (Greenfoot.getRandomNumber(60) == 0){
            int lane = Greenfoot.getRandomNumber(laneCount); // random lane number
            if (!laneSpawners[lane].isTouchingVehicle()){
                int vehicleType = Greenfoot.getRandomNumber(4);
                if (vehicleType == 0 && Greenfoot.getRandomNumber(2) == 0){
                    addObject(new Fisherman(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 1){
                    addObject(new Dinghy(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 2){
                    addObject(new PirateShip(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 3){
                    addObject(new NavyShip(laneSpawners[lane]), 0, 0);
                } 
            }
        }

        // Chance to spawn a Salmon
        if (Greenfoot.getRandomNumber(fishSpawnRate) == 0){
            int xSpawnLocation = Greenfoot.getRandomNumber(getWidth() - 10) + 5;
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;
            if (spawnAtTop){
                addObject(new Salmon(1), xSpawnLocation, lanePositionsY[0] - laneHeight / 2);
            } else {
                addObject(new Salmon(-1), xSpawnLocation, lanePositionsY[laneCount - 1] + laneHeight / 2);
            }
        }

        // Chance to spawn a Lobster
        if (isRaining && Greenfoot.getRandomNumber(fishSpawnRate) == 0) {
            int xSpawnLocation = Greenfoot.getRandomNumber(getWidth() - 100) + 20; // random between 99 and 699, so not near edges
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;
            if (spawnAtTop){
                addObject(new Lobster(1), xSpawnLocation, lanePositionsY[0] - laneHeight / 2);
            } else {
                addObject(new Lobster(-1), xSpawnLocation, lanePositionsY[laneCount - 1] + laneHeight / 2);
            }
        }

        // Chance to spawn an Effect
        if (Greenfoot.getRandomNumber(1200) == 0 && !isRaining) {
            addObject(new RainStorm(700), getWidth() / 2, getHeight() / 2);
        }
    }

    /**
     * <p>The prepareLanes method is a static (standalone) method that takes a list of parameters about the desired roadway and then builds it.</p>
     * 
     * <p><b>Note:</b> So far, Centre-split is the only option, regardless of what values you send for that parameters.</p>
     *
     * <p>This method does three things:</p>
     * <ul>
     *  <li> Determines the Y coordinate for each lane (each lane is centered vertically around the position)</li>
     *  <li> Draws lanes onto the GreenfootImage target that is passed in at the specified / calculated positions. 
     *       (Nothing is returned, it just manipulates the object which affects the original).</li>
     *  <li> Places the VehicleSpawners (passed in via the array parameter spawners) into the World (also passed in via parameters).</li>
     * </ul>
     * 
     * <p> After this method is run, there is a visual road as well as the objects needed to spawn Vehicles. Examine the table below for an
     * in-depth description of what the roadway will look like and what each parameter/component represents.</p>
     * 
     * <pre>
     *                  <=== Start Y
     *  ||||||||||||||  <=== Top Border
     *  /------------\
     *  |            |  
     *  |      Y[0]  |  <=== Lane Position (Y) is the middle of the lane
     *  |            |
     *  \------------/
     *  [##] [##] [##| <== spacing ( where the lane lines or borders are )
     *  /------------\
     *  |            |  
     *  |      Y[1]  |
     *  |            |
     *  \------------/
     *  ||||||||||||||  <== Bottom Border
     * </pre>
     * 
     * @param world     The World that the VehicleSpawners will be added to
     * @param target    The GreenfootImage that the lanes will be drawn on, usually but not necessarily the background of the World.
     * @param spawners  An array of VehicleSpawner to be added to the World
     * @param startY    The top Y position where lanes (drawing) should start
     * @param heightPerLane The height of the desired lanes
     * @param lanes     The total number of lanes desired
     * @param spacing   The distance, in pixels, between each lane
     * @param twoWay    Should traffic flow both ways? Leave false for a one-way street (Not Yet Implemented)
     * @param centreSplit   Should the whole road be split in the middle? Or lots of parallel two-way streets? Must also be two-way street (twoWay == true) or else NO EFFECT
     * 
     */
    public static int[] prepareLanes(World world, GreenfootImage target, VehicleSpawner[] spawners, int startY, int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit){
        // Declare an array to store the y values as I calculate them
        int[] lanePositions = new int[lanes];
        // Pre-calculate half of the lane height, as this will frequently be used for drawing.
        // To help make it clear, the heightOffset is the distance from the centre of the lane (it's y position)
        // to the outer edge of the lane.
        int heightOffset = heightPerLane / 2;

        // draw top border
        target.setColor(GREEN_BORDER);
        target.fillRect (0, startY, target.getWidth(), spacing);

        // Main Loop to Calculate Positions and draw lanes
        for (int i = 0; i < lanes; i++){
            // calculate the position for the lane
            lanePositions[i] = startY + spacing + (i * (heightPerLane+spacing)) + heightOffset; // y pos

            // draw lane
            target.setColor(WATER); 
            // the lane body
            target.fillRect (0, lanePositions[i] - heightOffset, target.getWidth(), heightPerLane);
            // the lane spacing - where the white or yellow lines will get drawn
            target.fillRect(0, lanePositions[i] + heightOffset, target.getWidth(), spacing);

            // Place spawners and draw lines depending on whether its 2 way and centre split
            if (twoWay && centreSplit){
                // first half of the lanes go rightward (no option for left-hand drive, sorry UK students .. ?)
                if (i < lanes / 2){ // first half is on top
                    spawners[i] = new VehicleSpawner(false, heightPerLane);
                    world.addObject(spawners[i], world.getWidth(), lanePositions[i]);
                } else { // second half of the lanes go leftward
                    spawners[i] = new VehicleSpawner(true, heightPerLane);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw rocks if middle 
                if (i == lanes / 2){
                    drawRocks(world, lanePositions, spacing, heightOffset, i);
                } else if (i > 0){ // draw lily pads if not first lane
                    drawLillies(world, lanePositions, spacing, heightOffset, i);
                } 

            } else if (twoWay) { // no centre split
                // alternate between leftward and rightward
                if (i % 2 == 0) {
                    spawners[i] = new VehicleSpawner(false, heightPerLane);
                    world.addObject(spawners[i], world.getWidth(), lanePositions[i]);
                } else {
                    spawners[i] = new VehicleSpawner(true, heightPerLane);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw rocks if between two "streets"
                if (i > 0) { // but not in first position
                    if (i % 2 == 0) {
                        drawRocks(world, lanePositions, spacing, heightOffset, i);
                    } else { // draw lillies (dotted lines)
                        drawLillies(world, lanePositions, spacing, heightOffset, i);
                    } 
                }
            } else { // One way traffic
                spawners[i] = new VehicleSpawner(true, heightPerLane); 
                world.addObject(spawners[i], 0, lanePositions[i]);
                if (i > 0) {
                    for (int j = 0; j < target.getWidth(); j += 120){
                        drawLillies(world, lanePositions, spacing, heightOffset, i);
                    }
                }
            }
        }
        // draws bottom border
        target.setColor (GREEN_BORDER);
        target.fillRect (0, lanePositions[lanes-1] + heightOffset, target.getWidth(), spacing);

        return lanePositions;
    }
    
    /**
     * Draws a lane of rocks acting as the centre split.
     */
    public static void drawRocks(World world, int[] lanePositions, int spacing, int heightOffset, int i) {
        for (int x = spacing + 15; x < world.getWidth() - 5; x += spacing * 3) {
            Rock rock = new Rock((double)spacing / 110); // pass in a scale based on the spacing
            rock.setRotation(Greenfoot.getRandomNumber(360));
            world.addObject(rock, x, lanePositions[i]- heightOffset - spacing / 2);
        }
    }
    
    /**
     * Draws a lane of lily pads acting as dotted white lanes of a road.
     */
    public static void drawLillies(World world, int[] lanePositions, int spacing, int heightOffset, int i) {
        for (int x = spacing + 15; x < world.getWidth() - 5; x += spacing * 6) {
            LilyPad lilypad = new LilyPad((double)spacing / 150); // pass in a scale based on the spacing
            lilypad.setRotation(Greenfoot.getRandomNumber(360));
            world.addObject(lilypad, x, lanePositions[i]- heightOffset - spacing / 2);
        }
    }

    // GETTERS
    /**
     *  Given a lane number (zero-indexed), return the y position in the centre of the lane.
     *  (doesn't factor offset, so watch your offset, i.e. with Bus).
     *  
     *  @param lane the lane number (zero-indexed)
     *  @return int the y position of the lane's center, or -1 if invalid
     */
    public int getLaneY(int lane) {
        if (lane >= 0 && lane < lanePositionsY.length){
            return lanePositionsY[lane];
        } 
        return -1;
    }
    
    /**
     * Given a y-position, return the lane number (zero-indexed).
     * Note that the y-position must be valid, and you should include the offset in your calculations before calling this method.
     * For example, if a Bus is in a lane at y=100, but is offset by -20, it is actually in the lane located at y=80, so you should send 80 to this method, not 100.
     * 
     * @param y - the y position of the lane the Vehicle is in
     * @return int the lane number, zero-indexed
     * 
     */
    public int getLane(int y) {
        for (int i = 0; i < lanePositionsY.length; i++){
            if (y == lanePositionsY[i]){
                return i;
            }
        }
        return -1;
    }
    
    public boolean getHasSplitAtCentre() {
        return splitAtCentre;
    }

    public boolean getIsTwoWayTraffic() {
        return twoWayTraffic;
    }

    public int[] getLanePositionsY() {
        return lanePositionsY;
    }

    public int getLaneCount() {
        return laneCount;
    }

    public int getLaneHeight() {
        return laneHeight;
    }

    public boolean getIsRaining() {
        return isRaining;
    }
    
    public void stopped() {
        // stop all long sound effects that might still be playing in classes that have them
        Fisherman.stopSounds();
        RainStorm.stopSounds();
        NavyShip.stopSounds();
        
        // stop ambient water sound
        ambientWater.stop();
    }
}
