package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Utilities.Alerts;
import Utilities.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DBAppointments {

    /**
     * Method returns a list of all appointments occuring in the next month
     * @return Observable List
     */
    public static ObservableList<Appointment> getMonthAppointments() {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        try {
            String sqlQuery = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, \n" +
                    "contacts.Contact_Name, appointments.Type, appointments.Start, appointments.End, customers.Customer_Name, datediff(End, now()) AS 'Days' FROM appointments\n" +
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
                String type = resultSet.getString("Type");
                String start = Time.utcToLocalTime(resultSet.getObject("Start", LocalDateTime.class));
                String end = Time.utcToLocalTime(resultSet.getObject("End", LocalDateTime.class));
                String customerName = resultSet.getString("Customer_Name");
                int days = resultSet.getInt("Days");
                Appointment appointment = new Appointment(appId,title, description, location, contact, type, start, end, customerName, days);

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
}
