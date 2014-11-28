package org.ossmeter.platform.client.api;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import org.ossmeter.platform.Date;
import org.ossmeter.platform.client.api.cache.SparkCache;
import org.ossmeter.platform.visualisation.MetricVisualisation;
import org.ossmeter.platform.visualisation.MetricVisualisationExtensionPointManager;
import org.ossmeter.platform.visualisation.UnsparkableVisualisationException;
import org.ossmeter.repository.model.Project;
import org.ossmeter.repository.model.ProjectRepository;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.QueryBuilder;

public class SparkResource extends AbstractApiResource {

	public Representation doRepresent() {
		// Check cache
		String sd = SparkCache.getSparkCache().getSparkData(getRequest().getResourceRef().toString());
		
		if (sd != null) {
			JsonNode obj;
			try {
				System.out.println("SD: "+ sd);
				obj = mapper.readTree(sd);
				return Util.createJsonRepresentation(obj);
			} catch (Exception e) {
				e.printStackTrace(); // FIXME
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				ObjectNode node = mapper.createObjectNode();
				node.put("status", "error");
				node.put("msg", "Error whilst retrieving sparkline.");
				return Util.createJsonRepresentation(node);
			}
		}
		
		// Miss. Hit database.
		String projectId = (String) getRequest().getAttributes().get("projectid");
		String metric = (String) getRequest().getAttributes().get("metricid");

		String[] metrics = metric.split("\\+");
		System.err.println("metrics to get: " + metrics);
		ArrayNode sparks = mapper.createArrayNode();
		for (String metricId : metrics) {
			
			String agg = getQueryValue("agg");
			String start = getQueryValue("startDate");
			String end = getQueryValue("endDate");
			
			QueryBuilder builder = QueryBuilder.start();
			if (agg != null && agg != "") {
	//			builder.... // TODO
			}
			try {
				if (start != null && start != "") {
					builder.and("__datetime").greaterThanEquals(new Date(start).toJavaDate());
				}
				if (end != null && end != "") {
					builder.and("__datetime").lessThanEquals(new Date(end).toJavaDate());
				}
			} catch (ParseException e) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				ObjectNode node = mapper.createObjectNode();
				node.put("status", "error");
				node.put("msg", "Invalid date. Format must be YYYYMMDD.");
				node.put("request", generateRequestJson(projectId, metricId));
				return Util.createJsonRepresentation(node);
			}
			
			BasicDBObject query = (BasicDBObject) builder.get(); 

			ProjectRepository projectRepo = platform.getProjectRepositoryManager().getProjectRepository();
			Project project = projectRepo.getProjects().findOneByShortName(projectId);
			if (project == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return Util.createJsonRepresentation(generateErrorMessage(mapper, "No project matched that requested.", projectId, metricId));
				
			}
			
			MetricVisualisationExtensionPointManager manager = MetricVisualisationExtensionPointManager.getInstance();
			Map<String, MetricVisualisation> registeredVisualisations = manager.getRegisteredVisualisations();
			System.out.println("registered visualisations: " + registeredVisualisations.keySet());
			MetricVisualisation vis = manager.findVisualisationById(metricId);
			
			if (vis == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return Util.createJsonRepresentation(generateErrorMessage(mapper, "No visualiser found with specified ID.", projectId, metricId));
			}
			
			DB db = platform.getMetricsRepository(project).getDb();
			
			System.setProperty("java.awt.headless", "true");
			
			byte[] sparky;
			try {
				sparky = vis.getSparky(db, query);
				ObjectNode sparkData = vis.getSparkData();

				if (sparky != null) {
					String uuid = UUID.randomUUID().toString();
					SparkCache.getSparkCache().putSpark(uuid, sparky);
					sparkData.put("spark", "/spark/"+uuid);
				}
				sparkData.put("metricId", metricId);
				sparkData.put("projectId", projectId);
				
				// And add to the return list
				sparks.add(sparkData);		
			} catch (ParseException e) {
//				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				// TODO Log this as series - needs investigating by admin
				sparks.add(generateErrorMessage(mapper, "Error whilst generating sparkle. Unable to parse data.", projectId, metricId));
			} catch (UnsparkableVisualisationException e) {
//				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				sparks.add(generateErrorMessage(mapper, "Visualisation not sparkable. Metrics must be time series in order to be sparkable.", projectId, metricId));
			} catch (IOException e) {
				e.printStackTrace(); // FIXME
//				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				sparks.add(generateErrorMessage(mapper, "Error whilst generating sparkle.", projectId, metricId));
			}
		}

		// Put in the cache
		
		if (sparks.size() == 1) { 
			SparkCache.getSparkCache().putSparkData(getRequest().getResourceRef().toString(), (ObjectNode) sparks.get(0));
			return Util.createJsonRepresentation(sparks.get(0));
		} else {
			SparkCache.getSparkCache().putSparkData(getRequest().getResourceRef().toString(), sparks);
			return Util.createJsonRepresentation(sparks);
		}
	}
	
	private JsonNode generateErrorMessage(ObjectMapper mapper, String msg, String projectId, String metricId) {
		ObjectNode node = mapper.createObjectNode();
		node.put("status", "error");
		node.put("msg", msg);
		node.put("request", generateRequestJson(projectId, metricId));
		return node;
	}
	
	private JsonNode generateRequestJson(String projectName, String metricId) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode r = mapper.createObjectNode();
		
		r.put("project", projectName);
		r.put("metricId", metricId);
		
		return r;
	}
}
