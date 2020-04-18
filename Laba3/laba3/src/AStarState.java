import java.util.*;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    private HashMap<Location, Waypoint> openWayPoints = new HashMap<Location,Waypoint>(); //Добавление нестатичного поля в класс
    private HashMap<Location, Waypoint> closeWayPoints = new HashMap<Location,Waypoint>();
    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }


    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        if(numOpenWaypoints() == 0)
        return null;

        Waypoint best = null;
        float maxValue = Float.MAX_VALUE;
        for (Waypoint value:openWayPoints.values())
        {
            float totalCost = value.getTotalCost();
               if (totalCost < maxValue)
               {
                   best = value;
                   maxValue = totalCost;
               }

        }
        return best;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        Location location = newWP.getLocation();// Находим location новой точки
        if (openWayPoints.containsKey(location))//Есть ли в карте заданный ключ
        {
            Waypoint currentWP = openWayPoints.get(location);
            if (newWP.getPreviousCost() < currentWP.getPreviousCost())
            {
                openWayPoints.put(location, newWP);//Добавляем элемент в карту
                return true;
            }
            return false;
        }
        openWayPoints.put(location, newWP);
        return true;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return openWayPoints.size();//возращает размер карты
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = openWayPoints.remove(loc);
        closeWayPoints.put(loc, waypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closeWayPoints.containsKey(loc);
    }
}
