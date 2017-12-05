package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;

import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AccountCreated extends Event {
	private final String username;
	private final int accountId;
	
	public AccountCreated(Event e) {
		super(e.getType(), Json.object().asObject().merge(e.getData()));
	
		if(!type.equals("AccountCreated")) {
			throw new IllegalArgumentException("Error creating account");
		}
		
		this.accountId = data.getInt("AccountID", -1);
		this.username = data.getString("Username", "");
	}
	
	public int getAccountId() {
		return this.accountId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
}
