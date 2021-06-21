*************************************************************************
*				      					                                                *
*				      					                                                *
*               	            RENDEZVOUS             			              *
*				     					                                                  *
*				     				                                	                  *
*************************************************************************

INFORMATION
Rendezvous is an application designed to create, store and edit individual 
Customer information. The user is then able to book appointments with 
stored customers.

@Author - Warren Harper				  
@Date - April 21, 2021

@IDE - IntelliJ IDEA Ultimate 2021.1
@Java - JDK 11.0.2
@JavaFX - SDK-16
@MySQL Driver - 8.0.22

NOTE: The customer ID and Appointment ID are purposefully hidden from the
user as they are not editable by the user and to keep the UI clean.

- HOW TO USE THE PROGRAM
Upon executing the program, a login form will present itself. Login using
the credentials username: test password: test

After successful login, you'll be taken to the appointments screen where 
you will see a table of appointments sorted by start time. You will be 
notified if there is an appointment starting in the next 15 minutes.

The default filter is 'This week' which will show appointments in the 
current week. You can change this filter using the radio buttons near the
top. On this screen you can create, edit or delete appointments. You can
view more appointment information (columns) by clicking '+' sign in the table.

The purple navigation bar down the left hand side will allow you to navigate
to the customers screen where you can view, create, edit or delete customers. 
lastly, if you navigate to the reports screen, you will be able to view 
various reports that are relevant to appointments.

- ADDITIONAL REPORT
In the top left of the reports screen, you will find a report that shows the
average length (in minutes) of appointments by location.



