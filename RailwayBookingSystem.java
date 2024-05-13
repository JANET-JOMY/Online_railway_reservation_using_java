import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

// Step 1: Implement Inheritance
abstract class Train {
    protected String trainName;
    protected int capacity;
    protected int coachPrice;

    public Train(String trainName, int capacity, int coachPrice) {
        this.trainName = trainName;
        this.capacity = capacity;
        this.coachPrice = coachPrice;
    }

    abstract void displayInfo();
}

class LocalTrain extends Train {
    public LocalTrain(String trainName, int capacity, int coachPrice) {
        super(trainName, capacity, coachPrice);
    }

    void displayInfo() {
        System.out.println("Local Train - Name: " + trainName + ", Capacity: " + capacity);
    }
}

class ExpressTrain extends Train {
    public ExpressTrain(String trainName, int capacity, int coachPrice) {
        super(trainName, capacity, coachPrice);
    }

    void displayInfo() {
        System.out.println("Express Train - Name: " + trainName + ", Capacity: " + capacity);
    }
}

// Step 2: Implement Custom Exception Handling
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Step 3: Implement a Booking System with Multithreading
class BookingSystem implements Runnable {
    private Train train;
    private int availableSeats;

    public BookingSystem(Train train, int availableSeats) {
        this.train = train;
        this.availableSeats = availableSeats;
    }

    public synchronized void bookTicket(String name, int age, String date, String from, String to, String coachType, int numSeats, String paymentMethod) throws BookingException {
        // Validate date, age, from, to, etc. (same validation as before)

        if (availableSeats < numSeats) {
            throw new BookingException("Not enough seats available.");
        }

        // Validate age
        if (age <= 0 || age > 100) {
            throw new BookingException("Please enter a valid age (between 1 and 100).");
        }

        // Deduct booked seats from available seats
        availableSeats -= numSeats;

        // Calculate total price before GST
        double totalPriceBeforeGST = train.coachPrice * numSeats;

        // Calculate GST based on payment method
        double gst = 0;
        switch (paymentMethod) {
            case "Google Pay":
                gst = 10;
                break;
            case "Amazon Pay":
                gst = 5;
                break;
            case "PhonePe":
                gst = 2;
                break;
            default:
                throw new BookingException("Invalid payment method selected.");
        }

        // Calculate total price after including GST
        double totalPrice = totalPriceBeforeGST;

        // Print booking details in a box-like format
        System.out.println("+---------------------------------------------+");
        System.out.println("|                Booking Details               |");
        System.out.println("+---------------------------------------------+");
        System.out.println("| Name:           " + name);
        System.out.println("| Age:            " + age);
        System.out.println("| Date:           " + date);
        System.out.println("| From:           " + from);
        System.out.println("| To:             " + to);
        System.out.println("| Train:          " + train.trainName);
        System.out.println("| Coach Type:     " + coachType);
        System.out.println("| Number of Seats:" + numSeats);
        System.out.println("| Amount:         " + totalPrice);
        System.out.println("| Payment Method: " + paymentMethod);
        System.out.println("| Successfully booked!");
        System.out.println("+---------------------------------------------+");

        // Print remaining available seats
        System.out.println("Remaining Available Seats: " + availableSeats);

        // Print final message
        System.out.println("You have successfully booked your " + train.trainName + " from " + from + " to " + to + ".");
    }

    // Validate date within the range of April 30 to December 31, 2024
    public boolean isValidDate(String date) {
        // Parse the date string into LocalDate
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Define the start and end dates of the valid range
        LocalDate startDate = LocalDate.of(2024, 4, 29);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // Check if the parsed date is within the valid range
        return !parsedDate.isBefore(startDate) && !parsedDate.isAfter(endDate);
    }

    public void run() {
        // No need to implement run method in this context
    }
}

// Step 4: Incorporate Generic Concepts
class Utility {
    public static <T> void printArray(T[] array) {
        for (T item : array) {
            System.out.println(item);
        }
    }
}

public class RailwayBookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Fixed available seats
        int availableSeats = 1000;

        System.out.println("Welcome to My Online Railway Booking System (KOCHI <-> BANGALORE)");
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Enter Number of Seats Required:");

        // Number of seats required
        int numSeats = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        // Array to store passenger details
        String[] names = new String[numSeats];
        int[] ages = new int[numSeats];

        // Input passenger details
        for (int i = 0; i < numSeats; i++) {
            System.out.println("Passenger " + (i + 1) + " Details:");

            // Prompt and validate name
            String name;
            do {
                System.out.print("Name: ");
                name = scanner.nextLine();

                // Validate name (Check if it contains any digits)
                if (name.matches(".*\\d.*")) {
                    System.out.println("Name should not contain numbers. Please enter a valid name.");
                }
            } while (name.matches(".*\\d.*")); // Loop until a valid name is entered

            // Store the validated name
            names[i] = name;

            // Prompt for age
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            // Store age
            ages[i] = age;
        }

        System.out.print("Date of Travel (DD/MM/YYYY): ");
        String date = scanner.nextLine();

        // Validate date
        BookingSystem booking = new BookingSystem(null, 0);
        if (!booking.isValidDate(date)) {
            System.out.println("Invalid date. Please enter a date between April 29, 2024, and December 31, 2024.");
            return;
        }

        System.out.print("From (Getting in): ");
        String from = scanner.nextLine();

        System.out.print("To (Destination): ");
        String to = scanner.nextLine();

        System.out.println("Select Train Number (16525, 16526, 12677): ");
                int trainNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        // Assign train based on train number
        Train train = null;
        switch (trainNumber) {
            case 16525:
                train = new ExpressTrain("Kanyakumari Cape Express", availableSeats, 1800);
                break;
            case 16526:
                train = new ExpressTrain("Kochvl-Mysore Express", availableSeats, 1800);
                break;
            case 12677:
                train = new ExpressTrain("Ernakulam Intercity Express", availableSeats, 1800);
                break;
            default:
                System.out.println("Invalid train number.");
                return;
        }

        // Display available time options based on the selected train
        switch (trainNumber) {
            case 16525:
                System.out.println("Available Time Options: [9:00, 15:20, 19:20]");
                break;
            case 16526:
                System.out.println("Available Time Options: [9:30, 14:45, 18:30]");
                break;
            case 12677:
                System.out.println("Available Time Options: [10:00, 16:15, 20:00]");
                break;
            default:
                System.out.println("Invalid train number.");
                return;
        }

        System.out.print("Enter Preferred Time (HH:mm): ");
        String preferredTime = scanner.nextLine();

        // Validate time format
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(preferredTime, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Enter time in the correct format (HH:mm).");
            return;
        }

        // Check if the entered time is available for booking
        // Add your logic here to check if the entered time is valid for the selected train
        // For example:
        switch (trainNumber) {
            case 16525:
                if (!(preferredTime.equals("9:00") || preferredTime.equals("15:20") || preferredTime.equals("19:20"))) {
                    System.out.println("No train available for this time.");
                    return;
                }
                break;
            case 16526:
                if (!(preferredTime.equals("9:30") || preferredTime.equals("14:45") || preferredTime.equals("18:30"))) {
                    System.out.println("No train available for this time.");
                    return;
                }
                break;
            case 12677:
                if (!(preferredTime.equals("10:00") || preferredTime.equals("16:15") || preferredTime.equals("20:00"))) {
                    System.out.println("No train available for this time.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid train number.");
                return;
        }

        System.out.print("Preferred Coach Type (General/3AC/2AC): ");
        String coachType = scanner.nextLine();

        // Create booking system with selected train and available seats
        booking = new BookingSystem(train, availableSeats);

        System.out.print("Select Payment Method (Google Pay/Amazon Pay/PhonePe): ");
        String paymentMethod = scanner.nextLine();

        try {
            // Calculate total price based on number of seats and coach price
            double totalPriceBeforeGST = train.coachPrice * numSeats;
            double gst = 0;
            switch (paymentMethod) {
                case "Google Pay":
                    gst = 10;
                    break;
                case "Amazon Pay":
                    gst = 5;
                    break;
                case "PhonePe":
                    gst = 2;
                    break;
                default:
                    throw new BookingException("Invalid payment method selected.");
            }
            double totalPrice = totalPriceBeforeGST + gst;

            // Book tickets for each passenger
            for (int i = 0; i < numSeats; i++) {
                booking.bookTicket(names[i], ages[i], date, from, to, coachType, 1, paymentMethod);
            }

            // Print total price after booking all tickets
            System.out.println("\nTotal Amount to be Paid (incl. GST): " + totalPrice);

            // Print train details
            System.out.println("\n+---------------------------------------------+");
            System.out.println("|               Train Details                 |");
            System.out.println("+---------------------------------------------+");
            System.out.println("| Train Name:     " + train.trainName);
            System.out.println("| From:           " + from);
            System.out.println("| To:             " + to);
            System.out.println("| Time:           " + preferredTime);
            System.out.println("| Coach Type:     " + coachType);
            System.out.println("| Number of Seats:" + numSeats);
            System.out.println("| Total Price (incl. GST): " + totalPrice);
            System.out.println("+---------------------------------------------+");

        } catch (BookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        scanner.close();
    }
}

