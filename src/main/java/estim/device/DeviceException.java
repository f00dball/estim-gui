package estim.device;

public class DeviceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2511119040994688293L;

	public DeviceException() {
	}

	public DeviceException(String message) {
		super(message);
	}

	public DeviceException(Throwable cause) {
		super(cause);
	}

	public DeviceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeviceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
