package org.manish;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

// Basic tests for score.
public class ElevatorTest {
    private Elevator elevator;

    @Before
    public void before() {
        elevator = new Elevator(0, 20);
    }

    @Test
    public void testScoreStationary() {
        // 20 + 1 - 7
        assertThat(elevator.score(new Request(7, Direction.Up)), is(14));
        assertThat(elevator.score(new Request(7, Direction.Down)), is(14));
    }

    @Test
    public void testScoreSameDirection() {
        elevator.addDestination(new Request(10, Direction.Down));

        // elevator should be at floor 0, but going up.
        assertThat(elevator.score(new Request(7, Direction.Up)), is(14));
        assertThat(elevator.score(new Request(7, Direction.Down)), is(13));
    }

    @Test
    public void testScoreOppositeDirection() {
        elevator.addDestination(new Request(7, Direction.Down));
        nStep(elevator, 3);
        // elevator should be at floor 3, but going up.
        assertThat(elevator.score(new Request(0, Direction.Up)), is(1));
    }

    @Test
    public void testUpdateDestination() {
        elevator.addDestination(new Request(7, Direction.Down));
        assertThat(elevator.getCurrentFloor(), is(0));

        nStep(elevator, 6);
        assertThat(elevator.getCurrentFloor(), is(6));
        assertThat(elevator.getDestinations(), is(Arrays.asList(7)));

        nStep(elevator, 1);
        assertThat(elevator.getDestinations().size(), is(0));
        assertThat(elevator.getDirection(), nullValue());
    }

    @Test
    public void testUpdateDestinationWithMultipleDestinations() {
        elevator.addDestination(new Request(7, Direction.Down));
        nStep(elevator, 1);

        elevator.addDestination(new Request(5, Direction.Up));
        nStep(elevator, 4);

        assertThat(elevator.getDestinations(), is(Arrays.asList(7)));
        assertThat(elevator.getDirection(), is(Direction.Up));
    }

    @Test
    public void testDestinationIsChanged() {
        elevator.addDestination(new Request(5, Direction.Down));
        nStep(elevator, 3);

        elevator.addDestination(new Request(2, Direction.Up));
        nStep(elevator, 1);

        assertThat(elevator.getDestinations(), is(Arrays.asList(5, 2)));
        assertThat(elevator.getDirection(), is(Direction.Up));

        nStep(elevator, 1);
        assertThat(elevator.getDestinations(), is(Arrays.asList(2)));
        assertThat(elevator.getDirection(), is(Direction.Down));

    }

    static void nStep(Elevator e, int n) {
        for (int i = 0; i < n; i++) {
            e.step();
        }
    }
}
