package com.walmart.ticket.model;

import java.time.Instant;
import java.util.List;

public class SeatHold {
	private static int counter;
	private List<Seat> seats;
	private Consumer reservedBy;
	private Instant createdAt;
	private  Integer Id;
	
	static {
		counter = 0;
	}
	
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public SeatHold(List<Seat> seats) {
		super();
		this.Id = ++counter;
		this.seats = seats;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	

	public List<Seat> getSeatLoc() {
		return seats;
	}
	
	public void setSeatLoc(List<Seat> seatLoc) {
		this.seats = seatLoc;
	}
	
	public Consumer getReservedBy() {
		return reservedBy;
	}
	
	public void setReservedBy(Consumer reservedBy) {
		this.reservedBy = reservedBy;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Seat<");
		if (seats != null)
			builder.append(seats).append(", ");
		if (reservedBy != null)
			builder.append("reservedBy=").append(reservedBy).append(", ");
		builder.append(">");
		return builder.toString();
	}
	
	
}
