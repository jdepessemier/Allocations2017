package miro.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.OfficialInformation;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Record;
import miro.shared.Time;

public class MiroState {
	
	private static List<Person> personList = new ArrayList<Person>();
	private static List<Project> projectList = new ArrayList<Project>();
	private static List<Assignment> assignmentList = new ArrayList<Assignment>();

	public static List<Project> getNotAssignedProject(Person person) {
		List<Project> projectNotAssigned = new ArrayList<Project>();

		if (person != null) {
			for (Project projectFromList : projectList) {
				Assignment assignment = new Assignment(projectFromList, person);

				if (!assignmentList.contains(assignment))
					projectNotAssigned.add(projectFromList);
			}
		}
		Collections.sort(projectNotAssigned);
		return projectNotAssigned;
	}

	public static List<Person> getPersonNotAssigned(Project project) {
		List<Person> personNotAssignedOfProject = new ArrayList<Person>();

		if (project != null) {
			for (Person personFromList : personList) {
				Assignment assignment = new Assignment(project, personFromList);

				if (!assignmentList.contains(assignment)) {
					personNotAssignedOfProject.add(personFromList);
				}
			}
		}
		Collections.sort(personNotAssignedOfProject);
		return personNotAssignedOfProject;
	}

	public static List<Assignment> getAssignments(Person person) {
		
		List<Assignment> assignmentFromPerson = new ArrayList<Assignment>();

		if (person != null) {
			for (Assignment assignment : assignmentList) {

				if (assignment.getPerson().equals(person)) {
					assignmentFromPerson.add(assignment);
				}
			}
		}
		return assignmentFromPerson;
	}

	public static List<Assignment> getAssignments(Project project) {
		List<Assignment> assignmentFromProject = new ArrayList<Assignment>();

		if (project != null) {
			for (Assignment assignment : assignmentList) {

				if (assignment.getProject().equals(project)) {
					assignmentFromProject.add(assignment);
				}
			}
		}
		return assignmentFromProject;
	}

	public static Person getPerson(String lastName) {
		
		Person person = new Person(lastName);

		int indexOf = personList.indexOf(person);

		if (indexOf < 0) {
			return null;
		}
		return personList.get(indexOf);
	}

	public static Assignment getAssignment(Person person, Project project) {

		Assignment assignment = new Assignment(project, person);
		int indexOfAssignment = assignmentList.indexOf(assignment);

		return assignmentList.get(indexOfAssignment);
	}

	static List<Assignment> builAssignmentList(List<Allocation> allocationsList) {
			
		for (Allocation allocation : allocationsList) {	

			String activityName = allocation.getActivityName();
			String personFullName = allocation.getPersonFullName();
			String missionName = allocation.getMissionName();
			Record[] listOfValue = new Record[12];
			
			for (int i=0;i<12;i++){
				Time time = new Time(i+1,OfficialInformation.CURRENT_YEAR);
				Double value = round(allocation.getAllocation(i),2);
				Record newRecord = new Record(value,time);
				listOfValue[i]= newRecord;
			}
			
			if (activityName.equals("Projets")){
				
				Person newPerson = new Person(personFullName);
				Project newProject = new Project(missionName);
				Assignment newAssignment = new Assignment(newProject,newPerson);
				newAssignment.setPrestation(listOfValue);
				if (assignmentList.isEmpty()){
					assignmentList.add(newAssignment);
				} else {
					if (assignmentList.contains(newAssignment)){
						int idx = assignmentList.indexOf(newAssignment);
						Assignment retrievedAssignment = assignmentList.get(idx);
						retrievedAssignment.setPrestation(listOfValue);
					} else {
						assignmentList.add(newAssignment);
					}
				}
			}					
		}	
		return assignmentList;
	}
	
	static List<Person> buildPersonList(List<Allocation> allocationsList) {
		
		// build the List of persons with their holidays, training, and other activities
		
		for (Allocation allocation : allocationsList) {
			
			String activityName = allocation.getActivityName();
			String personFullName = allocation.getPersonFullName();
			String missionName = allocation.getMissionName();
			
			if (!activityName.equals("Projets")){
				
				List<Record> valueList = new ArrayList<Record>();
				for (int i=0;i<12;i++){
					Time time = new Time(i+1,OfficialInformation.CURRENT_YEAR);
					Double value = round(allocation.getAllocation(i),2);
					Record newRecord = new Record(value,time);
					valueList.add(newRecord);
				}
				
 				Person newPerson = new Person(personFullName);
 				
 				// First the list of Persons is empty
 				if (personList.isEmpty()){
 					if (activityName.equals("Congés & Absences")){
 						newPerson.setHolliday(valueList);
 					}
 	 				if (activityName.equals("Formations")){
 	 					newPerson.setTraining(valueList);
 	 				}
 	 				if (activityName.equals("Activités Hors Projets")){
 	 	 				newPerson.setOther(valueList);
 					}
 	 				personList.add(newPerson);
 				} else { // The list is not empty, then we try to get back the person in the list
 					if (personList.contains(newPerson)){ // the list contains already the person
 						int index = personList.indexOf(newPerson);
 						Person retrievedPerson = personList.get(index);
 	 					if (activityName.equals("Congés & Absences")){
 	 						retrievedPerson.setHolliday(valueList);
 	 					}
 	 	 				if (activityName.equals("Formations")){
 	 	 					retrievedPerson.setTraining(valueList);
 	 	 				}
 	 	 				if (activityName.equals("Activités Hors Projets")){
 	 	 	 				retrievedPerson.setOther(valueList);
 	 					}
 					} else { // the list does not contain the person
 	 					if (activityName.equals("Congés & Absences")){
 	 						newPerson.setHolliday(valueList);
 	 					}
 	 	 				if (activityName.equals("Formations")){
 	 	 					newPerson.setTraining(valueList);
 	 	 				}
 	 	 				if (activityName.equals("Activités Hors Projets")){
 	 	 	 				newPerson.setOther(valueList);
 	 					}
 	 	 				personList.add(newPerson);
 					}
 				}
 			}			
 		}
		return personList;
	}

	static List<Assignment> builFinalAssignmentList(List<Person> personList, List<Assignment> assignmentList) {
		
		for (Assignment assignment : assignmentList) {	
			
			Person assignedPerson = assignment.getPerson();
			
			if (personList.contains(assignedPerson)){
				int index = personList.indexOf(assignedPerson);
				Person retrievedPerson = personList.get(index);
				assignment.setPerson(retrievedPerson);
			}
		}
//		Collections.sort(assignmentList, new Assignment.OrderByName());
		return assignmentList;
	}
	
	static void updateViewState(List<Assignment> assignmentList) {
		
		MiroState.assignmentList = assignmentList;
		updatePersonList();
		updateProjectList();
	}

	private static void updatePersonList() {
		
		personList.clear();

		for (Assignment assignment : assignmentList) {
			Person personFromAssignment = assignment.getPerson();

			if (!personList.contains(personFromAssignment)) {
				personList.add(personFromAssignment);
			}
		}
		Collections.sort(personList, new Person.OrderByPersonName());
	}	

	static void addAssignment(Assignment assignment) {
		if (assignment != null)
			assignmentList.add(assignment);
	}

	private static void updateProjectList() {
		
		projectList.clear();

		for (Assignment assignment : assignmentList) {
			Project projectFromAssignment = assignment.getProject();

			if (!projectList.contains(projectFromAssignment)) {
				projectList.add(projectFromAssignment);
			}
		}
		Collections.sort(projectList, new Project.OrderByName());
	}

	public static Project getProject(String projectName) {
		
		Project project = new Project(projectName);

		int indexOfProject = projectList.indexOf(project);

		if (indexOfProject < 0) {
			return null;
		}
		return projectList.get(indexOfProject);
	}

	static List<Person> getPersonList() {
		return personList;
	}

	static List<Project> getProjectList() {
		return projectList;
	}

	static List<Assignment> getAssignmentList() {
		return assignmentList;
	}
	
    static double round(double what, int howmuch) {
    	return (double)( (int)(what * Math.pow(10,howmuch) + .5) ) / Math.pow(10,howmuch);
    }
	
}
