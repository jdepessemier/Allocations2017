package miro.shared;

import javax.persistence.Id;

/**
 * This class defines a Lock for the database
 **/
public class Lock {

	@Id
	Long id;

	private boolean isLocked = false;

	/**
	 * Defines a Lock with the boolean equals 'false'
	 **/
	public Lock() {
	}

	/**
	 * Defines a Lock with an login specified
	 * 
	 * @param id
	 *            Login of Lock
	 **/
	public Lock(Long id) {
		this.id = id;
	}

	/**
	 * Return the boolean saying if the database is lock
	 * 
	 * @return true If the database is lock
	 **/
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Set the lock of the database
	 * 
	 * @param isLocked
	 *            New lock of the database
	 **/
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}