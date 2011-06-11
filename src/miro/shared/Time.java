package miro.shared;

import java.io.Serializable;

/**
 * This class represents a month and a year
 **/
public class Time implements Serializable {

	private int month;
	private int year;

	/**
	 * Defines a Time with defaults month and year
	 **/
	public Time() {
		month = 0;
		year = 0;
	}

	/**
	 * Defines a Time
	 * 
	 * @param month
	 *            The month
	 * @param year
	 *            The year
	 **/
	public Time(int month, int year) {
		this.month = month;
		this.year = year;
	}

	/**
	 * Returns the month
	 * 
	 * @return The month
	 **/
	public int getMonth() {
		return month;
	}

	/**
	 * Returns the year
	 * 
	 * @return The year
	 **/
	public int getYear() {
		return year;
	}

	/**
	 * Compares this Time to the specified object
	 * 
	 * @param o
	 *            The object to compare this Time against
	 * @return true if the Time are equal; false otherwise.
	 **/
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Time time = (Time) o;

		if (month != time.month)
			return false;
		if (year != time.year)
			return false;

		return true;
	}
}