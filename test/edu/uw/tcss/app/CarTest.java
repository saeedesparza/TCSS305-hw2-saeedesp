package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.*;

import edu.uw.tcss.model.*;
import edu.uw.tcss.model.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Car.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public class CarTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    /** Test method for Car constructor. */
    @Test
    public void testCarConstructor() {
        final Car c = new Car(8, 5, Direction.NORTH);

        assertEquals(8, c.getX(), "Car x coordinate not initialized correctly!");
        assertEquals(5, c.getY(), "Car y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, c.getDirection(),
                "Car direction not initialized correctly!");
        assertEquals(15, c.getDeathTime(), "Car death time not initialized correctly!");
        assertTrue(c.isAlive(), "Car isAlive() fails initially!");
    }

    /** Test method for Car setters. */
    @Test
    public void testCarSetters() {
        final Car c = new Car(8, 5, Direction.NORTH);

        c.setX(9);
        assertEquals(9, c.getX(), "Car setX failed!");
        c.setY(6);
        assertEquals(6, c.getY(), "Car setY failed!");
        c.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, c.getDirection(), "Car setDirection failed!");
    }

    /**
     * Test method for {@link Car#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {

        // Cars can move to STREETS, LIGHTS or to CROSSWALKS
        // so we need to test all of these conditions.

        // Cars should NOT choose to move to other terrain types
        // so we need to test that Cars never move to other terrain types.

        // Cars should only reverse direction if no other option is available
        // so we need to be sure to test that requirement also.

        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.GRASS);
        validTerrain.add(Terrain.CROSSWALK);

        final Car c = new Car(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.STREET || destinationTerrain == Terrain.LIGHT
                        && (currentLightCondition == Light.YELLOW
                        || currentLightCondition == Light.GREEN)) {

                    // Cars can pass STREET under GREEN & YELLOW light condition
                    assertTrue(c.canPass(destinationTerrain, currentLightCondition),
                            "Car should be able to pass STREET"
                                    + ", with light " + currentLightCondition);
                } else if (destinationTerrain == Terrain.CROSSWALK) {
                    // Cars can pass CROSSWALK
                    // if the light is YELLOW or GREEN but not RED

                    if (currentLightCondition == Light.RED || currentLightCondition == Light.YELLOW) {
                        assertFalse(c.canPass(destinationTerrain,
                                        currentLightCondition),
                                "Car should NOT be able to pass " + destinationTerrain
                                        + ", with light " + currentLightCondition);
                    } else { // light is green
                        assertTrue(c.canPass(destinationTerrain,
                                        currentLightCondition),
                                "Car should be able to pass " + destinationTerrain
                                        + ", with light " + currentLightCondition);
                    }
                } else if (!validTerrain.contains(destinationTerrain)) {

                    assertFalse(c.canPass(destinationTerrain, currentLightCondition),
                            "Car should NOT be able to pass " + destinationTerrain
                                    + ", with light " + currentLightCondition);
                }
            }
        }
    }

    /**
     * Test method for {@link Car#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseStraightSurroundedByStreet() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Car c = new Car(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = c.chooseDirection(neighbors);

            if (d == Direction.WEST) { // this should NOT be chosen
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) { // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenNorth,
                "Car chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Car chooseDirection() reversed direction when not necessary!");

        assertFalse(seenEast,
                "Car chooseDirection() chose east when not necessary!");

        assertFalse(seenWest,
                "Car chooseDirection() chose west when not necessary!");
    }


    @Test
    public void testChooseEastAroundStreet() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.WALL);
        neighbors.put(Direction.NORTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.LIGHT);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Car c = new Car(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = c.chooseDirection(neighbors);

            if (d == Direction.WEST) { // this should NOT be chosen
                seenWest = true;
            } else if (d == Direction.NORTH) { // this should NOT be chosen
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenEast,
                "Car chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Car chooseDirection() reversed direction when not necessary!");

        assertFalse(seenNorth,
                "Car chooseDirection() chose north when not necessary!");

        assertFalse(seenWest,
                "Car chooseDirection() chose west when not necessary!");
    }


    @Test
    public void testChooseWestAroundStreet() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.CROSSWALK);
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.TRAIL);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Car c = new Car(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = c.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) { // this should NOT be chosen
                seenNorth = true;
            } else if (d == Direction.EAST) { // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenWest,
                "Car chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Car chooseDirection() reversed direction when not necessary!");

        assertFalse(seenNorth,
                "Car chooseDirection() chose north when not necessary!");

        assertFalse(seenEast,
                "Car chooseDirection() chose west when not necessary!");
    }


    @Test
    public void testTurnAroundOnStreet() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.WALL);
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.TRAIL);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Car c = new Car(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = c.chooseDirection(neighbors);

            if (d == Direction.WEST) { // this should NOT be chosen
                seenWest = true;
            } else if (d == Direction.NORTH) { // this should NOT be chosen
                seenNorth = true;
            } else if (d == Direction.EAST) { // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) {
                seenSouth = true;
            }
        }

        assertTrue(seenSouth,
                "Car chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenWest,
                "Car chooseDirection() chose west when not necessary!");

        assertFalse(seenNorth,
                "Car chooseDirection() chose north when not necessary!");

        assertFalse(seenEast,
                "Car chooseDirection() chose east when not necessary!");
    }
}