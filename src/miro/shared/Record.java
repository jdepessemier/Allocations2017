package miro.shared;

import java.io.Serializable;

import javax.persistence.Embedded;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	private double number;

	@Embedded
	private Time time;

	public Record() {
		number = 0;
		time = new Time();
	}

	public Record(double number, Time time) {
		this.number = number;
		this.time = time;
	}

	public boolean setNumber(double number) {
		boolean isModified = (number >= 0);

		if (isModified)
			this.number = number;

		return isModified;
	}

	public double getNumber() {
		return number;
	}

	public Time getTime() {
		return time;
	}

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
