package org.manish;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.manish.Direction.Up;

public class NearestCarStrategyTest {

    ArrayList<Elevator> elevators = new ArrayList<Elevator>();
    ElevatorSchedulingStrategy classUnderTest = new NearestCarStrategy();

    @Before
    public void setUp() {
        elevators.add(new Elevator(0, 20));
        elevators.add(new Elevator(1, 20));
    }

    private Elevator pickUp(int floor, Direction direction) {
        Request request = new Request(floor, direction);
        Elevator e = classUnderTest.bestElevator(elevators, request);
        e.addDestination(request);
        return e;
    }

    @Test
    public void correctElevatorIsChosen() {
        Elevator firstScheduled = pickUp(10, Up);
        System.out.println(firstScheduled.getId());

        nStep(5);

        // one of the elevators is at floor 5, going up. this is the opposite direction. its score should be 1.
        // the other one is stationery, so its score should be 20 + 1 - 4.
        assertThat(pickUp(4, Up), not(is(firstScheduled)));

        // we want to go up and we on the way anyway.
        assertThat(pickUp(6, Up), is(firstScheduled));
        assertThat(pickUp(5, Up), is(firstScheduled));
    }

    @Test
    public void testSameDirectionIsPreferred() {
        elevators.get(0).update(5, 10);
        elevators.get(1).update(9, 0);

        for (Elevator e : elevators) {
            e.step();
        }

        // e0 is at floor 6 going up. // e1 is at floor 8 going down.
        assertThat(pickUp(7, Direction.Up).getId(), is(0));
        assertThat(pickUp(7, Direction.Down).getId(), is(1));
    }

    private void nStep(int n) {
        for (int i = 0; i < n; i++) {
            for (Elevator e : elevators) {
                e.step();
            }
        }
    }

}
