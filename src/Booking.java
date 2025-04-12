class Booking {
    private String passengerId;
    private String flightId;
    private int seatNumber;

    public Booking(String passengerId, String flightId, int seatNumber) {
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getFlightId() {
        return flightId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void displayBooking() {
        System.out.println("Passenger ID: " + passengerId);
        System.out.println("Flight ID: " + flightId);
        System.out.println("Seat Number: " + seatNumber);
    }
}
