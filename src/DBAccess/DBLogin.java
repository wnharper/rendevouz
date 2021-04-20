package DBAccess;

import Database.DBConnection;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h2>Data Base Login</h2>
 * DBLogin provides methods in order to validate user credentials and obtain user attributes
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class DBLogin {

    /**
     * Method verifies whether username and password are present in the database
     * @return boolean
     */
    public static boolean verifyUserPass(String username, String password) {

        try {
            String sqlQuery = "SELECT COUNT(1) FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "';";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) return true;
                else { return false;}

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * Method returns a user object containing user details
     * @return User object
     */
    public static User getUser(String username, String password) {

        User newUser = null;
        try {
            String sqlQuery = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "';";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("User_ID");
                String user = resultSet.getString("User_Name");
                String pass = resultSet.getString("Password");
                newUser = new User(id, user, pass);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return newUser;
    }


}
