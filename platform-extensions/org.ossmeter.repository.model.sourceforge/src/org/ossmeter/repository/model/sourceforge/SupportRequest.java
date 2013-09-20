package org.ossmeter.repository.model.sourceforge;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class SupportRequest extends Request {
	
	
	
	public SupportRequest() { 
		super();
		super.setSuperTypes("org.ossmeter.repository.model.sourceforge.Request","org.ossmeter.repository.model.sourceforge.Tracker","org.ossmeter.repository.model.sourceforge.NamedElement");
		LOCATION.setOwningType("org.ossmeter.repository.model.sourceforge.SupportRequest");
		STATUS.setOwningType("org.ossmeter.repository.model.sourceforge.SupportRequest");
		SUMMARY.setOwningType("org.ossmeter.repository.model.sourceforge.SupportRequest");
		CREATED_AT.setOwningType("org.ossmeter.repository.model.sourceforge.SupportRequest");
		UPDATED_AT.setOwningType("org.ossmeter.repository.model.sourceforge.SupportRequest");
	}
	
	public static StringQueryProducer LOCATION = new StringQueryProducer("location"); 
	public static StringQueryProducer STATUS = new StringQueryProducer("status"); 
	public static StringQueryProducer SUMMARY = new StringQueryProducer("summary"); 
	public static StringQueryProducer CREATED_AT = new StringQueryProducer("created_at"); 
	public static StringQueryProducer UPDATED_AT = new StringQueryProducer("updated_at"); 
	
	
	
	
	
	
}