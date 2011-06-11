package miro.shared;

import java.io.Serializable;

/**
 * This class represents a project from the project department
 **/
public class Project implements Serializable, Comparable {

	private String name;

	/**
	 * Defines a project with a default name
	 **/
	public Project() {
		name = "";
	}

	/**
	 * Defines a project with its name
	 * 
	 * @param name
	 *            Name of the project
	 **/
	public Project(String name) {
		this.name = name;
	}

	/**
	 * Return the project's name
	 * 
	 * @return The name
	 **/
	public String getName() {
		return name;
	}

	/**
	 * Compares this Project to the specified object
	 * 
	 * @param o
	 *            The object to compare this Project against
	 * @return true if the Project are equal; false otherwise.
	 **/
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

	/**
	 * Compares the egality of the name of two project
	 * 
	 * @param arg0
	 *            The object to compare this Project against
	 * @return 1 if the name of the current project is greater
	 * @return 0 if the name of the current project is equals
	 * @return -1 if the name of the current project is smaller
	 **/
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