/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jacob Carter - Implementation.
 *******************************************************************************/
package org.ossmeter.platform.bugtrackingsystem.redmine.api;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class RedmineConstants {

	// public static final DateTimeFormatter REQUEST_DATE_FORMATTER =
	// DateTimeFormat
	// .forPattern("YYYY-MM-dd");

	public static final int DEFAULT_PAGE_SIZE = 100;

	//public static final DateTimeFormatter DATE_FORMATTER = 
	
	public static final DateTimeFormatter getDateFormatter(boolean alternativeDateFormat) {
		if ( !alternativeDateFormat ) 
			return ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
		else
			return ISODateTimeFormat.date();
	}
	
	public static void main(String[] args) {
		
		System.out.println(RedmineConstants.getDateFormatter(false).print(new DateTime()));
		System.out.println(RedmineConstants.getDateFormatter(true).print(new DateTime()));
	}
	
}

