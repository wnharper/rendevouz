package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Utilities.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class DBAppointments {

    /**
     * Method returns a list of all appointments occuring in the next month
     * @return Observable List
     */
    public static ObservableList<Appointment> getMonthAppointments() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, \n" +
                    "contacts.Contact_Name, appointments.Contact_ID, appointments.Type, appointments.Start, appointments.End, customers.Customer_Name, appointments.Customer_ID, datediff(End, now()) AS 'Days' FROM appointments\n" +
                    "INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID\n" +
                    "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int appId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String contact = resultSet.getString("Contact_Name");
                int contactId = resultSet.getInt("Contact_ID");
                String type = resultSet.getString("Type");
                LocalDateTime start = Time.utcToLocalTime(resultSet.getObject("Start", LocalDateTime.class));
                LocalDateTime end = Time.utcToLocalTime(resultSet.getObject("End", LocalDateTime.class));
                String customerName = resultSet.getString("Customer_Name");
                int customerId = resultSet.getInt("Customer_ID");
                int days = resultSet.getInt("Days");
                Appointment appointment = new Appointment(appId,title, description, location, contact, contactId, type, start, end, customerName, customerId, days);

                appointmentsList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * Method takes appointment attributes and inserts them into appointments table in database
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     */
    public static void insertAppointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {

        try {
            String sqlQuery = "INSERT INTO appointments VALUES(" + appointmentId + " , '" + title + "', '" + description + "', '"
                    + location + "', '" + type + "', '" + start + "', '" + end + "', NOW(), 'TODO', NOW(), 'TODO', '"
                    + customerId + "', '" + userId + "', '" + contactId + "');";

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method takes appointment attributes and updates the appointment (via Id) in the database
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     */
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {

        try {
            String sqlQuery = "UPDATE appointments SET Title ='" + title + "', Description='" + description + "', Location ='"
                    + location + "', Type = '" + type + "', Start = '" + start + "', End = '" + end + "', Last_Updated_By = 'TODO' , Customer_ID = "
                    + customerId + ", User_ID = " + userId + ", Contact_ID = " + contactId + " WHERE Appointment_ID = " + appointmentId + ";"; // TODO user id

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method returns a new appointments ID by retrieving the last appointment ID in the
     * appointments table and adding 1.
     * @return new customer ID int
     */
    public static int getNewAppointmentId() {

        int id = 0;

        try {
            String sqlQuery = "SELECT * FROM appointments ORDER BY Appointment_ID DESC LIMIT 1;";
            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pStatement.executeQuery();

            if (resultSet == null) return 1;

            while (resultSet.next()) {
                // add 1 to the last appointment ID
                id = resultSet.getInt("Appointment_ID") + 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    /**
     * Method takes deletes appointment in database according to appointment ID
     * @param appointmentId
     */
    public static void deleteAppointment(int appointmentId) {

        try {
            String sqlQuery = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentId + ";";

            Statement statement = DBConnection.getDbConnection().createStatement();
            statement.execute(sqlQuery);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Method returns a list of all appointments occuring in the next month
     * @return Observable List
     */
    public static ObservableList<Appointment> getCustomerAppointments(int customer_id) {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT appointments.Appointment_ID, appointments.Start, appointments.End, appointments.Customer_ID FROM appointments WHERE Customer_ID = " + customer_id + ";";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int appId = resultSet.getInt("Appointment_ID");
                LocalDateTime start = resultSet.getObject("Start", LocalDateTime.class);
                LocalDateTime end = resultSet.getObject("End", LocalDateTime.class);
                int customerId = resultSet.getInt("Customer_ID");
                Appointment appointment = new Appointment(appId, start, end, customerId);

                appointmentsList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsList;
    }
}
