/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.ossmeter.platform.delta.vcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VcsCommit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Date date;
	protected String author;
	protected String authorEmail;
	protected String message;
	protected List<VcsCommitItem> items = new ArrayList<VcsCommitItem>();
	protected VcsRepositoryDelta repositoryDelta;
	protected String revision;
	
	public Date getJavaDate() {
		return date;
	}
	
	public void setJavaDate(Date date) {
		this.date = date;
	}
	
	public String getRevision() {
		return revision;
	}
	
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
	public String getAuthorEmail() {
		return authorEmail;
	}
	
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public VcsRepositoryDelta getDelta() {
		return repositoryDelta;
	}
	
	public void setDelta(VcsRepositoryDelta delta) {
		this.repositoryDelta = delta;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<VcsCommitItem> getItems() {
		return items;
	}
	
}
