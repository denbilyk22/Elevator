package classes;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator{

    private static final int MAX_PASSENGERS = 5;

    private final List<Passenger> passengers;

    private int availableFloors;
    private int locationFloor;
    private int destination;

    private boolean moveUp;

    private Building building;

    public Elevator(Building building) {
        this.availableFloors = building.getNumberOfFloors();
        this.building = building;

        passengers = new LinkedList<>();

        locationFloor = 0;

        moveUp = true;
    }

    public void takePassengers(){
        Building.Floor floor = building.getFloors().get(locationFloor);

        while(passengers.size() < MAX_PASSENGERS && !floor.getPassengersAwaitedList().isEmpty()){

            Passenger passengerToElevate;

            if(moveUp){
                passengerToElevate = floor.getPassengersAwaitedList()
                        .stream()
                        .filter(a -> a.getDestinationFloor() > locationFloor)
                        .findAny().get();

            }

            else {
                passengerToElevate = floor.getPassengersAwaitedList()
                        .stream()
                        .filter(a -> a.getDestinationFloor() < locationFloor)
                        .findAny().get();

            }

            passengers.add(passengerToElevate);
        }


    }

    public void move(){
        if(locationFloor == building.getMaximumFloors()){
            moveUp = false;
        }

        if(locationFloor < 1){
            moveUp = true;
        }

        if(moveUp){
            locationFloor++;
        }
        else {
            locationFloor--;
        }
    }



}
