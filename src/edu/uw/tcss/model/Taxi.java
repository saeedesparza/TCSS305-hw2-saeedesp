package edu.uw.tcss.model;

import java.util.Map;

/**
 * A taxi class.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public class Taxi extends AbstractVehicle {
    /**
     * The vehicle's death time.
     */
    private static final int THE_DEATH_TIME = 15;
    /**
     * The vehicle's max waiting period.
     */
    private static final int THE_MAX = 3;
    /**
     * A counter for measuring the vehicle's waiting period.
     */
    private int myClockCycle;

    /**
     * Constructs a new Taxi vehicle using the vehicle's x,
     * y, and direction provided. Note, this method calls upon
     * super() and passes the x, y, direction, and death time
     * provided to the abstract vehicle class for Truck vehicle
     * construction.
     *
     * @param theVehicleX The int that stores the vehicle's x.
     * @param theVehicleY The int that stores the vehicle's y.
     * @param theVehicleDirection The Direction that stores the vehicle's Direction.
     */
    public Taxi(final int theVehicleX, final int theVehicleY,
                 final Direction theVehicleDirection) {
        super(theVehicleX, theVehicleY, theVehicleDirection, THE_DEATH_TIME);
    }

    /**
     * This method returns a boolean representation of the
     * vehicle's terrain access. This ensures that the
     * vehicle will not traverse terrain that it is not
     * allowed to traverse.
     *
     * @param theTerrain The terrain to be checked.
     * @param theLight The light color.
     * @return True if the terrain is traversable, false if otherwise.
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean pass = false;
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            myClockCycle++;
        }
        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED
                && myClockCycle == THE_MAX) {
            pass = true;
            myClockCycle = 0;
        }
        if ((theTerrain == Terrain.LIGHT || theTerrain == Terrain.CROSSWALK)
                && theLight == Light.GREEN || theTerrain == Terrain.STREET) {
            pass = true;
        }
        return pass;
    }

    /**
     * This method returns the direction of the vehicle and handles
     * whether or not the vehicle is allowed to traverse on the
     * terrain held by the direction. This method ensures that the
     * terrain at the given direction is valid by checking if each
     * direction given is traversable given the current vehicle.
     *
     * @param theNeighbors The map of neighboring terrain.
     * @return The direction that holds valid terrain.
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction vehicleDirection = myVehicleDirection;
        for (final Direction direction : Direction.values()) {
            if (isValid(theNeighbors.get(getDirection()))) {
                break;
            } else if (isValid(theNeighbors.get(getDirection().left()))) {
                vehicleDirection = getDirection().left();
                break;
            } else if (isValid(theNeighbors.get(getDirection().right()))) {
                vehicleDirection = getDirection().right();
                break;
            } else {
                vehicleDirection = getDirection().reverse();
            }
        }
        return vehicleDirection;
    }

    /**
     * This method ensures that the given terrain from chooseDirection()
     * is traversable terrain given the vehicle's terrain traversal
     * rules and regulations.
     *
     * @param theTerrain The terrain passed.
     * @return True if the terrain is traversable, false if otherwise.
     */
    private boolean isValid(final Terrain theTerrain) {
        return theTerrain == Terrain.STREET || theTerrain == Terrain.CROSSWALK
                || theTerrain == Terrain.LIGHT;
    }
}