package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Building {

    private static final Random RANDOM = new Random();

    private int minimumFloors;
    private int maximumFloors;

    private int numberOfFloors;

    private List<Floor> floors;

    public Building(int minimumFloors, int maximumFloors) {
        this.minimumFloors = minimumFloors;
        this.maximumFloors = maximumFloors;

        numberOfFloors = randomNumberBetweenMinAndMax(minimumFloors, maximumFloors);

        floors = Arrays.asList(new Floor[numberOfFloors]);

        floors.forEach(a -> floors.set(floors.indexOf(a), new Floor(floors.indexOf(a))));
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public int getMaximumFloors() {
        return maximumFloors;
    }

    public int getMinimumFloors() {
        return minimumFloors;
    }

    private static int randomNumberBetweenMinAndMax(int minimum, int maximum){
        return RANDOM.nextInt((maximum-minimum) + 1) + minimum;
    }

    public class Floor {

        private static final int MIN_PASSENGERS = 0;
        private static final int MAX_PASSENGERS = 10;

        private int startNumberOfPassengersAwaited;

        private List<Passenger> passengersAwaitedList;

        private int floorNumber;

        public Floor(int floorNumber) {
            this.floorNumber = floorNumber;

            startNumberOfPassengersAwaited = randomNumberBetweenMinAndMax(MIN_PASSENGERS, MAX_PASSENGERS);

            passengersAwaitedList = new ArrayList<>();

            for (int i = 0; i < startNumberOfPassengersAwaited; i++) {
                passengersAwaitedList.add(new Passenger(this.floorNumber, numberOfFloors));
            }

        }

        public List<Passenger> getPassengersAwaitedList() {
            return passengersAwaitedList;
        }

        public int getFloorNumber() {
            return floorNumber;
        }

        public int getStartNumberOfPassengersAwaited() {
            return startNumberOfPassengersAwaited;
        }
    }
}
