package org.ossmeter.metricprovider.trans.newsgroups.activeusers.model;

import com.googlecode.pongo.runtime.*;
import com.mongodb.*;
// protected region custom-imports on begin
// protected region custom-imports end

public class NewsgroupsActiveUsersTransMetric extends PongoDB {
	
	public NewsgroupsActiveUsersTransMetric() {}
	
	public NewsgroupsActiveUsersTransMetric(DB db) {
		setDb(db);
	}
	
	protected NewsgroupDataCollection newsgroups = null;
	protected UserCollection users = null;
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	
	public NewsgroupDataCollection getNewsgroups() {
		return newsgroups;
	}
	
	public UserCollection getUsers() {
		return users;
	}
	
	
	@Override
	public void setDb(DB db) {
		super.setDb(db);
		newsgroups = new NewsgroupDataCollection(db.getCollection("NewsgroupsActiveUsersTransMetric.newsgroups"));
		pongoCollections.add(newsgroups);
		users = new UserCollection(db.getCollection("NewsgroupsActiveUsersTransMetric.users"));
		pongoCollections.add(users);
	}
}