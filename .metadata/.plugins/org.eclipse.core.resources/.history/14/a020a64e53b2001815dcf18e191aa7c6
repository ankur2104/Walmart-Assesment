# TicketService
Book tickets.

# Maven Commands:
Assuming current directory is at this repository (where pom.xml is present).
1. mvn clean
2. mvn clean package assembly:assembly OR mvn clean package assembly:assembly -DskipTests (To skip the test cases) // This will generate jar(*-jar-with-dependenciees.jar) file under target folder.
3. java -jar target\TicketService-0.0.1-jar-with-dependencies.jar com.walmart.ticket.Start // Program will start. Then follow the command prompt.


# Implementation:
Following data model has been implemented
* Venue: acts like a asset (ie theatre) where it has matrix of seats. each elemenet of matrix represents Seat object. 
* Seat: contains number(in x,y co-ordinates), status (using enum), reservedBy(Customer type)
* Loc: for indexing seat number. i.e. 0th row, 5th seat. 
* Customer: to store customer related information i.e. email.
* SeatHold: works as a separate timed based object where it has user requested information (#seats, email), and where event happened (timestamp). This object depends on the request for hold.

# Service Implementation:
* TicketServiceImpl: implements 3 services plus supporting methods. Basically, it has Map to save created SeatHolds (just because findAndHoldRequest(..) takes id instead of SeatHold), venue object to get access of seats. It also takes care that Seat is created only one time (referencing with single thread application).

# Note: 
* Test cases are certainly not enough. I would take care of that for sure.
* Right now, this finds available seats based on linearly. But I would enhance that as well. I meant, if user requests n no of seats and if second last row has that in one row, then i would like to take them compare to taking couple of seats from last row and remaining from second last.
