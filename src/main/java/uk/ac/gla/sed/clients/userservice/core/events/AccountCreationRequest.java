package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AccountCreationRequest extends Event {
	private String requestId;
	
	public AccountCreationRequest(String requestId) {
		super("AccountCreationRequest", Json.object().asObject()
			.set("RequestID", requestId)
		);
		this.requestId = requestId;
	}

	public String getRequestId() {
		return this.requestId;
	}

}
