# SeatReservationSystem


# Maven Commands:

* From the Root directory (where POM.xml exist) exceute the following command to compile the classes, run the test cases and package the jar as SeatReservation-1.0.0.jar(without libs) 

* mvn clean install 


# Implementation:
Following data model/POJO's has been implemented
* Consumer: Contains consumer information like Name,emailId,ConsumerId.
* Seat: Contains Seat co-ordinates in a matrix (like 0th row,4th seat) 
* SeatHold: Contains list of seats and reserved/hold information and the time it is put on hold
* A map is maintained for the seats in 3 different states (Available,Hold,Reserved)
 

# Note:
* The input for the number of seats (rows and columns ) is maintained in externalize property files. It loads at the start of the program.
* Hold time is hard coded in TicketServiceImpl.java as 5000 ms.
* The application is executed/tested through Test cases only . No web/Application server is added.
* One customer can request any number of holds.There is no validation to avoid that.
* The system doesn’t include any security features like authentication or authorization.
* Used System.out.println statements in this project. Ideally we should use some logging framework(like log4j) to better manage the logging.
 

# Assumptions
* Further test/enhancements might be required for synchronization issues as multiple user acting on same set of seats. TicketServiceImplMultiThreadedTest is added for this purpose.  
* It is assumed that lower numbered seats are nearest to screen and are the best seats available from the customer perspective.However the logic can be enhanced to include more business rules for determining the best available seats.   
* The current system design might not work in the distributed systems.That will require use of distributed/centralized caching or persistence layer.
* System doesn't allow to change the number of seats while reserving. All seats which are on hold are reserved.
* The data is not persisted anywhere.

 

 
