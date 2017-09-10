package estim.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ReceivePackage {



	public final static int SYNC_BYTE = 0;
	public final static int WAVE_FORM_BYTE = 1;
	public final static int POWER_BYTE = 2;
	public final static int FREQUENCY_BYTE = 3;
	public final static int POTI1_BYTE = 4;
	public final static int POTI2_BYTE = 5;
	
	protected final static short MAX_WAIT = 1000;
	
	protected PacketValue packetValue[] = new PacketValue[6];
	
	protected Logger logger = Logger.getLogger(SendPackage.class);
	
	public void readData(final InputStream is) throws IOException, TimeoutException {
		byte[] buffer = new byte[1024];

		long startTimestamp = System.currentTimeMillis();

		while (is.available() < packetValue.length) {
			Thread.yield();
			
			if (startTimestamp + MAX_WAIT < System.currentTimeMillis()) {
				
				// Flush buffer
				int bytes = 0;
				if(is.available() > 0) {
					bytes = is.read(buffer);
				}
				throw new TimeoutException("Timeout only " + bytes + " bytes in buffer");
			}
		}

		is.read(buffer);

		for(int i = 0; i < packetValue.length; i++) {
			packetValue[i] = new PacketValue(buffer[i]);
		}
	}
	
	public PacketValue readValue(int aByte) {
		return packetValue[aByte];
	}
	
	@Override
	public String toString() {
		return "ReceivePackage [packetValue=" + Arrays.toString(packetValue)
				+ "]";
	}
}
