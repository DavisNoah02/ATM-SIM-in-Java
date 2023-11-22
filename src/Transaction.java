import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends BankDb {
    public Transaction() {
        super();
    }

    public double getBalance(int accresult) {
    try (Connection connection = connect();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ?")) {
        
        preparedStatement.setInt(1, accresult);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            return resultSet.getDouble("balance");
        } else {
            // Handle the case where the account doesn't exist or some other error
            return 0.0; // Return a default value or handle the error appropriately
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return 0.0; // Return a default value or handle the error appropriately
    }
}
 
    
    

public void deposit(int dpresult, double amount) {

    if (amount <= 0) {
        System.out.println("Invalid deposit amount.");
        return;
    }

    try (Connection connection = connect();
         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE acc_no = ?")) {
        
        preparedStatement.setDouble(1, amount);
        preparedStatement.setInt(2, dpresult);
        preparedStatement.executeUpdate();
    }  
     catch (SQLException e) {
        e.printStackTrace();
    }
    
}


    // public boolean withdrawals(int accresult, double amount) {
    //     try (Connection connection = connect();
    //          PreparedStatement PreparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ?")) {
    
    //         PreparedStatement.setInt(1, accresult);
    //         PreparedStatement.setDouble(2, amount);
    //         int rowsAffected = PreparedStatement.executeUpdate();

    //         if (rowsAffected > 0) {
    //             return true;
    //         } else {
        
    //             System.out.println("Withdrawal failed: ");
    //             return false;
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return false; // Return false to indicate a failure
    //     }
    // }
    
    

    // public String transfer(int senderaccresult, int receiveraccresult, double amount) {
    //     try (Connection connection = connect();
    //          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?")) {
    //         preparedStatement.setDouble(1, amount);
    //         preparedStatement.setInt(2, senderaccresult);
    //         preparedStatement.setDouble(3, amount);
    
    //         int rowsAffected = preparedStatement.executeUpdate();
    //         if (rowsAffected == 1) {
    //             System.out.println("Transfer Successful");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return null; // You might want to return a result message indicating the success or failure of the transfer.
    // }
    
    
    public String  transfer(int senderAccountId, int receiverAccountId, double amount) {
        try(Connection connection = connect();) {
            
            connection.setAutoCommit(false);// start a transaction

            //updating sender's acc balance
            try (PreparedStatement senderStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ? AND balance >= ?")) {
                senderStatement.setDouble(1, amount);
                senderStatement.setInt(2, senderAccountId);
                senderStatement.setDouble(3, amount);
    
                int senderRowsAffected = senderStatement.executeUpdate();
    
                if (senderRowsAffected > 0) {
                    // Update receiver's account balance
                    try (PreparedStatement receiverStatement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE acc_no = ?")) {
                        receiverStatement.setDouble(1, amount);
                        receiverStatement.setInt(2, receiverAccountId);
    
                        int receiverRowsAffected = receiverStatement.executeUpdate();
    
                        if (receiverRowsAffected == 1) {
                            connection.commit(); // Commit the transaction
                            System.out.println("Transfer Successful");
                        } else {
                            connection.rollback(); // Rollback if receiver update fails
                            System.out.println("Receiver account not found or insufficient funds");
                        }
                    }
                } else {
                   
                    System.out.println("Sender account not found or insufficient funds");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error occured during transfer";
    }
    
    
    

    public String billPayment(int accresult, double amount, String payee) {
        try (Connection connection = connect()) {
            String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, amount);
                preparedStatement.setInt(2, accresult);
                preparedStatement.setDouble(3, amount);
    
                int rowsUpdated = preparedStatement.executeUpdate();
    
                if (rowsUpdated == 1) {
                    System.out.println("Bill payment was successful");
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payee;
    }
    
    

    public List<String> getTransactionHistory(int accresult) {
        List<String> transactionHistory = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareCall("{call sp_transactionhistory(?)}")) {
            preparedStatement.setInt(1, accresult);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String transactionDetails = resultSet.getString("transaction_details");
                transactionHistory.add(transactionDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }
}

