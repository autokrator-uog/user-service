package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;

import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AccountCreated extends Event {
	private String accountId;
	
	public AccountCreated(CreateAccountRequest request) {
		this.accountId = request.getAccountId();
		this.type = "AccountCreated";
		this.data = Json.object().asObject();
		this.data.set("AccountID", this.accountId);
	}
	
	public String getAccountId() {
		return this.accountId;
	}
	
}
