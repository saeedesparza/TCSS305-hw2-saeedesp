package edu.uw.tcss.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Abstract vehicle class.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public abstract class AbstractVehicle implements Vehicle {
    /**
     * The vehicle's x-axis on the map.
     */
    protected int myVehicleX;
    /**
     * The vehicle's y-axis on the map.
     */
    protected int myVehicleY;
    /**
     * The vehicle's direction.
     */
    protected Direction myVehicleDirection;
    /**
     * A random object for use in vehicle's that need it.
     */
    protected Random myRandom = new Random();
    /**
     * A set object for use in vehicles that need it.
     */
    protected Set<Direction> myDirections = new HashSet<>();
    /**
     * The vehicle's death time.
     */
    protected int myDeathTime;
    /**
     * A counter to keep track of death time.
     */
    protected int myCounter;
    /**
     * The vehicle's initial x-axis state.
     */
    protected int myIniX;
    /**
     * The vehicle's initial y-axis state.
     */
    protected int myIniY;

    /**
     * Constructs a new abstract vehicle using the vehicle's x,
     * y, direction, and death time provided. Note, this method
     * throws an IllegalArgumentException if the vehicle's x or
     * y-axis states passed are less than zero (0).
     *
     * @param theVehicleX The int that stores the vehicle's x.
     * @param theVehicleY The int that stores the vehicle's y.
     * @param theVehicleDirection The Direction that stores the vehicle's Direction.
     * @param theDeathTime The int that stores the vehicle's death time.
     */
    public AbstractVehicle(final int theVehicleX, final int theVehicleY,
                 final Direction theVehicleDirection, final int theDeathTime) {
        super();
        if (theVehicleX < 0 || theVehicleY < 0) {
            throw new IllegalArgumentException();
        }
        myVehicleX = theVehicleX;
        myVehicleY = theVehicleY;
        myVehicleDirection = theVehicleDirection;
        myDeathTime = theDeathTime;
        myIniX = theVehicleX;
        myIniY = theVehicleY;
    }

    /**
     *This method gets the vehicle's direction by returning the Direction
     * myVehicleDirection. This is done to use the direction in other methods
     * within the varying vehicle classes.
     *
     * @return The Direction that stores the vehicle's direction.
     */
    @Override
    public Direction getDirection() {
        return myVehicleDirection;
    }

    /**
     * This method returns the image file name of the vehicle depending
     * on the vehicle's life state. This method ensures that the vehicle
     * is alive before returning the vehicle's alive image file name;
     * if not alive, this method returns the vehicle's dead image file
     * name.
     *
     * @return The StringBuilder that stores the vehicle's image file name.
     */
    @Override
    public String getImageFileName() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName().toLowerCase());
        if (!isAlive()) {
            sb.append("_dead");
        }
        return sb.append(".gif").toString();
    }

    /**
     * This method returns the x-axis state of the vehicle. This is done
     * for debug mode use in the application.
     *
     * @return The int that stores the vehicle's x-axis state.
     */
    @Override
    public int getX() {
        return myVehicleX;
    }

    /**
     * This method return the y-axis state of the vehicle. This is done
     * for debug mode use in the application.
     *
     * @return The int that stores the vehicle's y-axis state.
     */
    @Override
    public int getY() {
        return myVehicleY;
    }

    /**
     * This method sets the x-axis state of the vehicle. This is done
     * using a parameter passed to ensure that the vehicle is being
     * placed on the correct coordinates on the map.
     *
     *
     * @param theX The new x-axis state.
     */
    @Override
    public void setX(final int theX) {
        myVehicleX = theX;
    }

    /**
     * This method sets the y-axis state of the vehicle. This is done
     * using a parameter passed to ensure that the vehicle is being
     * placed on the correct coordinates on the map.
     *
     *
     * @param theY The new y-axis state.
     */
    @Override
    public void setY(final int theY) {
        myVehicleY = theY;
    }

    /**
     * This method sets the direction of the vehicle. This is done
     * using a parameter passed to ensure that the vehicle is being
     * given the correct direction.
     *
     * @param theDir The new direction.
     */
    @Override
    public void setDirection(final Direction theDir) {
        myVehicleDirection = theDir;
    }

    /**
     * This method handles collisions between vehicles and how
     * each vehicle reacts to the collision depending on the
     * collision. This is done by ensuring that the vehicle with
     * a greater death time is killed if collision occurs with
     * another vehicle.
     *
     * @param theOther The other vehicle.
     */
    @Override
    public void collide(final Vehicle theOther) {
        if (theOther.isAlive() && isAlive() && getDeathTime() > theOther.getDeathTime()) {
            myCounter = myDeathTime;
        }
    }

    /**
     * This method returns the death time of the vehicle. This is done
     * for use in other methods such as the collide() method.
     *
     * @return The int that stores the vehicle's death time.
     */
    @Override
    public int getDeathTime() {
        return myDeathTime;
    }

    /**
     * This method returns the life state of the vehicle. If the
     * vehicle is dead, this method will return false. If the
     * vehicle is alive, this method will return true.
     *
     * @return True if the vehicle is alive, false if otherwise.
     */
    @Override
    public boolean isAlive() {
        return myCounter == 0;
    }

    /**
     * This method keeps track of how many cycles have passed
     * to ensure that the vehicle is revived in the appropriate
     * amount of cycles. The cycle amount is dependant on the
     * vehicle's death time.
     */
    @Override
    public void poke() {
        myCounter--;
    }

    /**
     * This method resets all of the vehicle's values for a
     * wipe of the city map. This means that the vehicle's
     * x, y, and life states will be reset to their initial
     * states.
     */
    @Override
    public void reset() {
        myCounter = 0;
        setX(myIniX);
        setY(myIniY);
    }
}