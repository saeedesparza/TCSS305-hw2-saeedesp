package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.model.Direction;
import edu.uw.tcss.model.Light;
import edu.uw.tcss.model.Terrain;
import edu.uw.tcss.model.Truck;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Truck.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public class TruckTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    /** Test method for Truck constructor. */
    @Test
    public void testTruckConstructor() {
        final Truck t = new Truck(19, 9, Direction.NORTH);

        assertEquals(19, t.getX(), "Truck x coordinate not initialized correctly!");
        assertEquals(9, t.getY(), "Truck y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, t.getDirection(),
                "Truck direction not initialized correctly!");
        assertEquals(0, t.getDeathTime(), "Truck death time not initialized correctly!");
        assertTrue(t.isAlive(), "Truck isAlive() fails initially!");
    }

    /** Test method for Truck setters. */
    @Test
    public void testTruckSetters() {
        final Truck t = new Truck(19, 9, Direction.NORTH);

        t.setX(20);
        assertEquals(20, t.getX(), "Truck setX failed!");
        t.setY(10);
        assertEquals(10, t.getY(), "Truck setY failed!");
        t.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, t.getDirection(), "Truck setDirection failed!");
    }

    /**
     * Test method for {@link Truck#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {

        // Trucks can move to STREETS, CROSSWALKS, and GRASS
        // so we need to test all of these conditions.

        // Trucks should NOT choose to move to other terrain types
        // so we need to test that Trucks never move to other terrain types.

        // Trucks should only reverse direction if no other option is available
        // so we need to be sure to test that requirement also.

        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.STREET);
        validTerrain.add(Terrain.LIGHT);
        validTerrain.add(Terrain.CROSSWALK);

        final Truck t = new Truck(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.STREET) {

                    // Trucks can pass GRASS under any light condition
                    assertTrue(t.canPass(destinationTerrain, currentLightCondition),
                            "Truck should be able to pass STREET"
                                    + ", with light " + currentLightCondition);
                } else if (destinationTerrain == Terrain.CROSSWALK) {
                    // Trucks can pass CROSSWALK
                    // if the light is YELLOW or GREEN but not RED

                    if (currentLightCondition == Light.GREEN) {
                        assertTrue(t.canPass(destinationTerrain,
                                        currentLightCondition),
                                "Truck should be able to pass " + destinationTerrain
                                        + ", with light " + currentLightCondition);
                    } else if (currentLightCondition == Light.YELLOW) { // light is yellow
                        assertTrue(t.canPass(destinationTerrain,
                                        currentLightCondition),
                                "Truck should be able to pass " + destinationTerrain
                                        + ", with light " + currentLightCondition);
                    } else {
                        assertFalse(t.canPass(destinationTerrain,
                                        currentLightCondition),
                                "Truck should NOT be able to pass " + destinationTerrain
                                        + ", with light " + currentLightCondition);
                    }
                } else if (!validTerrain.contains(destinationTerrain)) {

                    assertFalse(t.canPass(destinationTerrain, currentLightCondition),
                            "Truck should NOT be able to pass " + destinationTerrain
                                    + ", with light " + currentLightCondition);
                } else if (destinationTerrain == Terrain.LIGHT
                            && currentLightCondition == Light.RED) {
                    assertTrue(t.canPass(destinationTerrain, currentLightCondition),
                            "Truck should be able to pass LIGHT"
                                    + ", with light " + currentLightCondition);
                }
            }
        }
    }


    /**
     * Test method for {@link Truck#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPassFalse() {

        // Trucks can move to STREETS, CROSSWALKS, and GRASS
        // so we need to test all of these conditions.

        // Trucks should NOT choose to move to other terrain types
        // so we need to test that Trucks never move to other terrain types.

        // Trucks should only reverse direction if no other option is available
        // so we need to be sure to test that requirement also.

        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.GRASS);
        validTerrain.add(Terrain.TRAIL);
        validTerrain.add(Terrain.WALL);
        validTerrain.add(Terrain.LIGHT);

        final Truck t = new Truck(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.GRASS
                        || destinationTerrain == Terrain.TRAIL
                        || destinationTerrain == Terrain.WALL) {

                    assertFalse(t.canPass(destinationTerrain, currentLightCondition),
                            "Truck should be able to pass STREET"
                                    + ", with light " + currentLightCondition);
                }
            }
        }
    }

    /**
     * Test method for {@link Truck#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirectionSurroundedByStreets() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.STREET);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Truck t = new Truck(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = t.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenWest && seenNorth && seenEast,
                "Truck chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Truck chooseDirection() reversed direction when not necessary!");
    }


    @Test
    public void testChooseDirectionSurroundedByCrosswalks() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.CROSSWALK);
        neighbors.put(Direction.NORTH, Terrain.CROSSWALK);
        neighbors.put(Direction.EAST, Terrain.CROSSWALK);
        neighbors.put(Direction.SOUTH, Terrain.CROSSWALK);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Truck t = new Truck(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = t.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenWest && seenNorth && seenEast,
                "Truck chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Truck chooseDirection() reversed direction when not necessary!");
    }


    @Test
    public void testChooseDirectionSurroundedByLights() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.LIGHT);
        neighbors.put(Direction.NORTH, Terrain.LIGHT);
        neighbors.put(Direction.EAST, Terrain.LIGHT);
        neighbors.put(Direction.SOUTH, Terrain.LIGHT);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Truck t = new Truck(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = t.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenWest && seenNorth && seenEast,
                "Truck chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Truck chooseDirection() reversed direction when not necessary!");
    }


    /**
     * Test method for {@link Truck#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirectionOnStreetMustReverse() {

        for (final Terrain t : Terrain.values()) {
            if (t != Terrain.STREET && t != Terrain.CROSSWALK && t != Terrain.LIGHT) {

                final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
                neighbors.put(Direction.WEST, t);
                neighbors.put(Direction.NORTH, t);
                neighbors.put(Direction.EAST, t);
                neighbors.put(Direction.SOUTH, Terrain.STREET);

                final Truck tr = new Truck(0, 0, Direction.NORTH);

                // the Truck must reverse and go SOUTH
                assertEquals(Direction.SOUTH, tr.chooseDirection(neighbors),
                        "Truck chooseDirection() failed "
                                + "when reverse was the only valid choice!");
            }
        }
    }


    @Test
    public void testChooseDirectionOnCrosswalkMustReverse() {

        for (final Terrain t : Terrain.values()) {
            if (t != Terrain.STREET && t != Terrain.CROSSWALK && t != Terrain.LIGHT) {

                final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
                neighbors.put(Direction.WEST, t);
                neighbors.put(Direction.NORTH, t);
                neighbors.put(Direction.EAST, t);
                neighbors.put(Direction.SOUTH, Terrain.CROSSWALK);

                final Truck tr = new Truck(0, 0, Direction.NORTH);

                // the Truck must reverse and go SOUTH
                assertEquals(Direction.SOUTH, tr.chooseDirection(neighbors),
                        "Truck chooseDirection() failed "
                                + "when reverse was the only valid choice!");
            }
        }
    }


    @Test
    public void testChooseDirectionOnLightMustReverse() {

        for (final Terrain t : Terrain.values()) {
            if (t != Terrain.STREET && t != Terrain.CROSSWALK && t != Terrain.LIGHT) {

                final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
                neighbors.put(Direction.WEST, t);
                neighbors.put(Direction.NORTH, t);
                neighbors.put(Direction.EAST, t);
                neighbors.put(Direction.SOUTH, Terrain.CROSSWALK);

                final Truck tr = new Truck(0, 0, Direction.NORTH);

                // the Truck must reverse and go SOUTH
                assertEquals(Direction.SOUTH, tr.chooseDirection(neighbors),
                        "Truck chooseDirection() failed "
                                + "when reverse was the only valid choice!");
            }
        }
    }
}