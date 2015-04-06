package org.manish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// Request. PickupRequest
//          DropOffRequest.
public class Elevator {
    private final int numFloors;
    private int id;
    private int currentFloor;
    private List<Integer> goals = new ArrayList<Integer>();
    private Direction direction;

    Elevator(int id, int numFloors) {
        this.id = id;
        this.currentFloor = 0;
        this.numFloors = numFloors;

    }

    void step() {
        if (direction == Direction.Up && !topFloor()) {
            currentFloor++;
        } else if (direction == Direction.Down && !groundFloor()) {
            currentFloor--;
        }

        for (Iterator<Integer> it = goals.iterator(); it.hasNext(); ) {
            Integer goal = it.next();
            if (goal == currentFloor) {
                it.remove();
                break;
            }
        }

        updateDirection();
    }

    void addDestination(Request request) {
        goals.add(request.getPickupFloor());
        updateDirection();
    }

    // http://www.quora.com/Is-there-any-public-elevator-scheduling-algorithm-standard
    int score(Request request) {
        if (direction == null
                || sameDirection(request.getPickupFloor()) && direction == request.getDesiredDirection()) {
            return (numFloors + 1) - diff(request);
        } else if (sameDirection(request.getPickupFloor()) && direction != request.getDesiredDirection()) {
            return numFloors - diff(request);
        } else {
            return 1;
        }
    }

    // Meant to be used only in tests.
    void update(int floor, int newGoal) {
        this.currentFloor = floor;
        this.goals.add(newGoal);
    }

    private int diff(Request request) {
        return Math.abs(currentFloor - request.getPickupFloor());
    }

    // Is the elevator moving in the direction of this floor.
    private boolean sameDirection(int floor) {
        // if floor == currentFloor, sameDirection returns TRUE for either Up/Down.
        return (direction == Direction.Up && ((floor - currentFloor) >= 0))
                || (direction == Direction.Down) && ((floor - currentFloor) <= 0);
    }

    private boolean topFloor() {
        return currentFloor == numFloors - 1;
    }

    private boolean groundFloor() {
        return currentFloor == 0;
    }

    private void updateDirection() {
        if (goals.isEmpty()) {
            direction = null;
            return;
        }

        for (Integer goal : goals) {
            if (sameDirection(goal)) {
                return;
            }
        }

        if ((goals.get(0) - currentFloor) > 0) {
            direction = Direction.Up;
        } else if ((goals.get(0) - currentFloor) < 0) {
            direction = Direction.Down;
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public List<Integer> getDestinations() {
        return Collections.unmodifiableList(goals);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }
}
