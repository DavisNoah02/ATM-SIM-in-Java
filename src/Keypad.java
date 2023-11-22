import java.util.Scanner;

public class Keypad {
    private Scanner scanner;

    public Keypad() {
        scanner = new Scanner(System.in);
    }

    public int getInputPIN() {
        System.out.print("Enter your PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return pin;
    }

    public double getInputAmount() {
        System.out.print("Enter the amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        return amount;
    }

    public int getInputChoice() {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return choice;
    }
}

