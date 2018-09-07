package com.walmart.ticket.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.walmart.ticket.TicketReservationMain;
import com.walmart.ticket.model.STATUS;
import com.walmart.ticket.model.SeatHold;
import com.walmart.ticket.service.Impl.TicketServiceImpl;


@RunWith(ConcurrentTestRunner.class)
public class TicketServiceImplMultiThreadedTest {
	
	private TicketServiceImpl service;
	private int holdTime = 6000;
	
	

	@Before
	public void setUp() throws Exception {
		Map<STATUS,List<SeatHold>> seatsMap = TicketReservationMain.initializeSeats();
		
		service = new TicketServiceImpl(seatsMap, TicketReservationMain.nbrOfRows,TicketReservationMain.nbrOfSeatsPerRow);
	}

	
	
	@Test
	@ThreadCount(2)
	public void reserveSeats() throws InterruptedException{
		
		int numberOfSeats = service.numSeatsAvailable();
		assert(numberOfSeats == 50);
		
		// test for number of rows
		assert(TicketServiceImpl.getNbrOfRows() ==10);

		// test for number of columns
		assert(TicketServiceImpl.getNbrOfSeatsPerRow() ==5);

		
		// valid reservation case
		SeatHold s1 = service.findAndHoldSeats(30, "test@google.com");
		assertNotNull(s1);
		
		String response = service.reserveSeats(s1.getId(), "test@google.com");
		assertNotNull(response);
	//	assertTrue(response.contains("Congrats! Your seats have been reserved!"));
		
			// invalid ID
	/*	response = service.reserveSeats(-1, "test@google.com");
		assertNotNull(response);
		assertTrue(response.contains("SeatHold has expired or the email ID/Reservation ID is incorrect"));
		
		
		// Invalid emailID 
		 s1 = service.findAndHoldSeats(5, "test@google.com");
		response = service.reserveSeats(s1.getId(), "XYZ@google.com");
		assertNotNull(response);
		assertTrue(response.contains("SeatHold has expired or the email ID/Reservation ID is incorrect"));
		
		s1 = service.findAndHoldSeats(10, "test@google.com");
		// sleep for time more than hold time
		Thread.sleep(holdTime);
		response = service.reserveSeats(s1.getId(), "xyz@abc.com");
		assertNotNull(response);
		assertTrue(response.contains("SeatHold has expired or the email ID/Reservation ID is incorrect"));
		*/
		
		
	}
	
	
	
	
}
