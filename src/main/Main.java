package main;

public class Main {

    public static void main(String[] args) {
        Building building = new Building();

        Thread elevator = new Elevator(building);
        elevator.start();
    }
}
