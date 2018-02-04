package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;

import uk.ac.gla.sed.shared.eventbusclient.api.Consistency;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AccountCreated extends Event {
	private final String requestId;
	private final Integer accountId;
	
	public AccountCreated(Event e) {
		super(e.getType(), Json.object().asObject().merge(e.getData()), e.getConsistency());
	
		if(!type.equals("AccountCreated")) {
			throw new IllegalArgumentException("Error creating account");
		}

		this.accountId = data.getInt("AccountID", -1);
		this.requestId = data.getString("RequestID", "");
	}
	
	public int getAccountId() {
		return this.accountId;
	}
	
	public String getRequestId() {
		return this.requestId;
	}
}
