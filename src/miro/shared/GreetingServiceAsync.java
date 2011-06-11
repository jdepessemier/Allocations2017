package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The GreetingServiceAsync interface provides the asynchronous methods used for the RPC
 */

public interface GreetingServiceAsync {

	void getAssignments(AsyncCallback<List<Assignment>> callback);
	
	void getAllocations(AsyncCallback<List<Allocation>> callback);
		
	void putPersonAllocation(String currentPerson, String mission, String activity, int month, double value, AsyncCallback<Allocation> callback);
	
	void updateAssignments(List<Person> personList,List<Assignment> assignmentList, AsyncCallback callback);
	
	void setLocked(boolean locked, AsyncCallback callback) throws IllegalArgumentException;

	void getMonthOfDate(AsyncCallback<String> callback);

	void getConnections(AsyncCallback callback);
}
