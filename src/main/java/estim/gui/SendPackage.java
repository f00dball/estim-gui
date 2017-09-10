package estim.gui;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class SendPackage {

	protected final static int SYNC_BYTE = 0;
	protected final static int WAVE_FORM_BYTE = 1;
	protected final static int POWER_BYTE = 2;
	protected final static int FREQUENCY_BYTE = 3;
	
	protected PacketValue packetValue[] = new PacketValue[4];
	
	protected Logger logger = Logger.getLogger(SendPackage.class);
	
	public SendPackage(int power, int frequency, WaveForm waveForm) {
		power = Math.min(power, 254); // 255 is sync
		frequency = Math.min(frequency, 254); // 255 is sync
		
		packetValue[SYNC_BYTE] = new PacketValue(255);		
		packetValue[WAVE_FORM_BYTE] = new PacketValue(waveForm.getValue());
		packetValue[POWER_BYTE] = new PacketValue(power);
		packetValue[FREQUENCY_BYTE] = new PacketValue(frequency);
	}
	
	public void send(OutputStream outputStream) throws IOException {
		for(int i = 0; i < packetValue.length; i++) {
			logger.info("Write: " + packetValue[i].getIntValue());
			outputStream.write(packetValue[i].getIntValue());
		}
	}
}
