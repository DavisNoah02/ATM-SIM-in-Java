import java.util.Scanner;

public class AtmMain {
    public static void main(String[] args) {
        Keypad keypad = new Keypad();
        Transaction transaction = new Transaction();
        UserAcc userAccount = new UserAcc();
        Withdraw withdraw = new Withdraw();
        ATMScreen atmScreen = new ATMScreen();
        CashDispenser cashDispenser = new CashDispenser();

        try (Scanner scanner = new Scanner(System.in)) {
            // Prompt the user to enter their PIN for login

            System.out.print("Please enter your Account Number: ");
            int accountNumber = scanner.nextInt();
             scanner.nextLine(); // Consume the newline character

             System.out.print("Please enter your PIN: ");
            int enteredPin = scanner.nextInt();
             scanner.nextLine();
             
            // Simulate a user login by validating the PIN and obtaining the account ID
            int acc_number= simulateUserLogin(userAccount, enteredPin, accountNumber);
 
            if (acc_number != -1) {
                // Display a welcome message on the ATM screen
                atmScreen.displayMessage("Welcome to Your ATM");

                while (true) {
                    // Show menu options
                    atmScreen.displayMessage("Select an option:");
                    atmScreen.displayMessage("1. Check Balance");
                    atmScreen.displayMessage("2. Withdraw");
                    atmScreen.displayMessage("3. Deposit");
                    atmScreen.displayMessage("4. Transfer");
                    atmScreen.displayMessage("5. Pay Bills");
                    atmScreen.displayMessage("6. Exit");

                    // Capture user choice
                    int choice = keypad.getInputChoice();

                    switch (choice) {
                        case 1:
                            // Retrieve the user's account data
                            UserAcc.UserAccountData accountData = userAccount.retrieveAccount(acc_number);

                            // Display account balance on the ATM screen
                            atmScreen.displayBalance(accountData.getBalance(), accountData.getBalance());
                            break;
                        case 2:
                            // Capture user input for the withdrawal amount
                            double withdrawalAmount = keypad.getInputAmount();

                            // Check if withdrawal is allowed
                            UserAcc.UserAccountData accountdata = userAccount.retrieveAccount(acc_number);

                            if (withdrawalAmount > 0 && withdrawalAmount <= accountdata.getBalance()) {
                                // Perform the withdrawal
                                String withdrawalResult = withdraw.performWithdrawal(acc_number, withdrawalAmount);

                                // Dispense cash if the withdrawal was successful
                                if (withdrawalResult.equals("success")) {
                                    cashDispenser.dispenseCash(withdrawalAmount);
                                }

                                // Display the withdrawal result on the ATM screen
                                atmScreen.displayMessage(withdrawalResult);
                            } else {
                                atmScreen.displayMessage("Invalid withdrawal amount or insufficient funds.");
                            }
                            break;
                        case 3:
                            // Capture user input for the deposit amount
                            double depositAmount = keypad.getInputAmount();

                            if (depositAmount > 0) {
                                // Perform the deposit
                                //userAccount.deposit(accountId, depositAmount);
                                transaction.deposit(acc_number,depositAmount);

                                // Display the deposit result on the ATM screen
                                atmScreen.displayMessage("Deposit successful.");
                            } else {
                                atmScreen.displayMessage("Invalid deposit amount.");
                            }
                            break;
                        case 4:
                            // Capture user input for the receiver's account and the transfer amount
                           System.out.print("Enter receiver's account number: ");
                            int receiverAccountId = scanner.nextInt();
                           scanner.nextLine(); // Consume the newline character

                           System.out.print("Enter transfer amount: ");
                           double transferAmount = scanner.nextDouble();
                           scanner.nextLine(); // Consume the newline character

                            // Perform the transfer
                           String transferResult = transaction.transfer(acc_number, receiverAccountId, transferAmount);

                            // Display the transfer result on the ATM screen
                           atmScreen.displayMessage(transferResult);
                            break;
                        case 5:
                        //     // Capture user input for the bill payment amount and payee
                        //    System.out.print("Enter payee name: ");
                        //    String payee = scanner.nextLine();

                        //    System.out.print("Enter bill payment amount: ");
                        //    double billAmount = scanner.nextDouble();
                        //    scanner.nextLine(); // Consume the newline character

                        //     // Perform the bill payment
                        //     String billPaymentResult = transaction.billPayment(accresult, billAmount, payee);

                        //     // Display the bill payment result on the ATM screen
                        //     atmScreen.displayMessage(billPaymentResult);
                        //     break;
                        case 6:
                            atmScreen.displayMessage("Thank you for using Your ATM! Please Take Your Card");
                            System.exit(0);
                        default:
                            atmScreen.displayMessage("Invalid choice. Please try again.");
                    }
                }
            } else {
                atmScreen.displayMessage("Invalid PIN. Access denied.");
            }
        }
    }

    public static int simulateUserLogin(UserAcc userAccount, int enteredPin, int accountNumber) {
        // Replace this logic with your actual user login mechanism
        // In this simulation, we'll check if the account exists in the database

        // Retrieve the user's account data based on the entered PIN
        UserAcc.UserAccountData accountData = userAccount.retrieveAccount(enteredPin);

        if (accountData != null) {
            // The account exists; return the account ID
            return accountData.getAccountNumber();
        } else {
            // The account doesn't exist; return -1 to indicate an invalid login
            return -1;
        }
    }
}
