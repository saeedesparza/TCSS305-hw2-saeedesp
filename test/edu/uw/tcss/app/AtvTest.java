package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.*;

import edu.uw.tcss.model.*;
import edu.uw.tcss.model.Atv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for class Atv.
 *
 * @author Saeed Esparza
 * @version Autumn 2024
 */
public class AtvTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    /** Test method for Atv constructor. */
    @Test
    public void testAtvConstructor() {
        final Atv a = new Atv(1, 11, Direction.NORTH);

        assertEquals(1, a.getX(), "Atv x coordinate not initialized correctly!");
        assertEquals(11, a.getY(), "Atv y coordinate not initialized correctly!");
        assertEquals(Direction.NORTH, a.getDirection(),
                "Atv direction not initialized correctly!");
        assertEquals(25, a.getDeathTime(), "Atv death time not initialized correctly!");
        assertTrue(a.isAlive(), "Atv isAlive() fails initially!");
    }

    /** Test method for Atv setters. */
    @Test
    public void testAtvSetters() {
        final Atv a = new Atv(1, 11, Direction.NORTH);

        a.setX(2);
        assertEquals(2, a.getX(), "Atv setX failed!");
        a.setY(12);
        assertEquals(12, a.getY(), "Atv setY failed!");
        a.setDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, a.getDirection(), "Atv setDirection failed!");
    }

    /**
     * Test method for {@link Atv#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {

        // Atvs can move to ANY and ALL terrains except WALLS
        // so we need to test all of these conditions.

        // Atvs should NOT choose to move to WALL terrain types
        // so we need to test that Atvs never move to wall terrain types.

        // Atvs should NEVER reverse direction
        // so we need to be sure to test that requirement also.

        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.GRASS);
        validTerrain.add(Terrain.CROSSWALK);

        final Atv a = new Atv(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {

            // try the test under each light condition and terrain
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain != Terrain.WALL) {
                    // Atvs can pass ANY and ALL terrain under any ANY light condition
                    assertTrue(a.canPass(destinationTerrain, currentLightCondition),
                            "Atv should be able to pass ANY terrain"
                                    + ", with light " + currentLightCondition);
                } else {
                    assertFalse(a.canPass(destinationTerrain, currentLightCondition),
                            "Atv should not be able to pass WALL"
                                    + ", with light " + currentLightCondition);
                }
            }
        }
    }

    /**
     * Test method for {@link Atv#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirection() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Atv a = new Atv(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = a.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NEVER be chosen
                seenSouth = true;
            }
        }

        assertTrue(seenWest && seenNorth && seenEast,
                "Atv chooseDirection() fails to select randomly "
                        + "among all possible valid choices!");

        assertFalse(seenSouth,
                "Atv chooseDirection() reversed direction when not necessary!");
    }


    /**
     * Test method for {@link Atv#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirectionWallAndReverse() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.WALL);
        neighbors.put(Direction.NORTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.WALL);
        neighbors.put(Direction.SOUTH, Terrain.WALL);

        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;

        final Atv a = new Atv(0, 0, Direction.NORTH);

        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = a.chooseDirection(neighbors);

            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) {
                seenSouth = true;
            }
        }

        assertFalse(seenSouth && seenWest && seenNorth && seenEast,
                "Atv chooseDirection() reversed direction when not necessary!");
    }
}