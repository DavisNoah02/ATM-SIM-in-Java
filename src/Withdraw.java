import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Withdraw extends BankDb {
    public Withdraw() {
        super();
    }

    // public String performWithdrawal(int acc_number, double amount) {
    //     String result = "error";
    
    //     try (Connection connection = connect();
    //          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ?")) {
    //         preparedStatement.setDouble(1, amount);
    //         preparedStatement.setInt(2, acc_number);
    
    //         System.out.println("Executing SQL: " + preparedStatement.toString());
    
    //         int rowsAffected = preparedStatement.executeUpdate();
    
    //         System.out.println("Rows Affected: " + rowsAffected);
    
    //         if (rowsAffected > 0) {
    //             result = "success";
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    
    //     return result;
    // }
    // }

    public String performWithdrawal(int acc_number, double Withdrawalamount) {
        String result = "error";
        try (Connection connection = connect();
             PreparedStatement PreparedStatement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ?")) {
    
            PreparedStatement.setInt(1, acc_number);
            PreparedStatement.setDouble(2, Withdrawalamount);
            int rowsAffected = PreparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                result = "success";
            } else {
        
                System.out.println("Withdrawal failed: "); 
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
           
        }
        return result;
    }
}
