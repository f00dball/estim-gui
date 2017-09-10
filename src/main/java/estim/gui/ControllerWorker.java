package estim.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import estim.gui.gui.MainWindow;

public class ControllerWorker implements Runnable {

	protected final static int MIN_DELAY = 10;
	
	protected final MainWindow mainwindow;
	protected final InputStream is;
	protected final OutputStream os; 
	
	protected OutputHistory powerHistory = new OutputHistory();
	protected OutputHistory frequencyHistory = new OutputHistory();

	protected Logger logger = Logger.getLogger(ControllerWorker.class);
	
	protected volatile boolean active = true;
	
	public ControllerWorker(final MainWindow mainwindow, final InputStream is, final OutputStream os) {
		this.mainwindow = mainwindow;
		this.is = is;
		this.os = os;
	}
	
	public void run() {
		boolean dummyMode = false;
		
		if(is == null || os == null) {
			logger.info("Running in dummy mode");
			dummyMode = true;
		}
		
		while (active) {	
			
			// Wait for start
			while("Start".equals(mainwindow.getStartStopButton().getText())) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
			}
			
			logger.debug("New controller run");
			
			sendPackage(dummyMode);
		//	readResult(dummyMode);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				active = false;
				return;
			}
		}
	}

	private void sendPackage(boolean dummyMode) {
		int powerSlider = mainwindow.getPowerSlider().getValue();
		int audioValue = mainwindow.getAudioFeedbackSlider().getValue();
		
		try {
			audioValue = (int) (Integer.parseInt(mainwindow.getAudioTextField().getText()) / 100.0 * audioValue);
		} catch(NumberFormatException e) {
			audioValue = 0;
		}
		
		int totalPower =  powerSlider + audioValue;
		logger.debug("Total Power: " + totalPower);
		
		final int power = Math.min(255, totalPower);
		powerHistory.addValue(power);
		
		final int guiDelayTime = mainwindow.getPauseSlider().getValue();
		frequencyHistory.addValue(guiDelayTime);
		logger.debug("Delay: " + guiDelayTime);
		
		if(!dummyMode) {
			final SendPackage sendPackage = new SendPackage(power, guiDelayTime, WaveForm.DOWN_UP);
			logger.debug("Sending package");
			try {
				sendPackage.send(os);
			} catch (IOException e) {
				return;
			}
		}
	}

	public OutputHistory getPowerHistory() {
		return powerHistory;
	}

	protected void readResult(boolean dummyMode) {
		// Update gui from controller
		if(dummyMode) {
			mainwindow.getPoti1TextField().setText("0");
			mainwindow.getPoti2TextField().setText("0");
		} else {
			logger.debug("Read result");
			final ReceivePackage receivePackage = new ReceivePackage();
			try {
				receivePackage.readData(is);
				mainwindow.getPoti1TextField().setText(receivePackage.readValue(ReceivePackage.POTI1_BYTE).getValue());
				mainwindow.getPoti2TextField().setText(receivePackage.readValue(ReceivePackage.POTI2_BYTE).getValue());
			} catch (IOException e) {
				logger.warn("Exception", e);
			} catch (TimeoutException e) {
				logger.info("Timeout exception");
				logger.warn("Exception", e);
			}
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public OutputHistory getFrequencyHistory() {
		return frequencyHistory;
	}
}