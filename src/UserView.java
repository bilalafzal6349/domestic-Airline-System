import java.io.IOException;
import java.util.*;
import java.io.*;
class UserView {
    private Scanner scanner = new Scanner(System.in);
    private List<Flight> flights = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public UserView() {
        flights.add(new Flight("PK101", "Karachi", "Islamabad", 150));
        flights.add(new Flight("PK102", "Lahore", "Karachi", 180));
        flights.add(new Flight("PK103", "Islamabad", "Quetta", 100));
        flights.add(new Flight("PK104", "Peshawar", "Lahore", 120));
    }

    private void signUpUser() throws IOException {
        System.out.print("Enter ID: ");
        String signUpId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String signUpName = scanner.nextLine();
        System.out.print("Enter Password: ");
        String signUpPassword = scanner.nextLine();
        System.out.print("Are you signing up as Passenger or Staff? (P/S): ");
        String userType = scanner.nextLine();

        if (userType.equalsIgnoreCase("P")) {
            System.out.print("Enter Passport Number: ");
            String passport = scanner.nextLine();
            FileManager.appendToFile("data/users.txt", " id : "+signUpId + "," + "Name : "+ signUpName + "," + "password : "+ signUpPassword + "  role: " + ",Passenger," + "passport id : " +  passport);
            System.out.println("Passenger sign-up successful!");
        } else if (userType.equalsIgnoreCase("S")) {
            System.out.print("Enter Designation: ");
            String designation = scanner.nextLine();
            FileManager.appendToFile("data/users.txt", "id : " + signUpId + "," + "Name : " +  signUpName + "," + "password : " +  signUpPassword + " role : " +  ",Staff," + "designation: " +  designation);
            System.out.println("Staff sign-up successful!");
        } else {
            System.out.println("Invalid user type. Please try again.");
        }
    }

    private void loginUser() throws IOException {
        System.out.print("Enter ID: ");
        String loginId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String loginPassword = scanner.nextLine();

        List<String> users = FileManager.readFile("data/users.txt");

        for (String user : users) {
            String[] userData = user.split(",");
            if (userData[0].equals(loginId) && userData[2].equals(loginPassword)) {
                System.out.println("Login successful! Welcome, " + userData[1]);
                if (userData[3].equals("Passenger")) {
                    Passenger passenger = new Passenger(userData[0], userData[1], userData[2], userData[4]);
                    handlePassengerActions(passenger);
                } else if (userData[3].equals("Staff")) {
                    Staff staff = new Staff(userData[0], userData[1], userData[2], userData[4]);
                    handleStaffActions(staff);
                }
                return;
            }
        }

        System.out.println("Invalid credentials. Please try again.");
    }
    public void displayMenu() {
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Exit");
    }

    public void handleUserActions() {
        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        loginUser();
                        break;
                    case 2:
                        signUpUser();
                        break;
                    case 3:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (IOException e) {
                System.err.println("File error: " + e.getMessage());
            }
        }
    }
    private void handlePassengerActions(Passenger passenger) {
        boolean running = true;
        while (running) {
            try {
                passenger.passengerMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewFlights();
                        break;
                    case 2:
                        bookFlight(passenger);
                        break;
                    case 3:
                        viewBookings(passenger);
                        break;
                    case 4:
                        cancelBooking(passenger);
                        break;
                    case 5:
                        System.out.println("Logging out... Goodbye, " + passenger.getName());
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }  catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void handleStaffActions(Staff staff) {
        boolean running = true;
        while (running) {
            try {
                staff.staffMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addFlight();
                        break;
                    case 2:
                        removeFlight();
                        break;
                    case 3:
                        viewPassengers();
                        break;
                    case 4:
                        System.out.println("Logging out... Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }


// passenger methods
    private void viewFlights() {
        System.out.println("Available Flights:");
        for (Flight flight : flights) {
            flight.displayFlightDetails();
            System.out.println("-----------------------");
        }
    }

    private void bookFlight(Passenger passenger) {
        viewFlights();
        System.out.print("Enter the Flight ID you want to book: ");
        String flightId = scanner.nextLine();

        for (Flight flight : flights) {
            if (flight.getFlightId().equalsIgnoreCase(flightId)) {
                System.out.println("Select a seat to book:");
                flight.displaySeatMap();
                System.out.print("Enter seat number: ");
                int seatNumber = scanner.nextInt();
                scanner.nextLine();

                if (flight.bookSeat(seatNumber)) {
                    bookings.add(new Booking(passenger.getId(), flightId, seatNumber));
                    System.out.println(" Seat No " + seatNumber + " is  booked successfully! on this ID :  " + flightId);
                    return;
                } else {
                    System.out.println("Seat already booked or invalid seat number.");
                }
            }
        };
//        if the flight no equal to flight id then no run the if condition and after this statement is encountered

        System.out.println("Invalid Flight ID.");
    }

    private void viewBookings(Passenger passenger) {
        System.out.println("Your Bookings:");

        boolean hasBookings = false;
        for (Booking booking : bookings) {

            if (booking.getPassengerId().equalsIgnoreCase(passenger.getId())) {
                booking.displayBooking();
                System.out.println("-----------------------");
                hasBookings = true;
            }
        }
        if (!hasBookings) {
            System.out.println("No bookings found.");
        }
    }

    private void cancelBooking(Passenger passenger) {
        viewBookings(passenger);
        System.out.print("Enter Flight ID to cancel: ");
        String flightId = scanner.nextLine();
        System.out.print("Enter Seat Number to cancel: ");
        int seatNumber = scanner.nextInt();
        scanner.nextLine();

        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.getPassengerId().equals(passenger.getId()) && booking.getFlightId().equals(flightId) && booking.getSeatNumber() == seatNumber) {
                iterator.remove();
                System.out.println("Booking canceled successfully.");
                return;
            }
        }
        System.out.println("No matching booking found.");
    }


// staff functions or methods
    private void addFlight() {
        System.out.print("Enter Flight ID: ");
        String flightId = scanner.nextLine();
        System.out.print("Enter Origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter Total Seats: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine();

        flights.add(new Flight(flightId, origin, destination, totalSeats));
        System.out.println("Flight added successfully.");
    }

    private void removeFlight() {
        System.out.print("Enter Flight ID to remove: ");
        String flightId = scanner.nextLine();

        Iterator<Flight> iterator = flights.iterator();
        while (iterator.hasNext()) {
            Flight flight = iterator.next();
            if (flight.getFlightId().equalsIgnoreCase(flightId)) {
                iterator.remove();
                System.out.println("Flight removed successfully.");
                return;
            }
        }
        System.out.println("Flight not found.");
    }

    private void viewPassengers() {
        System.out.println("Booked Passengers:");
        for (Booking booking : bookings) {
            System.out.println("Passenger ID: " + booking.getPassengerId());
            System.out.println("Flight ID: " + booking.getFlightId());
            System.out.println("Seat Number: " + booking.getSeatNumber());
            System.out.println("-----------------------");
        }
    }
}
