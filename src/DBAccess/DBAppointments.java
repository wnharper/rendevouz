package DBAccess;

import Database.DBConnection;
import GUI.Login;
import Model.Appointment;
import Utilities.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <h2>Data Base Appointments</h2>
 * DBAppointments provides methods in order to retrieve, insert, update and remove data from the database
 * related to appointments
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class DBAppointments {

    /**
     * Method returns a list of all appointments in the database
     *
     * @return Observable List
     */
    public static ObservableList<Appointment> getAllAppointments() {
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
                Appointment appointment = new Appointment(appId, title, description, location, contact, contactId, type, start, end, customerName, customerId, days);

                appointmentsList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsList;
    }

    /**
     * Method takes appointment attributes and inserts them into appointments table in database
     *
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
     *
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
                    + location + "', Type = '" + type + "', Start = '" + start + "', End = '" + end + "', Last_Updated_By = '" + Login.currentUser.getUserId() + "' , Customer_ID = "
                    + customerId + ", User_ID = " + userId + ", Contact_ID = " + contactId + " WHERE Appointment_ID = " + appointmentId + ";";

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
     *
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
     * Method deletes appointment in database according to appointment ID
     *
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
     * Method returns a list of all appointments occurring in the next month
     *
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

    /**
     * Method returns Chart list of data from Database containing number of appointments
     * categorized by month
     *
     * @return Appointments by month count
     */
    public static ArrayList<XYChart.Series> appointmentCountData() {

        // Initialize chart
        ArrayList<XYChart.Series> months = new ArrayList<>();

        try {
            String sqlQuery = "SELECT Type, \n" +
                    "\tSUM(month(start) = 1) AS 'Jan', \n" +
                    "    SUM(month(start) = 2) AS 'Feb',\n" +
                    "    SUM(month(start) = 3) AS 'Mar',\n" +
                    "    SUM(month(start) = 4) AS 'Apr',\n" +
                    "    SUM(month(start) = 5) AS 'May',\n" +
                    "    SUM(month(start) = 6) AS 'Jun',\n" +
                    "    SUM(month(start) = 7) AS 'Jul',\n" +
                    "    SUM(month(start) = 8) AS 'Aug',\n" +
                    "    SUM(month(start) = 9) AS 'Sep',\n" +
                    "    SUM(month(start) = 10) AS 'Oct',\n" +
                    "    SUM(month(start) = 11) AS 'Nov',\n" +
                    "    SUM(month(start) = 12) AS 'Dec'\n" +
                    "    from appointments group by type;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {

                String type = resultSet.getString("Type");
                int jan = resultSet.getInt("Jan");
                int feb = resultSet.getInt("Feb");
                int mar = resultSet.getInt("Mar");
                int apr = resultSet.getInt("Apr");
                int may = resultSet.getInt("May");
                int jun = resultSet.getInt("Jun");
                int jul = resultSet.getInt("Jul");
                int aug = resultSet.getInt("Aug");
                int sep = resultSet.getInt("Sep");
                int oct = resultSet.getInt("Oct");
                int nov = resultSet.getInt("Nov");
                int dec = resultSet.getInt("Dec");

                XYChart.Series chartData = new XYChart.Series();
                chartData.setName(type);
                chartData.getData().add(new XYChart.Data("Jan", jan));
                chartData.getData().add(new XYChart.Data("Feb", feb));
                chartData.getData().add(new XYChart.Data("Mar", mar));
                chartData.getData().add(new XYChart.Data("Apr", apr));
                chartData.getData().add(new XYChart.Data("May", may));
                chartData.getData().add(new XYChart.Data("Jun", jun));
                chartData.getData().add(new XYChart.Data("Jul", jul));
                chartData.getData().add(new XYChart.Data("Aug", aug));
                chartData.getData().add(new XYChart.Data("Sep", sep));
                chartData.getData().add(new XYChart.Data("Oct", oct));
                chartData.getData().add(new XYChart.Data("Nov", nov));
                chartData.getData().add(new XYChart.Data("Dec", dec));


                months.add(chartData);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return months;
    }

    /**
     * Method returns Chart list of data from Database containing number of appointments
     * categorized by type
     *
     * @return Appointment count by type
     */
    public static XYChart.Series appointmentTypeData() {

        // Initialize chart
        XYChart.Series chartData = new XYChart.Series();
        try {
            String sqlQuery = "SELECT Type, COUNT(*) AS 'Count' FROM appointments GROUP BY Type;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {

                String type = resultSet.getString("Type");
                int count = resultSet.getInt("Count");
                chartData.getData().add(new XYChart.Data(type, count));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return chartData;
    }

    /**
     * Method returns Chart list of data from Database containing average length of appointment
     * in minutes grouped my location
     *
     * @return Appointment count by type
     */
    public static XYChart.Series appointmentLengthData() {

        // Initialize chart
        XYChart.Series chartData = new XYChart.Series();
        try {
            String sqlQuery = "SELECT Location, AVG(timestampdiff(MINUTE, Start, End)) AS 'Average_time' FROM appointments GROUP BY Location;";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {

                String location = resultSet.getString("Location");
                int time = resultSet.getInt("Average_time");
                chartData.getData().add(new XYChart.Data(location, time));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return chartData;
    }
}
