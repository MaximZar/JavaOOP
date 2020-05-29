/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location objLocation = (Location) obj;
            if (xCoord == objLocation.xCoord && yCoord == objLocation.yCoord) return true;
        }
        return false;
    }
    public int hashCode() {
        return 37 * (37 * 17 + xCoord) + yCoord;
    }
}