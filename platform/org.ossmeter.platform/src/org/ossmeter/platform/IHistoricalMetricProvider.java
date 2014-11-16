package org.ossmeter.platform;

import org.ossmeter.repository.model.Project;

import com.googlecode.pongo.runtime.Pongo;

public interface IHistoricalMetricProvider extends IMetricProvider {
	
	public String getCollectionName();
	
	public Pongo measure(Project project);
}
