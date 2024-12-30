import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbHelper {
    // Use an absolute path or ensure the relative path works correctly
    private static final String DATABASE_URL = "jdbc:ucanaccess://assets/BC210200167.accdb"; 
    
    static{
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                        } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    // Create a database connection
    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Insert an Inventor into the database
    public static boolean insertInventor(Inventor inventor) throws SQLException {
        try (Connection connection = createConnection()) {
            // Check if the inventor already exists
            String countQuery = "SELECT COUNT(*) FROM inventor WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(countQuery)) {
                preparedStatement.setString(1, inventor.getName());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return false; // Inventor already exists
                    }
                }
            }

            // Insert the inventor into the database
            String insertQuery = "INSERT INTO inventor (name, score) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, inventor.getName());
                preparedStatement.setInt(2, inventor.getScore());
                preparedStatement.executeUpdate();
                return true; // Insert successful
            }
        }
    }

    // Fetch all Inventors from the database
    public static List<Inventor> fetchAllInventors() throws SQLException {
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM inventor ORDER BY name ASC");
            List<Inventor> inventors = new ArrayList<>();
            while (resultSet.next()) {
                inventors.add(new Inventor(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("score")
                ));
            }
            return inventors;
        }
    }

    // Remove all Inventors from the database
    public static void removeAllInventors() throws SQLException {
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM inventor");
        }
    }

    // Fetch the top 3 Inventors based on score (correct syntax for MS Access)
    public static List<Inventor> fetchTopInventors() throws SQLException {
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();
            // MS Access uses "TOP" instead of "LIMIT"
            ResultSet resultSet = statement.executeQuery(
                "SELECT TOP 3 * FROM inventor ORDER BY score DESC"
            );
            List<Inventor> inventors = new ArrayList<>();
            while (resultSet.next()) {
                inventors.add(new Inventor(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("score")
                ));
            }
            return inventors;
        }
    }

    // Fetch top Inventor(s) (Unimplemented previously, can be used to get the highest scoring inventor)
    public static List<Inventor> getTopInventor() throws SQLException {
        // This method can return a list of the top inventor(s) based on a certain condition
        return fetchTopInventors(); // You can use the fetchTopInventors() logic here
    }

    // Delete an inventor by ID
    public static void deleteInventor() throws SQLException {
               try (Connection connection = createConnection()) {
        String deleteQuery = "DELETE FROM inventor"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate(); // Executes the delete query
        }
    }
    }
}
