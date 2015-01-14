package auth;

import java.util.*;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import providers.MyUsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;

import com.mongodb.Mongo;
import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.BasicDBObject;

import model.Users;
import model.User;
import model.LinkedAccount;
import model.Token;
import model.TokenType;
import model.Role;
import model.Message;
import model.Notification;
import model.PlotGridEntry;
import model.SparkGridEntry;
import model.Project;
import model.Metric;
import model.EventGroup;
import model.Event;
import model.GridEntry;
import model.Statistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.googlecode.pongo.runtime.querying.*;
import com.googlecode.pongo.runtime.PongoFactory;

public class MongoAuthenticator {

	public static final String USER_ROLE = "user";
	public static final String ADMIN_ROLE = "admin";

	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

	public static Statistics getStatistics() {
		DB db = getUsersDb();
		DBCollection col = db.getCollection("statistics");
		
		DBCursor cursor = col.find().sort(new BasicDBObject("date", -1));

		Statistics stats = null;
		if (cursor.hasNext()) {
			stats = (Statistics)PongoFactory.getInstance().createPongo(cursor.next());
		}
		db.getMongo().close();
		return stats;
	}

	public static List<Project> autocomplete(String query) {
		// Turn query into regex
		String regex = "^" + query + ".*";
		BasicDBObject obj = new BasicDBObject("name", java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE));

		// Pongo doesn't support regexs yet, so we're using the Java driver
		DB db = getUsersDb();
		DBCollection col = db.getCollection("projects");
		DBCursor cursor = col.find(obj).sort(new BasicDBObject("name", 1));

		List<Project> projects = new ArrayList<>();

		while (cursor.hasNext()) {
			projects.add((Project)PongoFactory.getInstance().createPongo(cursor.next()));
		}

		db.getMongo().close();
		return projects;
	}

	public static User createUser(final AuthUser identity) {
		final User user = new User();
		user.setJoinDate(new Date());
		
		Role role = new Role();
		role.setName(USER_ROLE);
		user.getRoles().add(role);

		// Each user has a default "messages" grid entry
		Message message = new Message();
		message.setSizeX(2);
		message.setSizeY(1);
		message.setRow(1); // Stupid gridster is 1-based
		message.setCol(1);
		message.setTitle("Welcome!");
		message.setBody("<p>Thanks for signing up for OSSMETER!</p>");
		user.getGrid().add(message);

		// // DEBUG - examples only (we could leave them here though, to show the user what it would look like)
		// Notification not = new Notification();
		// not.setSizeX(1);
		// not.setSizeY(1);
		// not.setRow(1);
		// not.setCol(1);

		// Project p = new Project();
		// p.setId("epsilon");
		// p.setName("epsilon");
		// not.setProject(p);
		// Metric m = new Metric();
		// m.setId("bugs");
		// m.setName("bugs");
		// not.setMetric(m);

		// not.setThreshold(30.0);
		// not.setAboveThreshold(true);
		// user.getGrid().add(not);

		// PlotGridEntry pge = new PlotGridEntry();
		// pge.setSizeX(2);
		// pge.setSizeY(1);
		// pge.setRow(1);
		// pge.setCol(1);
		// pge.setMetric(m);
		// pge.setProject(p);
		// user.getGrid().add(pge);

		// SparkGridEntry spark = new SparkGridEntry();
		// spark.setSizeX(1);
		// spark.setSizeY(1);
		// spark.setRow(1);
		// spark.setCol(1);
		// spark.setMetric(m);
		// spark.setProject(p);
		// // spark.setLastValue(-1);
		// user.getGrid().add(spark);


		// EventGroup ev = new EventGroup();
		// ev.setName("JDK");
		// ev.setSizeX(1);
		// ev.setSizeY(1);
		// ev.setRow(1);
		// ev.setCol(1);
		// user.getGrid().add(ev);

		// Event e = new Event();
		// e.setName("JDK 1.4 Release");
		// e.setDate(new Date(2010, 9, 1));
		// ev.getEvents().add(e);

		// Event e1 = new Event();
		// e1.setName("JDK 1.5 Release");
		// e1.setDate(new Date(2013,11, 24));
		// ev.getEvents().add(e1);


		LinkedAccount acc = new LinkedAccount();
		acc.setProviderUserId(identity.getId());
		acc.setProviderKey(identity.getProvider());
		user.getLinkedAccounts().add(acc);

		if (identity instanceof EmailIdentity) {
			user.setEmail(((EmailIdentity) identity).getEmail());
			user.setEmailValidated(false);
		}

		DB db = getUsersDb();
		Users users = new Users(db);		
		users.getUsers().add(user);
		users.getUsers().sync();
		db.getMongo().close();

		return user; // TODO
	}

	public static void createToken(final TokenType type, final String token, final User user) {
		DB db = getUsersDb();
		Users users = new Users(db);	

		final Token t = new Token();
		t.setToken(token);
		t.setUser(user);
		t.setType(type);

		Date created = new Date();
		t.setCreated(created);
		Date expires = new Date(created.getTime() + VERIFICATION_TIME * 1000);
		t.setExpires(expires);

		users.getTokens().add(t);
		users.getTokens().sync();

		db.getMongo().close();
	} 

	public static Token findToken(String token, TokenType type) {
		DB db = getUsersDb();
		Users users = new Users(db);	

		Token t = null;
		Iterator<Token> it = users.getTokens().find(Token.TOKEN.eq(token), Token.TYPE.eq(type.toString())).iterator();
		if (it.hasNext()) t = it.next();

		if (t != null) t.getUser(); // Force it to dereference the user

		db.getMongo().close();
		return t;
	}

	public static void deleteToken(String token) {
		DB db = getUsersDb();
		Users users = new Users(db);	

		Token t = users.getTokens().findOneByToken(token);
		if (t != null) {
			users.getTokens().remove(t);
			users.getTokens().sync();
		}
		db.getMongo().close();
	}

	public static void verifyUser(User user) {
		DB db = getUsersDb();
		Users users = new Users(db);	

		User u = users.getUsers().findOneByEmail(user.getEmail());
		u.setEmailValidated(true);
		users.getUsers().sync();

		db.getMongo().close();	
	}

	public static User changeUserPassword(final UsernamePasswordAuthUser authUser, boolean create) {
		LinkedAccount account = null;

		DB db = getUsersDb();
		Users users = new Users(db);
		User user = users.getUsers().findOneByEmail(authUser.getEmail());

		for (LinkedAccount acc : user.getLinkedAccounts()) {
			if (acc.getProviderKey().equals(authUser.getProvider())) {
				account = acc;
				break;
			}
		}

		if (account == null) {
			if (create) {
				account = new LinkedAccount();
				account.setProviderKey(authUser.getProvider());
				user.getLinkedAccounts().add(account);
			} else {
				db.getMongo().close();
				throw new RuntimeException("Account not enabled for password usage.");				
			}
		}

		// This isn't enough to dirty the object (Pongo bug), so we dirty the email too
		account.setProviderUserId(authUser.getHashedPassword());
		user.setEmail(user.getEmail());

		users.getUsers().sync();
		db.getMongo().close();

		return user;
	}

	public static void resetUserPassword(final MyUsernamePasswordAuthUser authUser) {
		changeUserPassword(authUser, false);
	}

	public static List<User> findAllUsers() {
		DB db = getUsersDb();
		Users users = new Users(db);
		
		List<User> us = new ArrayList<>();
		Iterator<User> it = users.getUsers().iterator();

		while (it.hasNext()) {
			us.add(it.next());
		}

		db.getMongo().close();
		return us;
	}

	public static void deleteUser(User user) {
		DB db = getUsersDb();
		Users users = new Users(db);	

		User u = users.getUsers().findOneByEmail(user.getEmail());
		if (u != null) {
			users.getUsers().remove(u);
			users.getUsers().sync();
		}
		db.getMongo().close();
	}

	/* 
	 * Check whether the user exists already.
	 */
	public static boolean userExists(final AuthUser identity) {
		return findUser(identity) != null;
	}	

	/* 
	 * Find the user in the database. Email address is the key here.
	 */
	public static User findUser(final AuthUserIdentity identity) {
		if (identity == null) {
			return null;
		}
		
		DB db = getUsersDb();
		Users users = new Users(db);

		User user = findUser(users, identity);

		db.getMongo().close(); // TODO: Is this ok? Do we need to maintain connection?

		return user;
	}	

	public static User findUser(final String email) {
		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(email);

		db.getMongo().close();
		return user;
	}

	public static void mergeUsers(final AuthUser oldUser, final AuthUser newUser) {
		throw new RuntimeException("Merge users not implemented yet. Sorry about that.");
	}

	public static void linkAccount(final AuthUser user, final AuthUser toLink) {
		User u = findUser(user);
		LinkedAccount acc = new LinkedAccount();
		// TODO setup acc
		u.getLinkedAccounts().add(acc);

		// TODO need to sync - this is a bit of an issue as we need to maintain an open
		// connection to Mongo
 	}	

 	public static Project findProjectById(String projectId) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		Project p = users.getProjects().findOneByIdentifier(projectId);

		db.getMongo().close();
		return p;
 	}

 	private static User findUser(Users users, final AuthUserIdentity identity) {
 		User user = null;
		if (identity instanceof UsernamePasswordAuthUser) {
			user = users.getUsers().findOneByEmail(((UsernamePasswordAuthUser)identity).getEmail());
		} else {
			// BasicDBObject query = new BasicDBObject("linkedAccounts.providerUserId", identity.getId());
			// query.put("linkedAccounts.providerKey", identity.getProvider());
			
			FieldQueryProducer idQuery = new ArrayQueryProducer("linkedAccounts.providerUserId");
			idQuery.eq(identity.getId());

			FieldQueryProducer provQuery = new ArrayQueryProducer("linkedAccounts.providerKey");
			provQuery.eq(identity.getProvider());

			Iterator<User> it = users.getUsers().find(idQuery, provQuery).iterator();
			if (it.hasNext()) user = it.next();
		}
		return user;
 	}

 	public static void updateUserLastLogin(final AuthUser authUser) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = findUser(users, authUser);
		user.setLastLogin(new Date());

		users.getUsers().sync();

		db.getMongo().close();
 	}


 	public static void updateGridLocations(final User user, final ArrayNode loc) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User u = users.getUsers().findOneByEmail(user.getEmail());

		for (GridEntry ge : u.getGrid()) {
			for (JsonNode gloc : loc) {
				if (ge.getId().equals(gloc.get("id").textValue())) {
					ge.setCol(gloc.get("col").asInt());
					ge.setRow(gloc.get("row").asInt());

					System.out.println("row" + gloc.get("row").asInt());
					System.out.println("col" + gloc.get("col").asInt());
					break;
				}
			}			
		}

		// This is needed due to a bug in Pongo - modifying the GridEntries isn't
		// enough to dirty the collection into syncing.
		u.setEmail(u.getEmail()); 

		users.getUsers().sync();
		db.getMongo().close();
 	}

 	public static void insertNewGrid(final User user, final GridEntry entry) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User u = users.getUsers().findOneByEmail(user.getEmail());

		u.getGrid().add(entry);
		users.getUsers().sync();

		db.getMongo().close();
 	}

 	public static void deleteGridObject(final User user, final String id) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User u = users.getUsers().findOneByEmail(user.getEmail());
		GridEntry toDel = null;

		for (GridEntry g : u.getGrid()) {
			if (id.equals(g.getUid())) {
				toDel = g;
				break;
			}
		}
		u.getGrid().remove(toDel);
		
		users.getUsers().sync();
		db.getMongo().close();
 	}

 	public static Notification findNotification(User u, String projectId, String metricId) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(u.getEmail());

		Notification noti = null;

		for (GridEntry g : user.getGrid()) {
			if (g instanceof Notification) {
				Notification gg = (Notification)g;
				if (gg.getMetric().getId().equals(metricId)
					&& gg.getProject().getId().equals(projectId)) {
					noti = gg;
					break;
				}
			}
		}

		users.getUsers().sync();
		db.getMongo().close();

		return noti;
 	}

 	public static void insertNotification(User u, Notification notification) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(u.getEmail());

		notification.setSizeX(1);
		notification.setSizeY(1);

		if (notification.getRow() == 0) {
			notification.setRow(1);
		}
		if (notification.getCol() == 0){
			notification.setCol(1);
		}

		user.getGrid().add(notification);

		users.getUsers().sync();
		db.getMongo().close();
 	}


 	public static EventGroup getEventGroupById(User u, String id) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(u.getEmail());
		EventGroup toReturn = null;

		for (GridEntry g : user.getGrid()) {
			if (g instanceof EventGroup) {
				EventGroup gg = (EventGroup)g;
				if (id.equals(gg.getUid())) {
					toReturn = gg;
					break;
				}
			}
		}

		db.getMongo().close();

		return toReturn;
 	}

 	public static void updateNotification(User u, String projectId, String metricId, double threshold, boolean aboveThreshold) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(u.getEmail());

		Notification noti = null;

		for (GridEntry g : user.getGrid()) {
			if (g instanceof Notification) {
				Notification gg = (Notification)g;
				if (gg.getMetric().getId().equals(metricId)
					&& gg.getProject().getId().equals(projectId)) {
					noti = gg;
					break;
				}
			}
		}

		if (noti == null) { // Create new
			noti = new Notification();
			noti.setSizeX(1);
			noti.setSizeY(1);
			noti.setRow(1);
			noti.setCol(1);

			Project p = new Project();
			p.setId(projectId);
			// p.setName("epsilon"); // TODO
			noti.setProject(p);
			Metric m = new Metric();
			m.setId(metricId);
			// m.setName("bugs"); //TODO
			noti.setMetric(m);

			noti.setThreshold(threshold);
			noti.setAboveThreshold(aboveThreshold);
			user.getGrid().add(noti);
		} else { // Edit existing - only support one notification per project metric
			noti.setThreshold(threshold);
			noti.setAboveThreshold(aboveThreshold);
		}

		users.getUsers().sync();
		db.getMongo().close();
 	}

 	public static void toggleSparkGrid(User u, String projectid, String projectName, String metricid, String metricName) {
 		DB db = getUsersDb();
		Users users = new Users(db);

		User user = users.getUsers().findOneByEmail(u.getEmail());

		SparkGridEntry spark = null;

		for (GridEntry g : user.getGrid()) {
			if (g instanceof SparkGridEntry) {
				SparkGridEntry gg = (SparkGridEntry)g;
				if (gg.getMetric().getId().equals(metricid)
					&& gg.getProject().getId().equals(projectid)) {
					spark = gg;
					break;
				}
			}
		}

		if (spark == null) {
			spark = new SparkGridEntry();
			spark.setSizeX(1);
			spark.setSizeY(1);
			spark.setRow(1);
			spark.setCol(1);

			Project p = new Project();
			p.setId(projectid);
			p.setName(projectName);
			Metric m = new Metric();
			m.setId(metricid);
			m.setName(metricName);

			spark.setMetric(m);
			spark.setProject(p);

			user.getGrid().add(spark);
		} else {
			user.getGrid().remove(spark);
		}

		users.getUsers().sync();
		db.getMongo().close();
 	}

	// May want to be more public? Or in its own class. This is just auth.
	public static DB getUsersDb() {
		try {
			String host = play.Play.application().configuration().getString("mongo.default.host");
			if (host == null) host = "localhost";
			
			Integer port = play.Play.application().configuration().getInt("mongo.default.port");
			if (port == null) port = 27017;

			Mongo mongo = new Mongo(host, port);	
			return mongo.getDB("users");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
