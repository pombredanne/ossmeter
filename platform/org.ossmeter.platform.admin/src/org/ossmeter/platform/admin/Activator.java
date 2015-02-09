/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    James Williams - Implementation.
 *******************************************************************************/
package org.ossmeter.platform.admin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.ossmeter.platform.osgi.services.ApiStartServiceToken;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator{

	// The plug-in ID
	public static final String PLUGIN_ID = "org.ossmeter.platform.admin"; //$NON-NLS-1$

	private Component component;
	
	public void start(BundleContext context) throws Exception {
		
		context.addServiceListener(new ServiceListener() {
			
			@Override
			public void serviceChanged(ServiceEvent event) {
				if (event.getType() == ServiceEvent.REGISTERED){
					component = new Component();
					component.getServers().add(Protocol.HTTP, 8183);
					component.getClients().add(Protocol.FILE);
					
					Application application = new AdminApplication();
					
					component.getDefaultHost().attachDefault(application);
					try {
						component.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "(objectclass=" + ApiStartServiceToken.class.getName() +")");
	}

	public void stop(BundleContext context) throws Exception {
		component.stop();
	}

}
