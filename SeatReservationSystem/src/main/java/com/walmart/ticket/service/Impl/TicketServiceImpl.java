package com.walmart.ticket.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import com.walmart.ticket.model.Consumer;
import com.walmart.ticket.model.STATUS;
import com.walmart.ticket.model.Seat;
import com.walmart.ticket.model.SeatHold;
import com.walmart.ticket.service.TicketService;

public class TicketServiceImpl implements TicketService{

	// this map will contain the list of seatHold for each status type.
	// this way shuffling between different status buckets will be easy.
	private Map<STATUS,List<SeatHold>> statusSeatsMap ;
	
	private long holdSeconds = 5000L;
	private static int nbrOfRows=0;
	
	private static int nbrOfSeatsPerRow=0;
	 
	final List<SeatHold> defaultSeatHold = new ArrayList<SeatHold>();
	

	public TicketServiceImpl(Map<STATUS, List<SeatHold>> seatsMap,int nbrRows,int nbrSeatsPerRow)
	{
		this.statusSeatsMap = seatsMap;
		nbrOfRows = nbrRows;
		nbrOfSeatsPerRow = nbrSeatsPerRow;
	}

	// this method will get the number of seats available from the map
	@Override
	public int numSeatsAvailable() {
		// TODO Auto-generated method stub
		//update any hold seats which has expired 
		updateExpiredHolds();
		if(!this.statusSeatsMap.get(STATUS.AVAILABLE).isEmpty())
		{	
			return this.statusSeatsMap.get(STATUS.AVAILABLE).get(0).getSeatLoc().size() ;
		}
		else
		{
			return 0;
		}
	}


	//check and update if the seat on hold has expired its hold period

	private void updateExpiredHolds() {
		long now ;
		long createdAt ;
		List<Integer> indexList =new ArrayList<Integer>();
		int index =0;
		now = Instant.now().getEpochSecond();

		synchronized(this)
		{
		for(SeatHold seatHold :this.statusSeatsMap.getOrDefault(STATUS.HOLD,defaultSeatHold))
		{
			if(seatHold!=null && seatHold.getCreatedAt()!=null)
			{	
			createdAt= seatHold.getCreatedAt().getEpochSecond();
			
			if((now-createdAt)*1000>=holdSeconds)
			{
				//remove from the hold list
				//this.statusSeatsMap.get(STATUS.HOLD).remove(index);
				indexList.add(index);

				//add to the available list
				List<SeatHold> seatAvailable =  this.statusSeatsMap.getOrDefault(STATUS.AVAILABLE,defaultSeatHold);

				for(SeatHold sa:seatAvailable)
				{
					List<Seat> existingSeats= sa.getSeatLoc();
					List<Seat> holdedSeats= seatHold.getSeatLoc();
					System.out.println(holdedSeats.size() +" seats are released from hold due to hold period expiry");
					// this is done to maintain the order of the seats after it is returned from hold state to available state.
					for(Seat seat:holdedSeats)
					{	
						int x = seat.getRow();
						int y = seat.getColumn();
						int ind = (x*nbrOfSeatsPerRow)+y ;
						existingSeats.add(ind,seat);
					}
				}	

			}
			}
			index++;

		}

		for(int i=0;i<indexList.size();i++)
		{
			int ind = indexList.get(i);
			SeatHold a = this.statusSeatsMap.get(STATUS.HOLD).remove(ind);
			
		}
		}

	}



	// Assumption: the lower number seat will be the best available seats,as those are near to the screen .
	// however the logic can be changed to find the seats in the same row if required.
	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub
		
		List<Seat> holdSeats = new ArrayList<Seat>();
		List<Seat> availableSeats = null;

		//it will also update any hold seats which has expired 
		
		int countSeatsA= numSeatsAvailable();
		// if seats available are less than required.
		if(  numSeats<=0){
			System.out.println("Not a valid number of seats");
			return new SeatHold(holdSeats) ;
		}
		
		if( countSeatsA < numSeats){
			System.out.println("Only "+ countSeatsA  +" seats are available for reservation, Hold request can not be completed");
			return new SeatHold(holdSeats) ;
		}
		else
		{
			Consumer cust = new Consumer(customerEmail);
			List<SeatHold> availableSeatList = Collections.synchronizedList(this.statusSeatsMap.get(STATUS.AVAILABLE));

			// for available seats we maintain only one List of SeatHold as it doesn't hold any information for customer,instant time etc.
			synchronized(this)
			{	
			for(SeatHold sa:availableSeatList)
			{
				availableSeats = sa.getSeatLoc();
				holdSeats = Collections.synchronizedList(new ArrayList<Seat>());
				for(int i=0;i<numSeats;i++)
				{
					// as the seats are removed list gets shorten by one.so remove (0).
					if(availableSeats.size()>0)
					{	
					Seat s = availableSeats.remove(0);
					holdSeats.add(s);
					}
					else
					{
						System.out.println("Hold Request for "+numSeats +" seats by "+customerEmail + " is NOT successful");
						return new SeatHold(holdSeats) ;
					}	
				}
				
				
			}

			SeatHold seatHold = new SeatHold(holdSeats);
			seatHold.setReservedBy(cust);
			seatHold.setCreatedAt(Instant.now());
			//update the map 
			
			List<SeatHold> exisSeatHold  = this.statusSeatsMap.getOrDefault(STATUS.HOLD, defaultSeatHold);
			exisSeatHold.add(seatHold);
			
			this.statusSeatsMap.put(STATUS.HOLD, exisSeatHold);
			
			System.out.println("Hold Request for "+numSeats +" seats by "+customerEmail + " is successful");
			System.out.println("Seats remaining " +numSeatsAvailable());
			return seatHold ;
		}
		}

	}



	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		// TODO Auto-generated method stub

		// get the list of seatsOn hold and find the one we are supposed to reserve
		List<SeatHold> seatsOnHold= statusSeatsMap.get(STATUS.HOLD);
		int index =0;
		synchronized(this)
		{	
		for(SeatHold sh:seatsOnHold)
		{
			if(sh!=null && sh.getId()==seatHoldId && sh.getReservedBy()!= null && sh.getReservedBy().getEmail().equals(customerEmail))
			{
				SeatHold seatRemovedfrmHold =	seatsOnHold.remove(index);
				seatRemovedfrmHold.setCreatedAt(Instant.now());

				List<SeatHold> seatReserved =statusSeatsMap.get(STATUS.RESERVED) ;

				if(seatReserved==null || seatReserved.isEmpty())
				{
					seatReserved = Collections.synchronizedList(new ArrayList<SeatHold>()) ;
				}

				seatReserved.add(seatRemovedfrmHold);
				statusSeatsMap.put(STATUS.RESERVED, seatReserved);
				statusSeatsMap.put(STATUS.HOLD, seatsOnHold);
				System.out.println("Reservation Request for the customer "+customerEmail+" is succesful");
				return reservationCode(seatRemovedfrmHold);
			}
			index++;
		}
		}
		// in case the the reservation ID is not found 
		System.out.println("Either SeatHold has expired or the email ID/Reservation ID is incorrect ");
		return "Either SeatHold has expired or the email ID/Reservation ID is incorrect";
	}


	private static String reservationCode(SeatHold seatReserved){
		StringBuilder sb = new StringBuilder();
		sb.append("Congrats! Your seats have been reserved!\n");
		sb.append("Details:\n");
		sb.append("Confirmation no: " + UUID.randomUUID().toString() + "\n");
		sb.append("seats: [ ");
		for(Seat st: seatReserved.getSeatLoc()){
			sb.append(st.getRow()*nbrOfSeatsPerRow+st.getColumn()); sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static int getNbrOfRows() {
		return nbrOfRows;
	}

	public static void setNbrOfRows(int nbrOfRows) {
		TicketServiceImpl.nbrOfRows = nbrOfRows;
	}

	public static int getNbrOfSeatsPerRow() {
		return nbrOfSeatsPerRow;
	}

	public static void setNbrOfSeatsPerRow(int nbrOfSeatsPerRow) {
		TicketServiceImpl.nbrOfSeatsPerRow = nbrOfSeatsPerRow;
	}


}



