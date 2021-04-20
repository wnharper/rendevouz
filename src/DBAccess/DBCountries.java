package DBAccess;

import Database.DBConnection;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h2>Data Base Countries</h2>
 * DBCountries provides methods in order to retrieve, insert, update and remove data from the database
 * related to countries
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class DBCountries {

    /**
     * Method returns a list of all Country Objects
     * @return Observable List
     */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT countries.Country_ID, countries.Country FROM countries";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Country_ID");
                String name = resultSet.getString("Country");
                Country country = new Country(id, name);

                countryList.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryList;
    }

    /**
     * Method returns a list of all the Countries in String format
     * @return Observable List
     */
    public static ObservableList<String> getAllCountriesString() {
        ObservableList<String> countryList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT countries.Country FROM countries";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Country");
                countryList.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryList;
    }
}
