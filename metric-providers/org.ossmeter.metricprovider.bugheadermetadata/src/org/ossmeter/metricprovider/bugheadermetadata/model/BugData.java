package org.ossmeter.metricprovider.bugheadermetadata.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class BugData extends Pongo {
	
	
	
	public BugData() { 
		super();
		URL.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		PRODUCT.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		COMPONENT.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		BUGID.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		STATUS.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		RESOLUTION.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		OPERATINGSYSTEM.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
		PRIORITY.setOwningType("org.ossmeter.metricprovider.bugheadermetadata.model.BugData");
	}
	
	public static StringQueryProducer URL = new StringQueryProducer("url"); 
	public static StringQueryProducer PRODUCT = new StringQueryProducer("product"); 
	public static StringQueryProducer COMPONENT = new StringQueryProducer("component"); 
	public static StringQueryProducer BUGID = new StringQueryProducer("bugId"); 
	public static StringQueryProducer STATUS = new StringQueryProducer("status"); 
	public static StringQueryProducer RESOLUTION = new StringQueryProducer("resolution"); 
	public static StringQueryProducer OPERATINGSYSTEM = new StringQueryProducer("operatingSystem"); 
	public static StringQueryProducer PRIORITY = new StringQueryProducer("priority"); 
	
	
	public String getUrl() {
		return parseString(dbObject.get("url")+"", "");
	}
	
	public BugData setUrl(String url) {
		dbObject.put("url", url);
		notifyChanged();
		return this;
	}
	public String getProduct() {
		return parseString(dbObject.get("product")+"", "");
	}
	
	public BugData setProduct(String product) {
		dbObject.put("product", product);
		notifyChanged();
		return this;
	}
	public String getComponent() {
		return parseString(dbObject.get("component")+"", "");
	}
	
	public BugData setComponent(String component) {
		dbObject.put("component", component);
		notifyChanged();
		return this;
	}
	public String getBugId() {
		return parseString(dbObject.get("bugId")+"", "");
	}
	
	public BugData setBugId(String bugId) {
		dbObject.put("bugId", bugId);
		notifyChanged();
		return this;
	}
	public String getStatus() {
		return parseString(dbObject.get("status")+"", "");
	}
	
	public BugData setStatus(String status) {
		dbObject.put("status", status);
		notifyChanged();
		return this;
	}
	public String getResolution() {
		return parseString(dbObject.get("resolution")+"", "");
	}
	
	public BugData setResolution(String resolution) {
		dbObject.put("resolution", resolution);
		notifyChanged();
		return this;
	}
	public String getOperatingSystem() {
		return parseString(dbObject.get("operatingSystem")+"", "");
	}
	
	public BugData setOperatingSystem(String operatingSystem) {
		dbObject.put("operatingSystem", operatingSystem);
		notifyChanged();
		return this;
	}
	public String getPriority() {
		return parseString(dbObject.get("priority")+"", "");
	}
	
	public BugData setPriority(String priority) {
		dbObject.put("priority", priority);
		notifyChanged();
		return this;
	}
	
	
	
	
}