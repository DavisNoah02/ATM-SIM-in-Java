import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAcc extends BankDb {
    public UserAcc() {
        super();
    }
    public class UserAccountData {
        private int accountNumber;
        private int pin;
        private double balance;

        public UserAccountData(int accountNumber, int pin, double balance) {
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = balance;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public int getPin() {
            return pin;
        }

        public double getBalance() {
            return balance;
        }
    }
    public int createAccount(int pin, double balance) {
        int accountNumber = -1;
    
        try (Connection connection = connect()) {
            String insertQuery = "INSERT INTO accounts (pin, balance) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, pin);
                preparedStatement.setDouble(2, balance);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        accountNumber = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return accountNumber;
    }
    

    // public int createAccount(int pin, double balance) {
    //     int accountnumber = -1;

    //     try (Connection connection = connect();
    //          CallableStatement callableStatement = connection.prepareCall("{call sp_saveaccount(?, ?, ?)}")) {
    //         callableStatement.setInt(1, pin);
    //         callableStatement.setDouble(2, balance);
    //         callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
    //         callableStatement.execute();
    //         accountnumber = callableStatement.getInt(3);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return accountnumber;
    // }

    public UserAccountData retrieveAccount(int enteredPin) {
        UserAccountData accountData = null;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT pin, balance FROM accounts WHERE pin = ?")) {
             preparedStatement.setInt(1, enteredPin);
             ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int pin = resultSet.getInt("pin");
                double balance = resultSet.getDouble("balance");
                accountData = new UserAccountData(enteredPin, pin, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountData;
    }

    // public void deleteAccount(int accountnmber) {
    //     try (Connection connection = connect();
    //          PreparedStatement preparedStatement = connection.prepareStatement("{call sp_deleteaccount(?)}")) {
    //          preparedStatement.setInt(1, accountnmber);
    //          preparedStatement.execute();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    public void deleteAccount(int accountNumber) {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM accounts WHERE account_number = ?")) {
            preparedStatement.setInt(1, accountNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account with account number " + accountNumber + " has been deleted.");
            } else {
                System.out.println("Account with account number " + accountNumber + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    
}
