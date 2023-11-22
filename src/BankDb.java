import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDb {
    private String servername;
    private String username;
    private String password;
    private String dbname;

    public BankDb() {
        this.servername = "localhost";
        this.username = "root";
        this.password = "Lee1992.";
        this.dbname = "myatmdb";
    }

    public Connection connect() {
        Connection connection = null;
        try {

            String url = "jdbc:mysql://" + servername + "/" + dbname;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet getData(String sql) {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

   
}
