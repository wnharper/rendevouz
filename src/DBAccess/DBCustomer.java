package DBAccess;

import Database.DBConnection;
import GUI.Login;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCustomer {

    /**
     * Method returns a list of all customer Objects
     * @return Observable List
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT  customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Phone, customers.Postal_Code, first_level_divisions.Division, countries.Country,\n" +
                    " (SELECT COUNT(*) FROM appointments WHERE customers.Customer_ID = appointments.Customer_ID) AS 'Appointments' FROM customers \n" +
                    "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                    "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString("Address");
                String country = resultSet.getString("Country");
                int appointments = resultSet.getInt("Appointments");
                String state = resultSet.getString("Division");
                String phone = resultSet.getString("Phone");
                String postCode = resultSet.getString("Postal_Code");
                Customer customer = new Customer(customerId, customerName, customerAddress, postCode, country, state, phone, appointments);

                customerList.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * Method returns a list of all the customers in String format
     * @return Observable List
     */
    public static ObservableList<String> getAllCustomersString() {
        ObservableList<String> customerList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "select customers.Customer_Name from customers";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Customer_Name");
                customerList.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * Method takes customer attributes and inserts them into customer table in database
     * @param customerID
     * @param name
     * @param address
     * @param postcode
     * @param phone
     * @param stateID
     */
    public static void insertCustomer(int customerID, String name, String address, String postcode, String phone, int stateID) {

        try {
            String sqlQuery = "INSERT INTO customers VALUES(" + customerID + " , '" + name+ "', '" + address + "', '"
                                                              + postcode + "', '" + phone + "', NOW(), 'script', NOW(), 'script', "
                                                              + stateID + ");";

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method takes customer attributes and updates customer in database according to customer ID
     * @param customerID
     * @param name
     * @param address
     * @param postcode
     * @param phone
     * @param stateID
     */
    public static void updateCustomer(int customerID, String name, String address, String postcode, String phone, int stateID) {

        try {
            String sqlQuery = "UPDATE customers SET Customer_Name = '" + name + "', Address = '" + address + "', Postal_Code = '" + postcode +
                              "', Phone = '" + phone + "', Last_Update = now(), Last_Updated_By = '" + Login.currentUser.getUserId() + "', Division_ID = " + stateID + " WHERE Customer_ID = " + customerID + ";";

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method returns a new customer ID by retrieving the last customer ID in the
     * customer table and adding 1.
     * @return new customer ID int
     */
    public static int getNewCustomerId() {

        int id = 0;

        try {
            String sqlQuery = "SELECT * FROM customers ORDER BY Customer_ID DESC LIMIT 1;";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet == null) return 1;

            while (resultSet.next()) {
                // add 1 to the last customer ID
                id = resultSet.getInt("Customer_ID") + 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    /**
     * Method deletes customer in database according to customer ID
     * @param customerID
     */
    public static void deleteCustomer(int customerID) {

        try {
            String sqlQuery = "DELETE FROM customers WHERE Customer_ID = " + customerID + ";";

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }






}
