package miro.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The GreetingService interface provides the synchronious methods used for the
 * RPC
 */

@RemoteServiceRelativePath("greetingService")
public interface GreetingService extends RemoteService {

	List<Assignment> getAssignments();
	
	List<Allocation> getAllocations();
		
	Allocation putPersonAllocation(String currentperson, String mission, String activity, int month, double value);

	void updateAssignments(List<Person> personList,List<Assignment> assignmentList);

	void setLocked(boolean locked) throws IllegalArgumentException;

	String getMonthOfDate();

	List<Connection> getConnections();
}
