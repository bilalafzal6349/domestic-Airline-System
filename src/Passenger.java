import java.io.IOException;

class Passenger extends User {
    private String passportNumber;

    public Passenger(String id, String name, String password, String passportNumber) {
        super(id, name, password);
        this.passportNumber = passportNumber;
    }

    @Override
    public void displayInfo() {
        System.out.println("Passenger ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Passport Number: " + passportNumber);
    }

    public void passengerMenu()   {
        System.out.println("Passenger Menu:");
        System.out.println("1. View Flights");
        System.out.println("2. Book a Flight");
        System.out.println("3. View Bookings");
        System.out.println("4. Cancel Booking");
        System.out.println("5. Logout");
    }
}
