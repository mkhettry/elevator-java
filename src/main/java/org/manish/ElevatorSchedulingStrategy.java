package org.manish;

/** An algorithm to select the best elevator for a given pick up request */
public interface ElevatorSchedulingStrategy {
    Elevator bestElevator(Iterable<Elevator> elevators, Request request);
}

class NearestCarStrategy implements ElevatorSchedulingStrategy {

    public NearestCarStrategy() {
    }

    @Override
    public Elevator bestElevator(Iterable<Elevator> elevators, Request request) {
        Elevator bestElevator = null;
        int bestScore = -1;

        for (Elevator elevator : elevators) {
            int currentScore = elevator.score(request);
            if (currentScore > bestScore) {
                bestElevator = elevator;
                bestScore = currentScore;
            }
        }

        return bestElevator;
    }
}
