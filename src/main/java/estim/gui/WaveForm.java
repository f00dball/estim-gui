package estim.gui;

public enum WaveForm {
	
	UP_DOWN(0),
	DOWN_UP(1);
	
	private final int value;
	
	private WaveForm(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
