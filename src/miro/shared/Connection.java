package miro.shared;

import java.io.Serializable;

import javax.persistence.Id;

/**
 * This class represents a username with its password
 */
public class Connection implements Serializable {
	@Id
	Long id;

	private String lastName = "";
	private String firstName = "";
	private String pwd = "";

	/**
	 * Defines a Connection with defaults lastName,firstName and password
	 */
	public Connection() {
	}

	/**
	 * Defines a Connection with the lastName,firstName and password specified
	 * 
	 * @param lastName
	 *            Last name of the username
	 * @param firstName
	 *            First name of the username
	 * @param pwd
	 *            Password of the username
	 */
	public Connection(String lastName, String firstName, String pwd) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.pwd = pwd;
	}

	/**
	 * Returns the last name of the connection
	 * 
	 * @return The last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the first name of the connection
	 * 
	 * @return The first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the password of the connection
	 * 
	 * @return The password
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * Compares this Connection to the specified object
	 * 
	 * @param o
	 *            The object to compare this Connection against
	 * @return true if the Connection are equal; false otherwise.
	 **/
	public boolean equals(Object o) {
		boolean isEquals = false;

		if (o instanceof Connection) {
			Connection connection = (Connection) o;
			isEquals = connection.lastName.equals(lastName)
					&& connection.firstName.equals(firstName);
		}
		return isEquals;
	}
}
