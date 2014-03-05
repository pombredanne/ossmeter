package org.ossmeter.repository.model.github.importer;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.ossmeter.platform.Platform;
import org.ossmeter.repository.model.ImportData;
import org.ossmeter.repository.model.github.GitHubRepository;


public class GitHubImporter {

	private String authToken;
	 
	public GitHubImporter(String authToken) {
		this.authToken = authToken;
/*		GitHubClient gitHubClient = new GitHubClient();
		gitHubClient.setOAuth2Token("ffab283e2be3265c7b0af244e474b28430351973");
		repositoryService = new RepositoryService();
		userService = new UserService();
		collaboratorService = new CollaboratorService();
		watcherService = new WatcherService();
*/	
	}
	
	public void waitApiRate(){
		
		String url = "https://api.github.com/rate_limit?access_token="+this.authToken;
		boolean sleep = true;
		
		while (sleep) {
			try {
					InputStream is = new URL(url).openStream();
					BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));		
					String jsonText = readAll(rd);
	
					JSONObject obj=(JSONObject)JSONValue.parse(jsonText);
					JSONObject rate = (JSONObject)((JSONObject)obj.get("rate"));
				
					Integer remaining = new Integer(rate.get("remaining").toString());
					if (remaining > 0) {
						sleep = false;
					}
				
			} catch (IOException e) {
				System.err.println("Having difficulties to connect, retrying...");
				continue;
			}		
		}	
	}

	
	private boolean isNotNull(JSONObject currentRepo, String attribute ) 
	{
		if (currentRepo.get(attribute) == null)
			return false;
		else 
			return true;		
	}
	
	
	
	private static String readAll(Reader rd) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	
	public void importAll(Platform platform){
	
		int lastImportedId = 0;
		InputStream is = null;
		BufferedReader rd = null;
		String jsonText = null;
		boolean stop = false;
		
		int firstId = 0;
		int lastId = 0;


		if (platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().size() != 0) {
			lastImportedId = new Integer(platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().first().getLastImportedProject());
		} else {
			ImportData id = new ImportData();
			id.setLastImportedProject(String.valueOf(lastImportedId));
			platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().add(id);	
			platform.getProjectRepositoryManager().getProjectRepository().sync();
		}

		while (!stop) {
			try {
				is = new URL("https://api.github.com/repositories?since=" + lastImportedId + "&access_token=" + this.authToken).openStream();		
				rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				jsonText = readAll(rd);			
			} catch (IOException e) {
				System.err.println("API rate limit exceeded. Waiting to restart the importing...");
//				platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportingData().first().setLastImportedProject(String.valueOf(lastImportedId));
//				platform.getProjectRepositoryManager().getProjectRepository().sync();
				waitApiRate();
				break;				
			}
			
			JSONArray obj=(JSONArray)JSONValue.parse(jsonText);
			if (! obj.isEmpty()) {
				firstId = new Integer((((JSONObject)(obj.get(0))).get("id")).toString());
				lastId = new Integer((((JSONObject)(obj.get(obj.size()-1))).get("id")).toString());
				System.out.println("Scanning page: " + "https://api.github.com/repositories?since=" + lastImportedId);
				System.out.println("--> Importing repositories from id:" + firstId + " to id:" + lastId);
				platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().first().setLastImportedProject(String.valueOf(firstId-1));
				platform.getProjectRepositoryManager().getProjectRepository().sync();	
				
				Iterator iter = obj.iterator();
				while (iter.hasNext()) {
					JSONObject entry = (JSONObject) iter.next();		
					GitHubRepository repository = importRepository((String) entry.get("full_name"), platform);
					platform.getProjectRepositoryManager().getProjectRepository().getProjects().add(repository);
				}
				platform.getProjectRepositoryManager().getProjectRepository().sync();	
				lastImportedId = lastId;
			} else {
				stop = true;
				System.out.println("Importing completed.");
				break;
			}
		}
	}
	
	
	public GitHubRepository importRepository(String projectId, Platform platform) {

			int lastImportedId = new Integer(platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().first().getLastImportedProject());

			GitHubRepository repository = new GitHubRepository();
			JSONObject currentRepo = null;
			try {
				System.out.println("---> processing repository " + projectId);

				InputStream is = new URL("https://api.github.com/repos/" + projectId + "?access_token=" + this.authToken).openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				String jsonText = readAll(rd);
			      		
				currentRepo=(JSONObject)JSONValue.parse(jsonText);

				if ((isNotNull(currentRepo,"description")))					
					repository.setDescription(currentRepo.get("description").toString());

				repository.setFull_name(projectId);
				
				repository.setName(currentRepo.get("name").toString());

				if ((isNotNull(currentRepo,"private")))					
					repository.set_private(new Boolean(currentRepo.get("private").toString()));

				if ((isNotNull(currentRepo,"fork")))					
					repository.setFork(new Boolean(currentRepo.get("fork").toString()));
				
				
				if ((isNotNull(currentRepo,"git_url")))					
					repository.setGit_url(currentRepo.get("git_url").toString());
				
				if ((isNotNull(currentRepo,"html_url")))					
					repository.setHtml_url(currentRepo.get("html_url").toString());
				
				if ((isNotNull(currentRepo,"clone_url")))					
					repository.setClone_url(currentRepo.get("clone_url").toString());

				if ((isNotNull(currentRepo,"homepage")))					
					repository.setHomepage(currentRepo.get("homepage").toString());

				if ((isNotNull(currentRepo,"mirror_url")))					
					repository.setMirror_url(currentRepo.get("mirror_url").toString());

				if ((isNotNull(currentRepo,"master_branch")))					
					repository.setMaster_branch(currentRepo.get("master_branch").toString());

				if ((isNotNull(currentRepo,"ssh_url")))					
					repository.setSsh_url(currentRepo.get("ssh_url").toString());
				
				
				if ((isNotNull(currentRepo,"svn_url")))					
					repository.setSvn_url(currentRepo.get("svn_url").toString());
				
				repository.setSize(new Integer(currentRepo.get("size").toString()));

				lastImportedId = new Integer(currentRepo.get("id").toString()); 
				platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportData().first().setLastImportedProject(String.valueOf(lastImportedId));
				platform.getProjectRepositoryManager().getProjectRepository().sync();	

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				System.err.println("API rate limit exceeded. Waiting to restart the importing...");
//				platform.getProjectRepositoryManager().getProjectRepository().getGitHubImportingData().first().setLastImportedProject(String.valueOf(lastImportedId));
//				platform.getProjectRepositoryManager().getProjectRepository().sync();
				waitApiRate();
			} 
			
			return repository;	
		
	}

}