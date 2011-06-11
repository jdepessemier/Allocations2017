package miro.shared;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class Allocation implements Serializable {
	
	@Id
	Long id;
	
    private String personFullName;
    private String missionName;
    private String activityName;	
	private double[] arrayOfWorkDays = new double[12];
	
	public Allocation() {
		personFullName = "";
		missionName = "";
		activityName = "";
		initArrayOfWorkDays();
	}
	
	public Allocation( String person, String mission, String activity) {
		this.personFullName = person;
		this.missionName = mission;
		this.activityName = activity;
		initArrayOfWorkDays();
	}

	private void initArrayOfWorkDays() {
		for (int i = 0; i < arrayOfWorkDays.length; i++) {
			arrayOfWorkDays[i] = 0.00;
		}
	}
	
	public double getAllocation(int index) {
		return arrayOfWorkDays[index];
	}

	public void setAllocation(int index, double value) {
		arrayOfWorkDays[index-1] = value;
	}

	public String getPersonFullName() {
		return personFullName;
	}

    public String getMissionName() {
        return missionName;
    }
 
    public String getActivityName() {
        return activityName;
    }
    

	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof Allocation) {
			Allocation allocation = (Allocation) o;
			isEquals = missionName.equals(allocation.getMissionName())&& 
					   personFullName.equals(allocation.getPersonFullName())&& 
					   activityName.equals(allocation.getActivityName());
		}
		return isEquals;
	}
	
}
