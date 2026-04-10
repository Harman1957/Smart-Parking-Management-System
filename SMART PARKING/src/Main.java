import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ParkingSystem ps = new ParkingSystem();

        ps.initializeSlots(10); // 🔥 IMPORTANT

        while(true) {
            System.out.println("\n1. Show Slots");
            System.out.println("2. Book Slot");
            System.out.println("3. Exit Slot (Generate Bill)");
            System.out.println("4. Exit Program");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:
                    System.out.println(ps.showSlots());
                    break;

                case 2:
                    System.out.print("Enter slot id: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter vehicle number: ");
                    String vehicleNo = sc.nextLine();

                    System.out.print("Enter vehicle type (Car/Bike): ");
                    String type = sc.nextLine();

                    System.out.println(ps.bookSlot(id, vehicleNo, type));
                    break;

                case 3:
                    System.out.print("Enter slot id: ");
                    int cancelId = sc.nextInt();

                    System.out.println(ps.exitSlot(cancelId));
                    break;

                case 4:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
