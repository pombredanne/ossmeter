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
package org.ossmeter.metricprovider.trans.newsgroups.contentclasses;

import java.util.Arrays;
import java.util.List;

import org.ossmeter.metricprovider.trans.newsgroups.contentclasses.model.ContentClass;
import org.ossmeter.metricprovider.trans.newsgroups.contentclasses.model.NewsgroupData;
import org.ossmeter.metricprovider.trans.newsgroups.contentclasses.model.NewsgroupsContentClassesTransMetric;
import org.ossmeter.metricprovider.trans.newsgroups.threads.ThreadsTransMetricProvider;
import org.ossmeter.metricprovider.trans.newsgroups.threads.model.ArticleData;
import org.ossmeter.metricprovider.trans.newsgroups.threads.model.NewsgroupsThreadsTransMetric;
import org.ossmeter.metricprovider.trans.newsgroups.threads.model.ThreadData;
import org.ossmeter.platform.IMetricProvider;
import org.ossmeter.platform.ITransientMetricProvider;
import org.ossmeter.platform.MetricProviderContext;
import org.ossmeter.platform.delta.ProjectDelta;
import org.ossmeter.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.ossmeter.repository.model.CommunicationChannel;
import org.ossmeter.repository.model.Project;
import org.ossmeter.repository.model.cc.nntp.NntpNewsGroup;
import org.ossmeter.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

public class ContentClassesTransMetricProvider implements ITransientMetricProvider<NewsgroupsContentClassesTransMetric>{

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;

	protected MetricProviderContext context;
	
	protected List<IMetricProvider> uses;

	@Override
	public String getIdentifier() {
		return ContentClassesTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup) return true;
			if (communicationChannel instanceof Discussion) return true;
		}
		return false;
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses = uses;
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(ThreadsTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
	}

	@Override
	public NewsgroupsContentClassesTransMetric adapt(DB db) {
		return new NewsgroupsContentClassesTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, NewsgroupsContentClassesTransMetric db) {

		if (uses.size()!=1) {
			System.err.println("Metric: " + getIdentifier() + " failed to retrieve " + 
								"the transient metric it needs!");
			System.exit(-1);
		}

		NewsgroupsThreadsTransMetric usedThreads = 
				((ThreadsTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));

		db.getNewsgroups().getDbCollection().drop();
		db.getContentClasses().getDbCollection().drop();
		db.sync();
		
		for (ThreadData thread: usedThreads.getThreads()) {
			
			for (ArticleData articleData: thread.getArticles()) {
				
				Iterable<NewsgroupData> newsgroupDataIt = 
					db.getNewsgroups().findByNewsgroupName(articleData.getNewsgroupName());
				NewsgroupData newsgroupData = null;
				for (NewsgroupData ngd:  newsgroupDataIt) 
					newsgroupData = ngd;
				if (newsgroupData == null) {
					newsgroupData = new NewsgroupData();
					newsgroupData.setNewsgroupName(articleData.getNewsgroupName());
					newsgroupData.setNumberOfArticles(0);
					db.getNewsgroups().add(newsgroupData);
				}
				newsgroupData.setNumberOfArticles(newsgroupData.getNumberOfArticles() + 1);
				
				Iterable<ContentClass> contentClassIt =db.getContentClasses().find(
								ContentClass.NEWSGROUPNAME.eq(articleData.getNewsgroupName()),
								ContentClass.CLASSLABEL.eq(articleData.getContentClass()));
				ContentClass contentClass = null;
				for (ContentClass cc:  contentClassIt) contentClass = cc;
				if (contentClass == null) {
					contentClass = new ContentClass();
					contentClass.setNewsgroupName(articleData.getNewsgroupName());
					contentClass.setClassLabel(articleData.getContentClass());
					contentClass.setNumberOfArticles(0);
					db.getContentClasses().add(contentClass);
				}
				contentClass.setNumberOfArticles(contentClass.getNumberOfArticles() + 1);
				db.sync();
			}
		}

		for (NewsgroupData newsgroupData: db.getNewsgroups()) {
			Iterable<ContentClass> contentClassIt = 
					db.getContentClasses().find(ContentClass.NEWSGROUPNAME.eq(newsgroupData.getNewsgroupName()));
			for (ContentClass contentClass:  contentClassIt)
				contentClass.setPercentage( ((float) 100 * contentClass.getNumberOfArticles()) / 
														newsgroupData.getNumberOfArticles());
		}
		db.sync();
	}

	@Override
	public String getShortIdentifier() {
		return "NewsgroupcontentClasses";
	}

	@Override
	public String getFriendlyName() {
		return "Content Classes in Newgroup Articles";
	}

	@Override
	public String getSummaryInformation() {
		return "Content Classes in Newgroup Articles";
	}

}
