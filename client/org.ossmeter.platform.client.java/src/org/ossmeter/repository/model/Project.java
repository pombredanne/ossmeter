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
package org.ossmeter.repository.model;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY,
	property = "_type")
@JsonSubTypes({
	@Type(value = Project.class, name="org.ossmeter.repository.model.Project"), 	@Type(value = org.ossmeter.repository.model.eclipse.EclipseProject.class, name="org.ossmeter.repository.model.eclipse.EclipseProject"),
	@Type(value = org.ossmeter.repository.model.github.GitHubRepository.class, name="org.ossmeter.repository.model.github.GitHubRepository"),
	@Type(value = org.ossmeter.repository.model.googlecode.GoogleCodeProject.class, name="org.ossmeter.repository.model.googlecode.GoogleCodeProject"),
	@Type(value = org.ossmeter.repository.model.redmine.RedmineProject.class, name="org.ossmeter.repository.model.redmine.RedmineProject"),
	@Type(value = org.ossmeter.repository.model.sourceforge.SourceForgeProject.class, name="org.ossmeter.repository.model.sourceforge.SourceForgeProject"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends NamedElement {

	protected List<VcsRepository> vcsRepositories;
	protected List<CommunicationChannel> communicationChannels;
	protected List<BugTrackingSystem> bugTrackingSystems;
	protected List<Person> persons;
	protected List<License> licenses;
	protected List<MetricProvider> metricProviderData;
	protected List<Company> companies;
	protected String shortName;
	protected String description;
	protected int year;
	protected boolean active;
	protected String lastExecuted;
	protected Project parent;
	protected String homePage;
	
	public String getShortName() {
		return shortName;
	}
	public String getDescription() {
		return description;
	}
	public int getYear() {
		return year;
	}
	public boolean getActive() {
		return active;
	}
	public String getLastExecuted() {
		return lastExecuted;
	}
	public String getHomePage() {
		return homePage;
	}
	
	public List<VcsRepository> getVcsRepositories() {
		return vcsRepositories;
	}
	public List<CommunicationChannel> getCommunicationChannels() {
		return communicationChannels;
	}
	public List<BugTrackingSystem> getBugTrackingSystems() {
		return bugTrackingSystems;
	}
	public List<Person> getPersons() {
		return persons;
	}
	public List<License> getLicenses() {
		return licenses;
	}
	public List<MetricProvider> getMetricProviderData() {
		return metricProviderData;
	}
	public List<Company> getCompanies() {
		return companies;
	}
}
