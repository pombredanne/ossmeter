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
package org.ossmeter.platform.bugtrackingsystem.bitbucket.api;

public class Bitbucket {
	public static final String PRIORITY_TRIVIAL = "trivial";
	public static final String PRIORITY_MINOR = "minor";
	public static final String PRIORITY_MAJOR = "major";
	public static final String PRIORITY_CRITICAL = "critical";
	public static final String PRIORITY_BLOCKER = "blocker";
	
	public static final String PRIORITY_NOT_TRIVIAL = "!trivial";
	public static final String PRIORITY_NOT_MINOR = "!minor";
	public static final String PRIORITY_NOT_MAJOR = "!major";
	public static final String PRIORITY_NOT_CRITICAL = "!critical";
	public static final String PRIORITY_NOT_BLOCKER = "!blocker";
	
	public static final String STATUS_NEW = "new";
	public static final String STATUS_OPEN = "open";
	public static final String STATUS_RESOLVED = "resolved";
	public static final String STATUS_ON_HOLD = "on hold";
	public static final String STATUS_INVALID = "invalid";
	public static final String STATUS_DUPLICATE = "duplicate";
	public static final String STATUS_WONTFIX = "wontfix";
	
	public static final String STATUS_NOT_NEW = "!new";
	public static final String STATUS_NOT_OPEN = "!open";
	public static final String STATUS_NOT_RESOLVED = "!resolved";
	public static final String STATUS_NOT_ON_HOLD = "!on hold";
	public static final String STATUS_NOT_INVALID = "!invalid";
	public static final String STATUS_NOT_DUPLICATE = "!duplicate";
	public static final String STATUS_NOT_WONTFIX = "!wontfix";
}
