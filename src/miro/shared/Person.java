package miro.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;

/**
 * This class represents a person from the project department
 **/

public class Person implements Serializable, Comparable {

	private String personName;

	@Embedded
	private List<Record> holidaysList = new ArrayList<Record>();

	@Embedded
	private List<Record> trainingList = new ArrayList<Record>();

	// liste des prestations pour les activites hors-projets
	@Embedded
	private List<Record> otherList = new ArrayList<Record>();

	public Person() {
		initLists();
	}

	public Person(String name) {
		this.personName = name;
		initLists();
	}

	private void initLists() {
		Record record = null;

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);
			holidaysList.add(record);
		}

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);
			trainingList.add(record);
		}

		for (int i = 0; i < 12; i++) {
			Time time = new Time(i + 1, 2010);
			record = new Record(0, time);
			otherList.add(record);
		}
	}

	public Record getHoliday(final Time time) {
		Record recordToReturn = null;

		for (Record record : holidaysList) {
			Time otherTime = record.getTime();
			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}
	
	public void setHolliday(List<Record> valueList){
		holidaysList = valueList;
	}

	public Record getTraining(final Time time) {
		Record recordToReturn = null;

		for (Record record : trainingList) {
			Time otherTime = record.getTime();
			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}

	public void setTraining(List<Record> valueList){
		trainingList = valueList;
	}
	
	public Record getOther(final Time time) {
		Record recordToReturn = null;

		for (Record record : otherList) {
			Time otherTime = record.getTime();
			if (otherTime.equals(time)) {
				recordToReturn = record;
			}
		}
		return recordToReturn;
	}

	public void setOther(List<Record> valueList){
		otherList = valueList;
	}
	
	public String getName() {
		return personName;
	}

	public boolean equals(Object o) {

		boolean isEquals = false;

		if (o instanceof Person) {
			Person person = (Person) o;
			isEquals = person.personName.equals(personName);
		}
		return isEquals;
	}

	@Override
	public int compareTo(Object arg0) {

		if (arg0 instanceof Person) {
			Person person = (Person) arg0;
			int compare = personName.compareTo(person.personName);

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;
			return 0;
		}
		return 1;
	}
}