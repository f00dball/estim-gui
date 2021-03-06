package estim.device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import estim.util.CloseableHelper;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class EStimDevice {
	
	protected final String device;
	
	protected SerialPort serialPort;
	
	protected OutputStream outputStream;

	protected InputStream inputStream;
	
	protected EStimDeviceState eStimDeviceState;
	
	/**
	 * The logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(EStimDevice.class);

	
	public EStimDevice(final String device) {
		this.device = device;
	}
	
	protected synchronized void writeToDevice(final String value) throws DeviceException {
		try {
			outputStream.write((value + "\r").getBytes());
			
			readDeviceInfo();
		} catch (IOException e) {
			throw new DeviceException(e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
	}

	public void setA(final short value) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		if(value < 0 || value > 100) {
			throw new DeviceException("Invalid value for A: " + value);
		}
		
		writeToDevice("A" + Short.toString(value));
	}
	
	public void setB(final short value) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		if(value < 0 || value > 100) {
			throw new DeviceException("Invalid value for B: " + value);
		}
		
		writeToDevice("B" + Short.toString(value));
	}
	
	public void setC(final short value) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		
		if(value < 0 || value > 100) {
			throw new DeviceException("Invalid value for C: " + value);
		}
		
		writeToDevice("C" + Short.toString(value));
	}
	
	public void setD(final short value) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		if(value < 0 || value > 100) {
			throw new DeviceException("Invalid value for D: " + value);
		}
		
		writeToDevice("D" + Short.toString(value));
	}
	
	public void setProgramMode(final ProgramMode programMode) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		writeToDevice("M" + programMode.getProgramNumber());
	}
	
	public void setPowerMode(final boolean high) throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		if(high) {
			writeToDevice("H");
		} else {
			writeToDevice("L");
		}
	}
	
	public void kill() throws DeviceException {
		if(serialPort == null) {
			throw new DeviceException("Not connected");
		}
		
		writeToDevice("K");
	}
	
	public void open() throws DeviceException {
		@SuppressWarnings("unchecked")
		final Enumeration<CommPortIdentifier> portEnumration = CommPortIdentifier.getPortIdentifiers();
		final List<CommPortIdentifier> list = Collections.list(portEnumration);
		
		// Find the serial port
		final CommPortIdentifier portId = list.stream()
			.filter(p -> p.getPortType() == CommPortIdentifier.PORT_SERIAL)
			.filter(p -> p.getName().equals(device))
			.findFirst()
			.orElseThrow(() -> new DeviceException("Unable to find device: " + device));
		
		try {
			serialPort = (SerialPort) portId.open("ET-Connection", 2000);
			logger.info("Opened port: " + device);
			serialPort.setSerialPortParams(9600,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			outputStream = serialPort.getOutputStream();

			serialPort.notifyOnOutputEmpty(true);

			inputStream = serialPort.getInputStream();
		} catch (Exception e) {
			throw new DeviceException(e);
		} 
		
	}
	
	public void close() throws DeviceException {

		CloseableHelper.closeWithoutException(inputStream);
		inputStream = null;
		
		CloseableHelper.closeWithoutException(outputStream);
		outputStream = null;
		
		if(serialPort != null) {
			// Disable outputs 
			kill();
			
			serialPort.close();
			serialPort = null;
		}
		
	}
	
	public EStimDeviceState getState() {
		return eStimDeviceState;
	}
	
	public void refreshState() throws DeviceException {
		writeToDevice("");
	}
	
	protected boolean isInfoReadCompletely(final StringBuilder sb) {
		if(sb.length() == 0) {
			return false;
		}
				
		return sb.charAt(sb.length() - 1) == (char) 10;
	}
	
	protected void readDeviceInfo() throws IOException, InterruptedException {
		final StringBuilder sb = new StringBuilder();
		int loop = 0;
		
		while(! isInfoReadCompletely(sb)) {
			final byte buffer[] = new byte[128];
			final int bytesRead = inputStream.read(buffer);
			sb.append(new String(buffer), 0, bytesRead);
			Thread.sleep(10);
			loop++;
			
			if(loop > 100) {
				logger.error("Unable to read device result");
				return;
			}			
		}
		
		eStimDeviceState = new EStimDeviceState(sb.toString());
	}

	/**
	 * Test * Test * Test * Test * Test * Test
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		final String defaultPort = "/dev/tty.usbserial-FT9RF2ZO";
		final EStimDevice eStimDevice = new EStimDevice(defaultPort);
		eStimDevice.open();
		System.out.println("Setting mode");
		eStimDevice.setProgramMode(ProgramMode.CONTINOUS);
		int base = 12;
		
		final Random random = new Random();
		
		for(int loop = 0; loop < 5; loop++) {
			
			for(int i = 0; i < 10; i++) {
				Thread.sleep(random.nextInt(5000));
				System.out.println("Level " + i);
				eStimDevice.setA((short) (i + base));
			}
			
			for(int i = 0; i < 10; i++) {
				System.out.println("Wait..." + i);
				Thread.sleep(1000);
			}
			
			int extra = random.nextInt(5);
			for(int i = 0; i < extra; i++) {
				Thread.sleep(random.nextInt(5000));
				System.out.println("Extra Level " + i);
				eStimDevice.setA((short) (10 + i + base));
			}
			
			for(int i = 0; i < 10; i++) {
				System.out.println("Wait..." + i);
				Thread.sleep(1000);
			}
			
			for(int i = 10 + extra; i >= 0; i--) {
				eStimDevice.setA((short) (i + base));
				Thread.sleep(200);
			}
			
			for(int i = 0; i < 10; i++) {
				System.out.println("Wait..." + i);
				Thread.sleep(1000);
			}

			base = base + 3;
		}
				
		eStimDevice.close();
	}
}
