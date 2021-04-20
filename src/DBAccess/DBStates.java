package DBAccess;

import Database.DBConnection;
import Model.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h2>Data Base States</h2>
 * DBStates provides methods in order to retrieve data from the database
 * related to country states
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class DBStates {
    /**
     * Method returns a list of all US state Objects
     * @param countryId of states to be returned
     * @return Observable List
     */
    public static ObservableList<State> getAllStates(int countryId) {
        ObservableList<State> stateList = FXCollections.observableArrayList();

        try {
            String id = Integer.toString(countryId);
            String sqlQuery = "SELECT first_level_divisions.Division_ID, first_level_divisions.Division FROM first_level_divisions WHERE COUNTRY_ID=" + id + ";";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int stateId = resultSet.getInt("Division_ID");
                String name = resultSet.getString("Division");
                int country = resultSet.getInt("COUNTRY_ID");
                State state = new State(stateId, name, country);

                stateList.add(state);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateList;
    }

    /**
     * Method returns a list of all the US states in String format
     * @return Observable List
     * @param countryId of states to be returned
     */
    public static ObservableList<String> getAllStatesString(int countryId) {
        ObservableList<String> stateList = FXCollections.observableArrayList();

        try {
            String id = Integer.toString(countryId);
            String sqlQuery = "SELECT first_level_divisions.Division FROM first_level_divisions WHERE COUNTRY_ID=" + id + ";";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Division");
                stateList.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateList;
    }

    /**
     * Method takes state name and queries the database, returning the corresponding
     * state ID int
     * @param stateName
     * @return state ID
     */
    public static int getStateId(String stateName) {
        int stateId = 0;
        try {

            String sqlQuery = "SELECT first_level_divisions.Division_ID FROM first_level_divisions WHERE Division = '" + stateName + "';";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                stateId = resultSet.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateId;
    }

    /**
     * Method takes state id and queries the database, returning the corresponding
     * state name
     * @param stateId ID
     * @return state Name string
     */
    public static String getStateName(int stateId) {
        String stateName = null;
        try {

            String sqlQuery = "SELECT first_level_divisions.Division FROM first_level_divisions WHERE Division_ID = " + stateId + ";";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                stateName = resultSet.getString("Division");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateName;
    }
}
