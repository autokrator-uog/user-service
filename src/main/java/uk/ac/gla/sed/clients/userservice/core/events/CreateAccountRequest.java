package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class CreateAccountRequest extends Event {
	private String requestId;
	
	public CreateAccountRequest(Event e) {
		super(e.getType(), Json.object().asObject().merge(e.getData()));

		if(!type.equals("AccountCreationRequest")) {
			throw new IllegalArgumentException("Error creating account request");
		}
		
		this.requestId = data.getString("RequestID", "");
	}

	public String getRequestId() {
		return this.requestId;
	}

}
