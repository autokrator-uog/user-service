package uk.ac.gla.sed.clients.userservice.core.events;

import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class CreateAccountRequest extends Event {
	private String username;
	private String id;
	
	public CreateAccountRequest(Event e) {
		super(e.getType(), Json.object().asObject().merge(e.getData()));
        
        this.username = data.getString("Username","");	

	}

	public String getAccountId() {
		return this.id;
	}

}
