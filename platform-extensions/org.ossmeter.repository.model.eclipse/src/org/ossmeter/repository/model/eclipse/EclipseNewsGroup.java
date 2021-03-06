/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Davide Di Ruscio - Implementation,
 *    Juri Di Rocco - Implementation.
 *******************************************************************************/
package org.ossmeter.repository.model.eclipse;

import com.googlecode.pongo.runtime.querying.StringQueryProducer;


public class EclipseNewsGroup extends org.ossmeter.repository.model.cc.nntp.NntpNewsGroup {
	
	
	
	public EclipseNewsGroup() { 
		super();
		super.setSuperTypes("org.ossmeter.repository.model.eclipse.NntpNewsGroup");
		TYPE.setOwningType("org.ossmeter.repository.model.eclipse.EclipseNewsGroup");
	}
	
	public static StringQueryProducer TYPE = new StringQueryProducer("type"); 
	
	
	public String getType() {
		return parseString(dbObject.get("type")+"", "");
	}
	
	public EclipseNewsGroup setType(String type) {
		dbObject.put("type", type);
		notifyChanged();
		return this;
	}
	
	
	
	
}