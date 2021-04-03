package classes;

import java.util.Random;

public class Passenger {

    private static final Random RANDOM = new Random();

    private int locationFloor;
    private int destinationFloor;
    private int maxFloorsAvailable;

    public Passenger(int locationFloor, int maxFloorsAvailable) {

        this.locationFloor = locationFloor;
        this.maxFloorsAvailable = maxFloorsAvailable;

        destinationFloor = RANDOM.nextInt(maxFloorsAvailable) + 1;

        while(destinationFloor == locationFloor){
            destinationFloor = RANDOM.nextInt();
        }
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}
