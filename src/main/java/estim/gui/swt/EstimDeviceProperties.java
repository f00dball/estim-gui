package estim.gui.swt;

public enum EstimDeviceProperties {

	CHANNEL_A(0), 
	CHANNEL_B(1),
	CHANNEL_C(2),
	CHANNEL_D(3),
	PROGRAMM_MODE(4),
	POWER_LEVEL(5);
	
	
	protected int propertyNumber;
	
	EstimDeviceProperties(final int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}
	
	public int getPropertyNumber() {
		return propertyNumber;
	}
}
