package org.ossmeter.metricprovider.historic.newsgroups.users.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class NewsgroupsUsersHistoricMetric extends Pongo {
	
	protected List<DailyNewsgroupData> newsgroups = null;
	
	
	public NewsgroupsUsersHistoricMetric() { 
		super();
		dbObject.put("newsgroups", new BasicDBList());
		NUMBEROFUSERS.setOwningType("org.ossmeter.metricprovider.historic.newsgroups.users.model.NewsgroupsUsersHistoricMetric");
		NUMBEROFACTIVEUSERS.setOwningType("org.ossmeter.metricprovider.historic.newsgroups.users.model.NewsgroupsUsersHistoricMetric");
		NUMBEROFINACTIVEUSERS.setOwningType("org.ossmeter.metricprovider.historic.newsgroups.users.model.NewsgroupsUsersHistoricMetric");
	}
	
	public static NumericalQueryProducer NUMBEROFUSERS = new NumericalQueryProducer("numberOfUsers");
	public static NumericalQueryProducer NUMBEROFACTIVEUSERS = new NumericalQueryProducer("numberOfActiveUsers");
	public static NumericalQueryProducer NUMBEROFINACTIVEUSERS = new NumericalQueryProducer("numberOfInactiveUsers");
	
	
	public int getNumberOfUsers() {
		return parseInteger(dbObject.get("numberOfUsers")+"", 0);
	}
	
	public NewsgroupsUsersHistoricMetric setNumberOfUsers(int numberOfUsers) {
		dbObject.put("numberOfUsers", numberOfUsers);
		notifyChanged();
		return this;
	}
	public int getNumberOfActiveUsers() {
		return parseInteger(dbObject.get("numberOfActiveUsers")+"", 0);
	}
	
	public NewsgroupsUsersHistoricMetric setNumberOfActiveUsers(int numberOfActiveUsers) {
		dbObject.put("numberOfActiveUsers", numberOfActiveUsers);
		notifyChanged();
		return this;
	}
	public int getNumberOfInactiveUsers() {
		return parseInteger(dbObject.get("numberOfInactiveUsers")+"", 0);
	}
	
	public NewsgroupsUsersHistoricMetric setNumberOfInactiveUsers(int numberOfInactiveUsers) {
		dbObject.put("numberOfInactiveUsers", numberOfInactiveUsers);
		notifyChanged();
		return this;
	}
	
	
	public List<DailyNewsgroupData> getNewsgroups() {
		if (newsgroups == null) {
			newsgroups = new PongoList<DailyNewsgroupData>(this, "newsgroups", true);
		}
		return newsgroups;
	}
	
	
}