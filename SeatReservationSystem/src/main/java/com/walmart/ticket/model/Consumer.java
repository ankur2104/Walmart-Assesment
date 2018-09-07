package com.walmart.ticket.model;

public class Consumer {
	private String ConsumerId;
	private String Name;
	private String email;
	
	public Consumer(String email) {
		super();
		this.email = email;
	}
	
	/**
	 * @return the consumerId
	 */
	public String getConsumerId() {
		return ConsumerId;
	}
	
	/**
	 * @param consumerId
	 */
	public void setConsumerId(String consumerId) {
		ConsumerId = consumerId;
	}
	
		/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (Name != null)
			builder.append(Name).append(", ");
		if (email != null)
			builder.append(email);
		return builder.toString();
	}
	
	
	
}
