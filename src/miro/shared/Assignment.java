package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Id;

/**
 * This class defines a project contains a person with his prestations
 **/
public class Assignment implements Serializable {

	@Id
	Long id;

	@Embedded
	private Project project;

	@Embedded
	private Person person;

	@Embedded
	private Record[] recordListOfPrestation = new Record[12];
	
//    public static class OrderByName implements Comparator<Assignment> {
//
//        @Override
//        public int compare(Assignment o1, Assignment o2) {
//            return o1.person.getName().compareTo(o2.person.getName());
//        }
//    }

	/**
	 * Defines a Assignment with a project and a person with null value
	 **/
	public Assignment() {
		project = null;
		person = null;
		initRecordListOfPrestation();
	}

	public Assignment(Project project, Person person) {
		this.project = project;
		this.person = person;
		initRecordListOfPrestation();
	}

	private void initRecordListOfPrestation() {
		for (int i = 0; i < recordListOfPrestation.length; i++) {
			recordListOfPrestation[i] = new Record();
		}
	}

	public Record getPrestation(int index) {
		return recordListOfPrestation[index];
	}

	public void setPrestation(int index, Record record) {
		recordListOfPrestation[index] = record;
	}
	
	public void setPrestation(Record[] record) {
		recordListOfPrestation = record;
	}

	public Project getProject() {
		return project;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int compareTo(Object arg0) {

		if (arg0 instanceof Assignment) {
			Assignment assignment = (Assignment) arg0;
			int compare = assignment.person.getName().compareTo(assignment.person.getName());

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;

			return 0;
		}
		return 1;
	}
	
	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof Assignment) {
			Assignment assignment = (Assignment) o;

			isEquals = project.equals(assignment.project)
					&& person.equals(assignment.person);
		}
		return isEquals;
	}
}