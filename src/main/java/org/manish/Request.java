package org.manish;

public class Request {
    private int pickupFloor;
    private Direction desiredDirection;

    public Request(int pickupFloor, Direction desiredDirection) {
        this.pickupFloor = pickupFloor;
        this.desiredDirection = desiredDirection;
    }

    public int getPickupFloor() {
        return pickupFloor;
    }

    public Direction getDesiredDirection() {
        return desiredDirection;
    }
}

