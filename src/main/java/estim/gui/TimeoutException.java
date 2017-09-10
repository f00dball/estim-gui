package estim.gui;

public class TimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486133055366499791L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(Throwable cause) {
		super(cause);
	}

}
