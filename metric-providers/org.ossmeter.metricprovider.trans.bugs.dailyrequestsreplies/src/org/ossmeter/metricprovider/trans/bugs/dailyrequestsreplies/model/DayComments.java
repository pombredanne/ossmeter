/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yannis Korkontzelos - Implementation.
 *******************************************************************************/
package org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model;

import com.googlecode.pongo.runtime.Pongo;
import com.googlecode.pongo.runtime.querying.NumericalQueryProducer;
import com.googlecode.pongo.runtime.querying.StringQueryProducer;


public class DayComments extends Pongo {
	
	
	
	public DayComments() { 
		super();
		NAME.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		BUGTRACKERID.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		NUMBEROFCOMMENTS.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		NUMBEROFREQUESTS.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		NUMBEROFREPLIES.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		PERCENTAGEOFCOMMENTS.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		PERCENTAGEOFREQUESTS.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
		PERCENTAGEOFREPLIES.setOwningType("org.ossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments");
	}
	
	public static StringQueryProducer NAME = new StringQueryProducer("name"); 
	public static StringQueryProducer BUGTRACKERID = new StringQueryProducer("bugTrackerId"); 
	public static NumericalQueryProducer NUMBEROFCOMMENTS = new NumericalQueryProducer("numberOfComments");
	public static NumericalQueryProducer NUMBEROFREQUESTS = new NumericalQueryProducer("numberOfRequests");
	public static NumericalQueryProducer NUMBEROFREPLIES = new NumericalQueryProducer("numberOfReplies");
	public static NumericalQueryProducer PERCENTAGEOFCOMMENTS = new NumericalQueryProducer("percentageOfComments");
	public static NumericalQueryProducer PERCENTAGEOFREQUESTS = new NumericalQueryProducer("percentageOfRequests");
	public static NumericalQueryProducer PERCENTAGEOFREPLIES = new NumericalQueryProducer("percentageOfReplies");
	
	
	public String getName() {
		return parseString(dbObject.get("name")+"", "");
	}
	
	public DayComments setName(String name) {
		dbObject.put("name", name);
		notifyChanged();
		return this;
	}
	public String getBugTrackerId() {
		return parseString(dbObject.get("bugTrackerId")+"", "");
	}
	
	public DayComments setBugTrackerId(String bugTrackerId) {
		dbObject.put("bugTrackerId", bugTrackerId);
		notifyChanged();
		return this;
	}
	public int getNumberOfComments() {
		return parseInteger(dbObject.get("numberOfComments")+"", 0);
	}
	
	public DayComments setNumberOfComments(int numberOfComments) {
		dbObject.put("numberOfComments", numberOfComments);
		notifyChanged();
		return this;
	}
	public int getNumberOfRequests() {
		return parseInteger(dbObject.get("numberOfRequests")+"", 0);
	}
	
	public DayComments setNumberOfRequests(int numberOfRequests) {
		dbObject.put("numberOfRequests", numberOfRequests);
		notifyChanged();
		return this;
	}
	public int getNumberOfReplies() {
		return parseInteger(dbObject.get("numberOfReplies")+"", 0);
	}
	
	public DayComments setNumberOfReplies(int numberOfReplies) {
		dbObject.put("numberOfReplies", numberOfReplies);
		notifyChanged();
		return this;
	}
	public float getPercentageOfComments() {
		return parseFloat(dbObject.get("percentageOfComments")+"", 0.0f);
	}
	
	public DayComments setPercentageOfComments(float percentageOfComments) {
		dbObject.put("percentageOfComments", percentageOfComments);
		notifyChanged();
		return this;
	}
	public float getPercentageOfRequests() {
		return parseFloat(dbObject.get("percentageOfRequests")+"", 0.0f);
	}
	
	public DayComments setPercentageOfRequests(float percentageOfRequests) {
		dbObject.put("percentageOfRequests", percentageOfRequests);
		notifyChanged();
		return this;
	}
	public float getPercentageOfReplies() {
		return parseFloat(dbObject.get("percentageOfReplies")+"", 0.0f);
	}
	
	public DayComments setPercentageOfReplies(float percentageOfReplies) {
		dbObject.put("percentageOfReplies", percentageOfReplies);
		notifyChanged();
		return this;
	}
	
	
	
	
}