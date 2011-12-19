package miro.shared;

import java.io.Serializable;

public class Time implements Serializable {

	private static final long serialVersionUID = 1L;
	private int month;
	private int year;

	public Time() {
		month = 0;
		year = 0;
	}

	public Time(int month, int year) {
		this.month = month;
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

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