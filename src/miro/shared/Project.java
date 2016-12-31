package miro.shared;

import java.io.Serializable;
import java.util.Comparator;

public class Project implements Serializable, Comparable {

	private String name;
	
    public static class OrderByName implements Comparator<Project> {

        @Override
        public int compare(Project o1, Project o2) {
            return o1.name.compareTo(o2.name);
        }
    }

	public Project() {
		name = "";
	}

	public Project(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o != null && this == null)
			return false;
		if (o == null && this != null)
			return false;

		if (o == null && this == null)
			return true;

		if (o instanceof Project) {
			Project project = (Project) o;

			isEquals = project.name.equals(name);
		}
		return isEquals;
	}

	@Override
	public int compareTo(Object arg0) {

		if (arg0 instanceof Project) {
			Project project = (Project) arg0;
			int compare = name.compareTo(project.name);

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;

			return 0;
		}
		return 1;
	}
}