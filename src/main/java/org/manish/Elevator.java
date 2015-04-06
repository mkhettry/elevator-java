package org.manish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Elevator {
    private final int numFloors;
    private final int id;

    // mutable state.
    private int currentFloor;
    private List<Integer> goals = new ArrayList<Integer>();
    private Direction direction;

    Elevator(int id, int numFloors) {
        this.id = id;
        this.currentFloor = 0;
        this.numFloors = numFloors;

    }

    /**
     * Take one step. For a elevator in motion, this could mean either going up or down one floor. Picking up
     * a request as well as changing direction.
     */
    public void step() {
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

    /**
     * Add a new pickup request.
     * @param request
     */
    void addDestination(Request request) {
        goals.add(request.getPickupFloor());
        updateDirection();
    }

    /**
     * Implement a score for the nearest car strategy.
     * TODO: I think this belongs in the strategy.
     * @param request
     * @return a numeric score. The higher the score, the more desirable it is to use this elevator.
     * @see <a href="http://www.quora.com/Is-there-any-public-elevator-scheduling-algorithm-standard">Public elevator scheduling notes</a>
     */
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

    /*
     * Private method but needs some documentation!
     * If the elevator has reached one of its goals, remove the goal.
     * Changes directions if its needed; i.e. all its goals in the current direction have been fulfilled.
     * If there are no goals, just wait on the current floor; i.e set direction to null.
     * Implementation notes: Keep an unordered list of goals. Assumption is that there are very few goals
     * at any given time and the code complexity of sorting is not worth it.
     */
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

    @Override
    public String toString() {
        StringBuilder gs = new StringBuilder();

        // Apache string utils has functions to do string.join
        gs.append("[");
        for (Iterator<Integer> iterator = goals.iterator(); iterator.hasNext(); ) {
            Integer goal = iterator.next();
            gs.append(goal);
            if (iterator.hasNext()) {
                gs.append(",");
            }
        }
        gs.append("]");

        return "[" + id + ":" + currentFloor + "," + gs + "]";
    }
}
