package DBAccess;

import Database.DBConnection;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {

    /**
     * Method returns a list of all the contacts in String format
     * @return Observable List
     */
    public static ObservableList<String> getAllContactsString() {
        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT contacts.Contact_Name FROM contacts;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Contact_Name");
                list.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    /**
     * Method returns a list of all the contacts in object form from database
     * @return Observable List
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> list = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT * FROM contacts;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Contact_ID");
                String name = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                Contact contact = new Contact(id, name, email);
                list.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
