package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;

/**
 * This class represents a benefit with a number and a time
 **/
public class Record implements Serializable {

	private double number;

	@Embedded
	private Time time;

	/**
	 * Defines a record with defaults number and time
	 **/
	public Record() {
		number = 0;
		time = new Time();
	}

	/**
	 * Defines a record with the number and time specified
	 * 
	 * @param number
	 *            Value of the benefit
	 * @param time
	 *            Time of the benefit
	 **/
	public Record(double number, Time time) {
		this.number = number;
		this.time = time;
	}

	/**
	 * Set the record value
	 * 
	 * @param number
	 *            new record value
	 * @return true if the update is successful
	 **/
	public boolean setNumber(double number) {
		boolean isModified = (number >= 0);

		if (isModified)
			this.number = number;

		return isModified;
	}

	/**
	 * Returns the record value
	 * 
	 * @return The value
	 **/
	public double getNumber() {
		return number;
	}

	/**
	 * Returns the record time
	 * 
	 * @return The time
	 **/
	public Time getTime() {
		return time;
	}

	/**
	 * Compares this Record to the specified object
	 * 
	 * @param o
	 *            The object to compare this Record against
	 * @return true if the Record are equal; false otherwise.
	 **/
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Record record = (Record) o;

		if (number != record.number)
			return false;

		if (time != null ? !time.equals(record.time) : record.time != null)
			return false;

		return true;
	}
}
