package estim.gui;

public class PacketValue {
	protected char value;
	
	public PacketValue(byte value) {
		this.value = (char) value;
		this.value = (char) (this.value & 0x00FF);
	}
	
	public PacketValue(int value) {
		this.value = (char) value;
		this.value = (char) (this.value & 0x00FF);
	}
	
	public int getIntValue() {
		return value;
	}

	public String toString() {
		return "PacketValue [value=" + value + "]";
	}
	
	public String getValue() {
		return Integer.toString((int) value);
	}
	
}