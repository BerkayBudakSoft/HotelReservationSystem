import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String RESERVATION_FILE = "reservations.txt";
    private static List<Room> rooms;
    private static List<Reservation> reservations;

    public static void main(String[] args) {
        rooms = initializeRooms();
        reservations = loadReservations();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    makeReservation(scanner);
                    break;
                case 2:
                    cancelReservation(scanner);
                    break;
                case 3:
                    saveReservations();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // load reservations from a file
    private static List<Reservation> loadReservations() {
        return new ArrayList<>();
    }

    // save reservations to a file
    private static void saveReservations() {
        //  implement reservation saving to a file
    }

    // initialize available room types and their prices
    private static List<Room> initializeRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Suite", 100.0));
        rooms.add(new Room("Classic", 75.0));
        rooms.add(new Room("Family", 90.0));
        // add more room types as needed
        return rooms;
    }

    // function to create a reservation
    private static void makeReservation(Scanner scanner) {
        System.out.println("Room types:");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + ". " + room.getType() + " - $" + room.getPrice());
        }

        System.out.print("Select room type: ");
        int roomIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (roomIndex < 1 || roomIndex > rooms.size()) {
            System.out.println("Invalid room type, reservation canceled.");
            return;
        }

        Room selectedRoom = rooms.get(roomIndex - 1);

        System.out.print("Check-in date (year/month/day): ");
        String checkInDate = scanner.nextLine();

        System.out.print("Check-out date (year/month/day): ");
        String checkOutDate = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("ID number: ");
        String idNumber = scanner.nextLine();

        System.out.print("Phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter email address: ");
        String email = scanner.nextLine();

        // calculate the total price based on selected room and reservation duration.
        double totalPrice = calculateTotalPrice(selectedRoom.getPrice(), checkInDate, checkOutDate);

        Reservation reservation = new Reservation(selectedRoom, checkInDate, checkOutDate, name, idNumber, phoneNumber, email, totalPrice);
        reservations.add(reservation);

        System.out.println("Reservation created.");
        sendEmailConfirmation(reservation);
    }

    // function to cancel a reservation
    private static void cancelReservation(Scanner scanner) {
        System.out.print("ID number: ");
        String idNumber = scanner.nextLine();

        Reservation reservation = findReservationById(idNumber);
        if (reservation != null) {
            reservations.remove(reservation);
            System.out.println("Reservation canceled successfully.");
        } else {
            System.out.println("No reservation found with this ID number.");
        }
    }

    // function to find a reservation by ID number
    private static Reservation findReservationById(String idNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.getIdNumber().equals(idNumber)) {
                return reservation;
            }
        }
        return null;
    }

    // function to send an email confirmation for a reservation
    private static void sendEmailConfirmation(Reservation reservation) {
        String emailContent = "Reservation details:\n" +
                "Room type: " + reservation.getRoom().getType() + "\n" +
                "Check-in date: " + reservation.getCheckInDate() + "\n" +
                "Check-out date: " + reservation.getCheckOutDate() + "\n" +
                "Name: " + reservation.getName() + "\n" +
                "ID number: " + reservation.getIdNumber() + "\n" +
                "Phone number: " + reservation.getPhoneNumber() + "\n" +
                "Email: " + reservation.getEmail() + "\n" +
                "Total Price: $" + reservation.getTotalPrice(); // Added total price

        System.out.println("An email confirmation has been sent with reservation details:\n" + emailContent);
    }

    // function to calculate the total price based on room price and reservation duration
    private static double calculateTotalPrice(double roomPrice, String checkInDate, String checkOutDate) {
        // parse check-in and check-out dates
        String[] checkInParts = checkInDate.split("/");
        String[] checkOutParts = checkOutDate.split("/");

        int checkInYear = Integer.parseInt(checkInParts[0]);
        int checkInMonth = Integer.parseInt(checkInParts[1]);
        int checkInDay = Integer.parseInt(checkInParts[2]);

        int checkOutYear = Integer.parseInt(checkOutParts[0]);
        int checkOutMonth = Integer.parseInt(checkOutParts[1]);
        int checkOutDay = Integer.parseInt(checkOutParts[2]);

        // calculate the number of days for the reservation
        LocalDate startDate = LocalDate.of(checkInYear, checkInMonth, checkInDay);
        LocalDate endDate = LocalDate.of(checkOutYear, checkOutMonth, checkOutDay);
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        // calculate the total price
        double totalPrice = roomPrice * numberOfDays;
        return totalPrice;
    }

    // inner class representing a hotel room
    private static class Room {
        private String type;
        private double price;

        public Room(String type, double price) {
            this.type = type;
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public double getPrice() {
            return price;
        }
    }

    // inner class representing a reservation
    private static class Reservation {
        private Room room;
        private String checkInDate;
        private String checkOutDate;
        private String name;
        private String idNumber;
        private String phoneNumber;
        private String email;
        private double totalPrice; // new field for total price

        public Reservation(Room room, String checkInDate, String checkOutDate, String name, String idNumber, String phoneNumber, String email, double totalPrice) {
            this.room = room;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.name = name;
            this.idNumber = idNumber;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.totalPrice = totalPrice; // initialize the total price
        }

        public Room getRoom() {
            return room;
        }

        public String getCheckInDate() {
            return checkInDate;
        }

        public String getCheckOutDate() {
            return checkOutDate;
        }

        public String getName() {
            return name;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}
