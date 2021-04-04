package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class Elevator extends Thread{

    private static final int MAX_PASSENGERS = 5;

    private int availableFloors;

    private int locationFloor;
    private int destination;

    private int finalDestinationFloor;

    private List<Passenger> elevatorPassengers;

    private boolean moveUp;

    private boolean isFinish;

    private Building building;

    public Elevator(Building building) {
        this.building = building;
        this.availableFloors = building.getNumberOfFloors();

        elevatorPassengers = new ArrayList<>();

        locationFloor = 0;

        moveUp = true;
    }

    public void run(){
        while (!isFinish()){
            printPassengers();
            deliverPassengers();
            takePassengers();
            move();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void takePassengers() {
        Building.Floor floor = building.getFloors().get(locationFloor);
        List<Passenger> passengersAwaitedList = floor.getPassengersAwaitedList();

        if(elevatorPassengers.isEmpty() && !passengersAwaitedList.isEmpty()){
            int countPassengersUp = (int) passengersAwaitedList.stream()
                    .filter(passenger -> passenger.getDestinationFloor() > locationFloor)
                    .count();

            int countPassengersDown = (int) passengersAwaitedList.stream()
                    .filter(passenger -> passenger.getDestinationFloor() < locationFloor)
                    .count();

            moveUp = countPassengersUp >= countPassengersDown;
        }

        while (elevatorPassengers.size() < MAX_PASSENGERS
                && !passengersAwaitedList.isEmpty()
                && hasPassengerToElevate(passengersAwaitedList)) {


            Passenger passengerToElevate = getPassengerToElevate(passengersAwaitedList);

            elevatorPassengers.add(passengerToElevate);
            passengersAwaitedList.remove(passengerToElevate);

        }

        setDestinationAndFinalDestination();

    }

    private void deliverPassengers(){
        elevatorPassengers.removeIf(pas -> pas.getDestinationFloor() == locationFloor);
    }

    private void move() {
        locationFloor = destination;
    }

    private boolean hasPassengerToElevate(List<Passenger> passengersAwaitedList) {
        if (moveUp) {
            return passengersAwaitedList.stream().anyMatch(pas -> pas.getDestinationFloor() > locationFloor);
        } else {
            return passengersAwaitedList.stream().anyMatch(pas -> pas.getDestinationFloor() < locationFloor);
        }

    }

    private Passenger getPassengerToElevate(List<Passenger> passengersAwaitedList) {
        if (moveUp) {
            return passengersAwaitedList.stream()
                    .filter(pas -> pas.getDestinationFloor() > locationFloor)
                    .findAny().get();
        } else {
            return passengersAwaitedList.stream()
                    .filter(pas -> pas.getDestinationFloor() < locationFloor)
                    .findAny().get();
        }

    }

    private void setDestinationAndFinalDestination(){
        if(elevatorPassengers.size() == MAX_PASSENGERS){

            int maximumDestination = elevatorPassengers.stream()
                                    .mapToInt(Passenger::getDestinationFloor)
                                    .max().getAsInt();

            int minimumDestination = elevatorPassengers.stream()
                                    .mapToInt(Passenger::getDestinationFloor)
                                    .min().getAsInt();

            if (moveUp) {
                destination = minimumDestination;

                finalDestinationFloor = maximumDestination;
            } else {
                destination = maximumDestination;

                finalDestinationFloor = minimumDestination;
            }
        }

        else if(elevatorPassengers.size() < 5 && !elevatorPassengers.isEmpty()){

            if (moveUp) {

                destination = elevatorPassengers.stream()
                        .mapToInt(Passenger::getDestinationFloor)
                        .min().getAsInt();

                int nextNearestStop = building.getFloors().stream()
                        .filter(floor -> floor.getFloorNumber() > locationFloor
                                && !floor.getPassengersAwaitedList().isEmpty()
                                && hasPassengerToElevate(floor.getPassengersAwaitedList()))
                        .mapToInt(Building.Floor::getFloorNumber)
                        .min().orElse(-1);

                if(nextNearestStop != -1 && nextNearestStop < destination){
                    destination = nextNearestStop;
                }

                finalDestinationFloor = elevatorPassengers.stream().
                        mapToInt(Passenger::getDestinationFloor)
                        .max().getAsInt();;
            }

            else {
                destination = elevatorPassengers.stream()
                        .mapToInt(Passenger::getDestinationFloor)
                        .max().getAsInt();

                int nextNearestStop = building.getFloors().stream()
                        .filter(floor -> floor.getFloorNumber() < locationFloor
                                        && !floor.getPassengersAwaitedList().isEmpty()
                                        && hasPassengerToElevate(floor.getPassengersAwaitedList()))
                        .mapToInt(Building.Floor::getFloorNumber)
                        .max().orElse(-1);

                if(nextNearestStop != -1 && nextNearestStop > destination){
                    destination = nextNearestStop;
                }

                finalDestinationFloor = elevatorPassengers.stream()
                        .mapToInt(Passenger::getDestinationFloor)
                        .min().getAsInt();
            }
        }

        else if(elevatorPassengers.isEmpty() && building.getFloors().get(locationFloor).getPassengersAwaitedList().isEmpty()){

            if(building.getFloors().stream()
                    .filter(floor -> floor.getFloorNumber() < locationFloor)
                    .anyMatch(floor -> !floor.getPassengersAwaitedList().isEmpty())){

                moveUp = false;

                destination = building.getFloors().stream()
                        .filter(floor -> floor.getFloorNumber() < locationFloor && !floor.getPassengersAwaitedList().isEmpty())
                        .mapToInt(Building.Floor::getFloorNumber)
                        .max().getAsInt();

            }

            else if(building.getFloors().stream()
                    .filter(floor -> floor.getFloorNumber() > locationFloor)
                    .anyMatch(floor -> !floor.getPassengersAwaitedList().isEmpty())){

                moveUp = true;

                destination = building.getFloors().stream()
                        .filter(floor -> floor.getFloorNumber() > locationFloor && !floor.getPassengersAwaitedList().isEmpty())
                        .mapToInt(Building.Floor::getFloorNumber)
                        .min().getAsInt();

            }

            finalDestinationFloor = destination;

        }

    }

    private boolean isFinish(){
        if(building.getFloors().stream().allMatch(floor -> floor.getPassengersAwaitedList().isEmpty()) && elevatorPassengers.isEmpty()){
            isFinish = true;
        }
        return isFinish;
    }

    private void printPassengers(){
        for (int i = building.getFloors().size() - 1; i >= 0; i--) {
            String floor = (building.getFloors().get(i).getFloorNumber()) + 1 + " етаж : " ;

            System.out.print(String.format("%10s", floor));

            if(i == locationFloor){

                StringBuilder elevator = new StringBuilder();

                if(moveUp){
                  elevator.append("^^");
                }
                else {
                    elevator.append("↓↓");
                }

                elevator.append("ліфт (");
                elevatorPassengers.forEach(a-> elevator.append(a.getDestinationFloor() + 1 + " "));
                elevator.deleteCharAt(elevator.lastIndexOf(" "));
                elevator.append(")");
                System.out.print(String.format("%22s", elevator.toString()));
            }

            else {
                System.out.print(String.format("%22s", ""));
            }


            System.out.print(" | ");
            building.getFloors().get(i).getPassengersAwaitedList()
                    .forEach(a -> System.out.print(a.getDestinationFloor() + 1 + " "));

            System.out.println();
        }
        System.out.println();
    }
}
