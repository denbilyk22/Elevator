import classes.Building;
import classes.Elevator;

public class Main {

    public static void main(String[] args) {

        int minimumFloors = 5;
        int maximumFloors = 6;

        Building building = new Building(minimumFloors, maximumFloors);

        Thread elevator = new Elevator(building);
        elevator.start();

        try {
            elevator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish");

    }



}
