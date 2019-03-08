package uk.ac.ed.plasmo.exception;

public class InvalidSubmissionException extends RuntimeException {

	private static final long serialVersionUID = 1188913429282020906L;
	
	public InvalidSubmissionException() {
		super();
	}
	
	public InvalidSubmissionException(String message) {
		super(message);
	}

}
