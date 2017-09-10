package estim.gui.old;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class Main {

	@SuppressWarnings("rawtypes")
	static Enumeration portList;
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static OutputStream outputStream;
	static boolean outputBufferEmptyFlag = false;

	/**
	 * Method declaration
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedCommOperationException 
	 * @throws PortInUseException 
	 * @throws InterruptedException 
	 * 
	 * @see
	 */
	public static void main(String[] args) throws IOException, UnsupportedCommOperationException, PortInUseException, InterruptedException {
		boolean portFound = false;
		String defaultPort = "/dev/tty.usbserial-FT9RF2ZO";

		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL 
					&& portId.getName().equals(defaultPort)) {
			
					portFound = true;
					
					System.out.println("Found port " + defaultPort);

					
					serialPort = (SerialPort) portId.open("ET-Connection", 2000);
					System.out.println("Opened port");
					outputStream = serialPort.getOutputStream();
					serialPort.setSerialPortParams(9600,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					System.out.println("Set parameter");

					serialPort.notifyOnOutputEmpty(true);

					InputStream in = serialPort.getInputStream();
					
					for(int i = 0; i < 10; i++) {
						outputStream.write(("A" + Integer.toString(i) + "\r").getBytes());
						
						Thread.sleep(100);
						
						final StringBuilder sb = new StringBuilder();
	
						while(in.available() > 0) {
							final byte buffer[] = new byte[128];
							in.read(buffer);
							sb.append(new String(buffer));
						}
						System.out.println(sb);
					}
					
					Thread.sleep(2000);
					outputStream.write("K\r".getBytes());

					serialPort.close();
					
					
				}
			}

			if (!portFound) {
				System.out.println("port " + defaultPort + " not found.");
			}
	}

}
