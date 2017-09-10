package estim.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import estim.gui.gui.MainWindow;
import estim.gui.gui.ObservableString;
import estim.gui.gui.PortSelectorWindow;

public class Main {
	

	static class GUIResultWrapper {
		private String port;

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}
	}

	public static void main(final String[] args) throws IOException {
		final String port = selectPort();
		
		startApplication(port);
	}

	private static String selectPort() {
		
		final PortSelectorWindow portSelectorWindow = new PortSelectorWindow();
		final GUIResultWrapper resultWrapper = new GUIResultWrapper();
		
		final Observer portObserver = new Observer() {
			
			public void update(Observable o, Object arg) {
				if(o instanceof ObservableString) {
					final ObservableString observableString = (ObservableString) o;
					
					synchronized (resultWrapper) {
						resultWrapper.setPort(observableString.getData());
						resultWrapper.notifyAll();
					}
				}
			}			
		};
		
		portSelectorWindow.getResultObserver().addObserver(portObserver);
		portSelectorWindow.setVisible(true);
		
		synchronized (resultWrapper) {
			while(resultWrapper.getPort() == null) {
				try {
					resultWrapper.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		return resultWrapper.getPort();
	}
	
	private static void startApplication(final String port) throws IOException {
		
		InputStream is = null;
		OutputStream os = null;
		
		if(! port.contains("DEVELOPMENT")) {
			final OutputPort serialPort = new OutputPort(port);
			
			serialPort.openPort();
			
			if(! serialPort.isPortOpen()) {
				System.out.println("Unable to open port");
				return;
			}
			
			 is = serialPort.getInputStream();
			 os = serialPort.getOutputStream();
		}
		
		final MainWindow mainWindow = new MainWindow(port);
		
		final AudioWorker audioWorker = new AudioWorker(mainWindow.getAudioTextField());
		(new Thread(audioWorker)).start();
		
		final ControllerWorker controllerWorker = new ControllerWorker(mainWindow, is, os);
		final Thread controllerThread = new Thread(controllerWorker);
		controllerThread.start();
		controllerThread.setPriority(Thread.MAX_PRIORITY);
		
		AppCtx.getInstance().setControllerWorker(controllerWorker);
		AppCtx.getInstance().setAudioWorker(audioWorker);
		setLookAndFeel();
		
		mainWindow.show();
	} 
	
	/**
	 * Try to set the new Nimbus L&F
	 */
	protected static void setLookAndFeel() {
		try {
		    for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            return;
		        }
		    }
		} catch (Exception e) {
		    try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception e1) {
		    	// Ignore exception and use the old look and feel
		    }
		}
	}
}
