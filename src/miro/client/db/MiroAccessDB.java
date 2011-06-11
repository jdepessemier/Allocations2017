package miro.client.db;

import java.util.List;

import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.Connection;
import miro.shared.GreetingService;
import miro.shared.GreetingServiceAsync;
import miro.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The class is a intermediate class between the client and the server
 **/

public class MiroAccessDB {

	static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public static void getAssignments(AsyncCallback<List<Assignment>> GET_ASSIGNMENTS_CALLBACK) {
		greetingService.getAssignments(GET_ASSIGNMENTS_CALLBACK);
	}
	
	public static void getAllocations(AsyncCallback<List<Allocation>> GET_ALLOCATIONS_CALLBACK) {
		greetingService.getAllocations(GET_ALLOCATIONS_CALLBACK);
	}
	
	public static void putPersonAllocation(String currentPerson, String mission, String activity, int month, double value, AsyncCallback<Allocation> PUT_PERSON_HOLIDAYS_CALLBACK){
		greetingService.putPersonAllocation(currentPerson, mission, activity, month, value, PUT_PERSON_HOLIDAYS_CALLBACK);
	}

	public static void updateAssignments(List<Person> personList,List<Assignment> assignmentList,AsyncCallback<String> UPDATE_ASSIGNMENTS_CALLBACK) {
		greetingService.updateAssignments(personList, assignmentList,UPDATE_ASSIGNMENTS_CALLBACK);
	}

	public static void setLocked(boolean isLocked,AsyncCallback SET_LOCKED_CALLBACK) {
		greetingService.setLocked(isLocked, SET_LOCKED_CALLBACK);
	}

	public static void getMonthOfDate(AsyncCallback<String> MONTH_CALLBACK) {
		greetingService.getMonthOfDate(MONTH_CALLBACK);
	}

	public static void getConnections(AsyncCallback<List<Connection>> CONNECTION_CALLBACK) {
		greetingService.getConnections(CONNECTION_CALLBACK);
	}
}