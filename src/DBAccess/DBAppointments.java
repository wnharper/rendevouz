package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE End BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY);";

            PreparedStatement pStatement = DBConnection.getDbConnection().prepareStatement(sqlQuery);

            ResultSet resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                int appId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String contact = resultSet.getString("Contact_Name");
                String type = resultSet.getString("Type");
                String start = resultSet.getString("Start");
                String end = resultSet.getString("End");
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
}
