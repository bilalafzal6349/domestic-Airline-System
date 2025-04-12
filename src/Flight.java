import java.util.HashMap;
import java.util.Map;

class Flight {
    private String flightId;
    private String origin;
    private String destination;
    private int totalSeats;
    private HashMap<Integer, Boolean> seatMap;

    public Flight(String flightId, String origin, String destination, int totalSeats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.seatMap = new HashMap<>();
        for (int i = 1; i <= totalSeats; i++) {
            seatMap.put(i, true);
        }
    }

    public String getFlightId() {
        return flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return (int) seatMap.values().stream().filter(seat -> seat).count();
    }

    public void displaySeatMap() {
        System.out.println("Seat Availability:");
        for (int i = 1; i <= totalSeats; i++) {
            System.out.print("Seat " + i + (seatMap.get(i) ? " (Available) " : " (Booked) "));
            if (i % 10 == 0) System.out.println();
        }
        System.out.println();
    }

    public boolean bookSeat(int seatNumber) {
        if (seatMap.containsKey(seatNumber) && seatMap.get(seatNumber)) {
            seatMap.put(seatNumber, false);
            return true;
        }
        return false;
    }

    public void displayFlightDetails() {
        System.out.println("Flight ID: " + flightId);
        System.out.println("Origin: " + origin);
        System.out.println("Destination: " + destination);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Available Seats: " + getAvailableSeats());
    }
}
