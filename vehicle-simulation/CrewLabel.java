import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An image that displays the size of a LargeVehicle's crew along with a crew symbol. 
 * 
 * @author Patrick Hu 
 * @version October 2022
 */
public class CrewLabel extends SuperSmoothMover
{
    private int fontSize = 25;
    private double scale = 0.07;
    private int width = 60, height = 60;
    public int value_x = 0, value_y = height / 2 + 4;
    public int crewSymbol_x = 17, crewSymbol_y = height / 2 + 10;
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private Color transparent = new Color(0,0,0,0);

    public CrewLabel(int crewSize) {
        update(crewSize); // update() will essentially construct a new crew label
    }

    public void update(int newCrewSize) {
        // draw canvas with current number of crewmates
        GreenfootImage canvas = new GreenfootImage(width, height);

        // add image of number of crewmates
        String s = Integer.toString(newCrewSize);
        if (s.length() == 2) { // crew size is in double digits
            crewSymbol_x = 27; // offset crew symbol more to the right
        }
        GreenfootImage value = new GreenfootImage(s, fontSize, fillColor, transparent, lineColor);
        canvas.drawImage(value, value_x, value_y);  

        // add image of crewmate symbol on left
        GreenfootImage crewSymbol = new GreenfootImage("./images/crew-symbol.png");
        crewSymbol.scale((int)(crewSymbol.getWidth() * scale), (int)(crewSymbol.getHeight() * scale));
        canvas.drawImage(crewSymbol, crewSymbol_x, crewSymbol_y);
              
        setImage(canvas);
    }
}