package model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class InvitationRequest extends Pongo {
	
	
	
	public InvitationRequest() { 
		super();
		EMAIL.setOwningType("model.InvitationRequest");
		TOKEN.setOwningType("model.InvitationRequest");
		STATUS.setOwningType("model.InvitationRequest");
	}
	
	public static StringQueryProducer EMAIL = new StringQueryProducer("email"); 
	public static StringQueryProducer TOKEN = new StringQueryProducer("token"); 
	public static StringQueryProducer STATUS = new StringQueryProducer("status"); 
	
	
	public String getEmail() {
		return parseString(dbObject.get("email")+"", "");
	}
	
	public InvitationRequest setEmail(String email) {
		dbObject.put("email", email);
		notifyChanged();
		return this;
	}
	public String getToken() {
		return parseString(dbObject.get("token")+"", "");
	}
	
	public InvitationRequest setToken(String token) {
		dbObject.put("token", token);
		notifyChanged();
		return this;
	}
	public String getStatus() {
		return parseString(dbObject.get("status")+"", "");
	}
	
	public InvitationRequest setStatus(String status) {
		dbObject.put("status", status);
		notifyChanged();
		return this;
	}
	
	
	
	
}