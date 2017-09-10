package estim.gui;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class OutputPort {
	protected final String serialPortName;
	protected SerialPort serialPort;
	
	public OutputPort(final String serialPortName) {
		this.serialPortName = serialPortName;
	}
	
	public void openPort() {
		@SuppressWarnings("rawtypes")
		final Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			final CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				if (portId.getName().equals(serialPortName)) {
					System.out.println("Found port " + serialPortName);

					try {
						serialPort = (SerialPort) portId.open("SimpleWrite", 2000);
						serialPort.setSerialPortParams(9600,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);

						serialPort.notifyOnOutputEmpty(true);

					} catch(final Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	public static List<CommPortIdentifier> getPortList() {
		final List<CommPortIdentifier> resultList = new ArrayList<CommPortIdentifier>();
		@SuppressWarnings("rawtypes")
		final Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			final CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				resultList.add(portId);
			}
		}
		
		return resultList;
	}
	
	public void closePort() {
		if(serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}
	
	public boolean isPortOpen() {
		return serialPort != null;
	}
	
	public InputStream getInputStream() throws IOException {
		return serialPort.getInputStream();

	}
	
	public OutputStream getOutputStream() throws IOException {
		return serialPort.getOutputStream();
	}
}
