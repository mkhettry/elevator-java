package org.manish;

import java.util.Random;

public class ElevatorSimulation {
    private final ElevatorController controller;
    private final Random random;

    public static void main(String[] args) {
        ElevatorSimulation elevatorSimulation = new ElevatorSimulation(10, 2);
        elevatorSimulation.simulate(10);

    }

    public ElevatorSimulation(int numFloors, int numElevators) {
        this.controller = new ElevatorController(numFloors, numElevators);
        this.random = new Random();
    }


    public void simulate(int numTicks) {
        for (int i = 0; i < numTicks; i++) {

            System.out.print(String.format("%03d", i) + ": ");
            if (random.nextInt(5) == 0) {

                Request r = new Request(random.nextInt(controller.getNumFloors()),
                        random.nextBoolean() ? Direction.Down : Direction.Up);



                // probably do something better. like passengers arrive with a poisson distribution or some such.
                // right now 20% of the time, request an elevator at a random floor with a random distribution.
                int id = controller.pickUp(r.getPickupFloor(), r.getDesiredDirection());
                System.out.print("<" + r.getPickupFloor() + "," + r.getDesiredDirection() + "->" + id + ">");
            }
            controller.step();

            System.out.println(controller);
        }
    }
}
