package org.manish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElevatorController {
    private final int numFloors;

    private List<Elevator> elevators = new ArrayList<Elevator>();

    // TODO: find a better way to inject the strategy.
    private ElevatorSchedulingStrategy scheduler = new NearestCarStrategy();

    public ElevatorController(int numFloors, int numElevators) {
        this.numFloors = numFloors;
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
        // I really wasn't sure why you need update in the controller. The controller should pick the
        // best elevator and let each elevator updates its state based on where its going.
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Elevator> iterator = elevators.iterator(); iterator.hasNext(); ) {
            Elevator e = iterator.next();
            sb.append(e.toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Schedule a pick up
     * @param floor where the person will be picked up.
     * @param direction expected direction of the person.
     * @return if of the elevator
     */
    public int pickUp(int floor, Direction direction) {
        Request request = new Request(floor, direction);

        Elevator bestElevator = scheduler.bestElevator(elevators, request);
        bestElevator.addDestination(request);
        return bestElevator.getId();
    }

    // Test only method.
    void update(int id, int floor, int newGoal) {
        elevators.get(id).update(floor, newGoal);
    }

    public int getNumFloors() {
        return numFloors;
    }
}