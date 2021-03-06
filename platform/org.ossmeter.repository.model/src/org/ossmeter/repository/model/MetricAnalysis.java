package org.ossmeter.repository.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class MetricAnalysis extends Pongo {
	
	
	
	public MetricAnalysis() { 
		super();
		METRICID.setOwningType("org.ossmeter.repository.model.MetricAnalysis");
		PROJECTID.setOwningType("org.ossmeter.repository.model.MetricAnalysis");
		ANALYSISDATE.setOwningType("org.ossmeter.repository.model.MetricAnalysis");
		EXECUTIONDATE.setOwningType("org.ossmeter.repository.model.MetricAnalysis");
		MILLISTAKEN.setOwningType("org.ossmeter.repository.model.MetricAnalysis");
	}
	
	public static StringQueryProducer METRICID = new StringQueryProducer("metricId"); 
	public static StringQueryProducer PROJECTID = new StringQueryProducer("projectId"); 
	public static StringQueryProducer ANALYSISDATE = new StringQueryProducer("analysisDate"); 
	public static StringQueryProducer EXECUTIONDATE = new StringQueryProducer("executionDate"); 
	public static NumericalQueryProducer MILLISTAKEN = new NumericalQueryProducer("millisTaken");
	
	
	public String getMetricId() {
		return parseString(dbObject.get("metricId")+"", "");
	}
	
	public MetricAnalysis setMetricId(String metricId) {
		dbObject.put("metricId", metricId);
		notifyChanged();
		return this;
	}
	public String getProjectId() {
		return parseString(dbObject.get("projectId")+"", "");
	}
	
	public MetricAnalysis setProjectId(String projectId) {
		dbObject.put("projectId", projectId);
		notifyChanged();
		return this;
	}
	public Date getAnalysisDate() {
		return parseDate(dbObject.get("analysisDate")+"", null);
	}
	
	public MetricAnalysis setAnalysisDate(Date analysisDate) {
		dbObject.put("analysisDate", analysisDate);
		notifyChanged();
		return this;
	}
	public Date getExecutionDate() {
		return parseDate(dbObject.get("executionDate")+"", null);
	}
	
	public MetricAnalysis setExecutionDate(Date executionDate) {
		dbObject.put("executionDate", executionDate);
		notifyChanged();
		return this;
	}
	public double getMillisTaken() {
		return parseDouble(dbObject.get("millisTaken")+"", 0.0d);
	}
	
	public MetricAnalysis setMillisTaken(double millisTaken) {
		dbObject.put("millisTaken", millisTaken);
		notifyChanged();
		return this;
	}
	
	
	
	
}