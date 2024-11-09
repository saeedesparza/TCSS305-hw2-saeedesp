package edu.uw.tcss.model;

import java.util.Map;

/**
 * A truck class.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public class Truck extends AbstractVehicle {
    /**
     * Constructs a new Truck vehicle using the vehicle's x,
     * y, and direction provided. Note, this method calls upon
     * super() and passes the x, y, direction, and death time
     * provided to the abstract vehicle class for Truck vehicle
     * construction.
     *
     * @param theVehicleX The int that stores the vehicle's x.
     * @param theVehicleY The int that stores the vehicle's y.
     * @param theVehicleDirection The Direction that stores the vehicle's Direction.
     */
    public Truck(final int theVehicleX, final int theVehicleY,
               final Direction theVehicleDirection) {
        super(theVehicleX, theVehicleY, theVehicleDirection, 0);
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
        return theTerrain == Terrain.CROSSWALK
                && (theLight == Light.YELLOW || theLight == Light.GREEN)
                || theTerrain == Terrain.STREET || theTerrain == Terrain.LIGHT;
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
        myDirections.clear();
        int isAssigned = 0;
        Direction vehicleDirection = myVehicleDirection;
        for (final Direction direction : Direction.values()) {
            if ((theNeighbors.get(direction) == Terrain.STREET
                    || theNeighbors.get(direction) == Terrain.CROSSWALK
                    || theNeighbors.get(direction) == Terrain.LIGHT)
                    && direction != getDirection().reverse()) {
                isAssigned = 1;
                myDirections.add(direction);
            }
        }
        if (isAssigned == 1) {
            final int index = myRandom.nextInt(myDirections.size());
            int i = 0;
            for (final Direction direction : myDirections) {
                if (i == index) {
                    vehicleDirection = direction;
                }
                i++;
            }
        } else {
            vehicleDirection = getDirection().reverse();
        }
        return vehicleDirection;
    }
}