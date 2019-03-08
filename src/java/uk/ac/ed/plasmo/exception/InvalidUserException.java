package uk.ac.ed.plasmo.exception;

public class InvalidUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidUserException() {
		super();
	}
	
	public InvalidUserException(String message) {
		super(message);
	}

}
