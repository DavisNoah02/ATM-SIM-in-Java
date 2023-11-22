public class ATMScreen {
    public void displayBalance(double availableBalance, double totalBalance) {
        System.out.println("Available Balance: $" + availableBalance);
        System.out.println("Total Balance: $" + totalBalance);
    }

    public void displayMessage(String string) {
        System.out.println(string);
    }
}

