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
package org.ossmeter.repository.model.github;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY,
	property = "_type")
@JsonSubTypes({
	@Type(value = GitHubIssue.class, name="org.ossmeter.repository.model.github.GitHubIssue"), })
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubIssue extends Object {

	protected List<GitHubUser> subscribed_users;
	protected List<GitHubUser> mentioned_users;
	protected int number;
	protected String state;
	protected String title;
	protected String body;
	protected GitHubUser creator;
	protected GitHubUser assignee;
	protected String created_at;
	protected String updated_at;
	protected String closed_at;
	
	public int getNumber() {
		return number;
	}
	public String getState() {
		return state;
	}
	public String getTitle() {
		return title;
	}
	public String getBody() {
		return body;
	}
	public String getCreated_at() {
		return created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public String getClosed_at() {
		return closed_at;
	}
	
	public List<GitHubUser> getSubscribed_users() {
		return subscribed_users;
	}
	public List<GitHubUser> getMentioned_users() {
		return mentioned_users;
	}
}
