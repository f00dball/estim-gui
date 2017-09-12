package estim.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EStimDeviceState {
	
	protected String firmwareVersion;
	
	protected boolean highPowerMode;
	
	protected int energySource;
	
	protected short a;
	
	protected short b;
	
	protected short c;
	
	protected short d;

	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(EStimDeviceState.class);

	// 
	// 512:0:0:100:100:2:L:0:2.105
	//
	public EStimDeviceState(final String state) {
		final String[] splitState = state.split(":");
		
		if(splitState.length != 9) {
			logger.error("Unable to parse : " + state);
			return;
		}
		
		energySource = parseShort(splitState[0]);
		
		a = (short) (parseShort(splitState[1]) / 2);
		b = (short) (parseShort(splitState[2]) / 2);
		c = (short) (parseShort(splitState[3]) / 2);
		d = (short) (parseShort(splitState[4]) / 2);
		
		if("L".equals(splitState[6])) {
			highPowerMode = false;
		} else {
			highPowerMode = true;
		}
		
		firmwareVersion = splitState[8].replaceAll("\n", "");
		
		System.out.println(this);
	}

	public static short parseShort(final String shortValue) {
		try {
			return Short.parseShort(shortValue);
		} catch (NumberFormatException e) {
			logger.error("Unable to parse {} as short", shortValue);
			return -1;
		}
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}


	public boolean isHighPowerMode() {
		return highPowerMode;
	}


	public int getEnergySource() {
		return energySource;
	}


	public short getA() {
		return a;
	}


	public short getB() {
		return b;
	}


	public short getC() {
		return c;
	}


	public short getD() {
		return d;
	}


	@Override
	public String toString() {
		return "EStimDeviceState [firmwareVersion=" + firmwareVersion + ", highPowerMode=" + highPowerMode
				+ ", energySource=" + energySource + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		result = prime * result + c;
		result = prime * result + d;
		result = prime * result + energySource;
		result = prime * result + ((firmwareVersion == null) ? 0 : firmwareVersion.hashCode());
		result = prime * result + (highPowerMode ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EStimDeviceState other = (EStimDeviceState) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		if (c != other.c)
			return false;
		if (d != other.d)
			return false;
		if (energySource != other.energySource)
			return false;
		if (firmwareVersion == null) {
			if (other.firmwareVersion != null)
				return false;
		} else if (!firmwareVersion.equals(other.firmwareVersion))
			return false;
		if (highPowerMode != other.highPowerMode)
			return false;
		return true;
	}

}
