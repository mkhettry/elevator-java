package org.manish;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private final int numStories;

    private List<Elevator> elevators = new ArrayList<Elevator>();

    public ElevatorController(int numElevators, int numFloors) {
        this.numStories = numFloors;
        elevators = new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, numFloors));
        }
    }

    public void step() {
        for (Elevator e : elevators) {
            e.step();
        }
    }

    public void update() {
        // ??
    }

    /**
     * Schedule a pick up
     * @param floor where the person will be picked up.
     * @param direction expected direction of the person.
     * @return if of the elevator
     */
    public int pickUp(int floor, Direction direction) {
        Elevator bestElevator = null;
        int bestScore = -1;

        Request request = new Request(floor, direction);
        for (Elevator elevator : elevators) {
            int currentScore = elevator.score(request);
            if (currentScore > bestScore) {
                bestElevator = elevator;
                bestScore = currentScore;
            }
        }

        bestElevator.addDestination(request);
        return bestElevator.getId();
    }

    // Test only method.
    void update(int id, int floor, int newGoal) {
        elevators.get(id).update(floor, newGoal);
    }

}