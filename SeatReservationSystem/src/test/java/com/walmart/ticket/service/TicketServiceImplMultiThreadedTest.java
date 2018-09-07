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
	@ThreadCount(10)
	public void reserveSeats() throws InterruptedException{
		
		String response = null;
		int numberOfSeats = service.numSeatsAvailable();
		assertTrue(numberOfSeats == 50 || numberOfSeats == 44 || numberOfSeats == 38 ||  numberOfSeats == 32 || numberOfSeats == 26 || numberOfSeats == 20 || numberOfSeats == 14 || numberOfSeats == 8 || numberOfSeats == 2);
		
		
		// test for number of rows
		assert(TicketServiceImpl.getNbrOfRows() ==10);

		// test for number of columns
		assert(TicketServiceImpl.getNbrOfSeatsPerRow() ==5);

		
		// valid reservation case
		SeatHold s1 = service.findAndHoldSeats(6, Thread.currentThread().getName()+"@xyz.com");
		assertNotNull(s1);
		
		if(s1!=null && s1.getReservedBy()!=null && s1.getReservedBy().getEmail().equals(Thread.currentThread().getName()+"@xyz.com") && s1.getSeatLoc().size()>0)
		{	
		 response = service.reserveSeats(s1.getId(), Thread.currentThread().getName()+"@xyz.com");
		assertNotNull(response);
		assertTrue(response.contains("Congrats! Your seats have been reserved!"));
		}
		
		
		
		
		
		
	}
	
	
	
	
}
