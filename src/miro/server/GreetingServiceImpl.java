package miro.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.Connection;
import miro.shared.GreetingService;
import miro.shared.Lock;
import miro.shared.Person;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

/**
 * This class represents the server contains the methods of the RPC
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	Calendar calendar = new GregorianCalendar();

	static {
		
		ObjectifyService.register(Lock.class);
		ObjectifyService.register(Connection.class);
		ObjectifyService.register(Assignment.class);

		Objectify ofy = ObjectifyService.begin();

		if (ofy.query(Connection.class).countAll() == 0) {

			Objectify obj = ObjectifyService.beginTransaction();
			obj.put(new Connection("DE PESSEMIER", "Johan", "stw657"));
			obj.getTxn().commit();
			obj = ObjectifyService.beginTransaction();
			obj.put(new Connection("AUQUIERE", "Eric", "stw657"));
			obj.getTxn().commit();
			obj = ObjectifyService.beginTransaction();
			obj.put(new Connection("PROJECT", "MANAGER", "dbj409"));
			obj.getTxn().commit();
		}

		if (ofy.query(Lock.class).countAll() == 0) {

			Objectify obj = ObjectifyService.beginTransaction();
			obj.put(new Lock(1l));
			obj.getTxn().commit();
		}
	}

	public void setLocked(boolean locked) throws IllegalArgumentException {
		Objectify ofy = ObjectifyService.begin();
		Lock lock = (Lock) ofy.find(new Key(Lock.class, 1));

		if (lock.isLocked()) {
			if (locked)
				throw new IllegalArgumentException(
						"Table utilisée par un autre utilisateur.\nRéessayez plus tard!");
			else
				lock.setLocked(false);
		} else {
			lock.setLocked(true);
		}
		ofy.put(lock);
	}

	public List<Assignment> getAssignments() {
		
		List<Assignment> assignmentList = new ArrayList<Assignment>();

		Objectify ofy = ObjectifyService.begin();

		Query<Assignment> query = ofy.query(Assignment.class);

		for (Assignment assignmentFromList : query) {
			assignmentList.add(assignmentFromList);
		}

		return assignmentList;
	}
	
	public List<Allocation> getAllocations() {
		
		List<Allocation> allocationsList = new ArrayList<Allocation>();
		
		ObjectifyService.register(Allocation.class);

		Objectify ofy = ObjectifyService.begin();

		Query<Allocation> query = ofy.query(Allocation.class);

		for (Allocation allocationFromQuery : query) {
			allocationsList.add(allocationFromQuery);
		}
		return allocationsList;
	}
	

	public Allocation putPersonAllocation(String personname,
										  String missionname,
										  String activityname, 
										  int month, 
										  double value) {
		
		ObjectifyService.register(Allocation.class);
		Objectify ofy = ObjectifyService.begin();
		Query<Allocation> query = ofy.query(Allocation.class);
        
			
		Allocation allocation = query
			.filter("personFullName =", personname)
			.filter("missionName =", missionname)			
			.filter("activityName =", activityname).get();
			
	    if (allocation == null) allocation = new Allocation(personname, missionname, activityname);
	        allocation.setAllocation(month, value);
	  
	        ofy.put(allocation);
			
	        return allocation;
	        
	}

	public void updateAssignments(List<Person> personList,List<Assignment> assignmentList) {
		
		List<Assignment> assignmentListFromDB = getAssignments();

		for (Assignment assignment : assignmentList) {

			if (assignmentListFromDB.contains(assignment)) {
				int indexOf = assignmentListFromDB.indexOf(assignment);
				Assignment assignment2 = assignmentListFromDB.get(indexOf);

				for (int i = 0; i < 12; i++) {
					assignment2.setPrestation(i, assignment.getPrestation(i));
				}
			} else {
				assignmentListFromDB.add(assignment);
			}
		}
		updateInfosPersonInAssignmentList(personList, assignmentListFromDB);

		putData(assignmentListFromDB);

		setLocked(false);
	}

	private void updateInfosPersonInAssignmentList(List<Person> personList,List<Assignment> assignmentList2) {

		for (int i = 0; i < personList.size(); i++) {

			Person person = personList.get(i);

			for (int j = 0; j < assignmentList2.size(); j++) {
				if (assignmentList2.get(j).getPerson().equals(person)) {
					assignmentList2.get(j).setPerson(person);
				}
			}
		}
	}

	private void putData(List<Assignment> assignmentList) {

		for (Assignment assignment : assignmentList) {
			Objectify ofy = ObjectifyService.beginTransaction();

			try {
				ofy.put(assignment);
				ofy.getTxn().commit();
			} catch (Exception e) {
				ofy.put(assignment);
				ofy.getTxn().commit();
			}
		}

	}
	
	public List<Connection> getConnections() {

		List<Connection> connectionList = new ArrayList<Connection>();

		Objectify ofy = ObjectifyService.begin();

		Query<Connection> query = ofy.query(Connection.class);

		for (Connection connection : query) {
			connectionList.add(connection);
		}

		return connectionList;
	}

	public String getMonthOfDate() {
		return "" + (calendar.get(GregorianCalendar.MONTH) + 1);
	}

	public String getYearOfDate() {
		return "" + calendar.get(GregorianCalendar.YEAR);
	}		
}