package org.manish;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.manish.Direction.Up;

public class ElevatorControllerTest {
    private ElevatorController elevatorController;

    @Before
    public void setUp() {
        elevatorController = new ElevatorController(2, 20);
    }

    @Test
    public void correctElevatorIsChosen() {
        int firstScheduled = elevatorController.pickUp(10, Up);
        System.out.println(firstScheduled);

        nStep(5);

        // one of the elevators is at floor 5, going up. this is the opposite direction. its score should be 1.
        // the other one is stationery, so its score should be 20 + 1 - 4.
        assertThat(elevatorController.pickUp(4, Up), not(is(firstScheduled)));

        // we want to go up and we on the way anyway.
        assertThat(elevatorController.pickUp(6, Up), is(firstScheduled));
        assertThat(elevatorController.pickUp(5, Up), is(firstScheduled));
    }

    @Test
    public void testSameDirectionIsPreferred() {
        elevatorController.update(0, 5, 10);
        elevatorController.update(1, 9, 0);

        elevatorController.step();

        // e0 is at floor 6 going up. // e1 is at floor 8 going down.
        assertThat(elevatorController.pickUp(7, Direction.Up), is(0));
        assertThat(elevatorController.pickUp(7, Direction.Down), is(1));
    }

    private void nStep(int n) {
        for (int i = 0; i < n; i++) {
            elevatorController.step();
        }
    }

}
