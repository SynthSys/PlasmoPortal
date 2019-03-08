package uk.ac.ed.plasmo.exception;

public class InvalidModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidModelException() {
		super();
	}
	
	public InvalidModelException(String message){
		super(message);
	}

}
