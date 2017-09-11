package estim.gui;

import java.awt.Color;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class AudioWorker implements Runnable {

	protected Logger logger = Logger.getLogger(AudioWorker.class);
	
	protected static TargetDataLine targetDataLine;
	protected static AudioInputStream m_audioInputStream;
	protected volatile boolean active = true;
	
	protected OutputHistory outputHistory = new OutputHistory();
	
	protected JTextField textFieldToUpdate;
	protected int movingAverage[] = new int[5];
	protected int maPos = 0;
	
	public AudioWorker(JTextField textfield) {
		setup();
		this.textFieldToUpdate = textfield;
	}
	
	protected void setup() {
		AudioFormat audioFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_UNSIGNED, 8000.0F, 8, 1, 1, 8000,
				false);
		
		/*
		 * Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
 for (Mixer.Info info: mixerInfos){
  Mixer m = AudioSystem.getMixer(info);
  Line.Info[] lineInfos = m.getSourceLineInfo();
  for (Line.Info lineInfo:lineInfos){
   System.out.println (info.getName()+"---"+lineInfo);
   Line line = m.getLine(lineInfo);
   System.out.println("\t-----"+line);
  }
  lineInfos = m.getTargetLineInfo();
  for (Line.Info lineInfo:lineInfos){
   System.out.println (m+"---"+lineInfo);
   Line line = m.getLine(lineInfo);
   System.out.println("\t-----"+line);

  }
		 */
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,
				audioFormat);
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		m_audioInputStream = new AudioInputStream(targetDataLine);

	}

	public void run() {
		targetDataLine.start();

		byte[] tempBuffer = new byte[4096];
		
		while(active) {
			int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			int max = 0;
			
			for (int i = 0; i < cnt; i++) {
				int totalValue = (int) tempBuffer[i];

				if(totalValue > max) {
					max = totalValue;
				}				
			}

			updateGui(max);

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private void updateGui(int max) {
		movingAverage[maPos] = max;
		logger.debug(maPos + " " + movingAverage[maPos]);
		maPos = (maPos + 1) % movingAverage.length;
		
		int sum = 0;
		for(int i = 0; i < movingAverage.length; i++)
			sum = sum + movingAverage[i];
		
		sum = sum / movingAverage.length;
		final String maxFinal = Integer.toString(sum);

		if(! "".equals(textFieldToUpdate.getText())) {
			final int oldValue = Integer.parseInt(textFieldToUpdate.getText());
			
			if(oldValue < max) {
				textFieldToUpdate.setBackground(Color.GREEN);
			} else if(oldValue == max) {
				textFieldToUpdate.setBackground(Color.WHITE);
			} else {
				textFieldToUpdate.setBackground(Color.RED);
			}
		}
		
		outputHistory.addValue(sum);
		
		textFieldToUpdate.setText(maxFinal);
	}
	
	public OutputHistory getOutputHistory() {
		return outputHistory;
	}

	public void shutdown() {
		this.active = false;
	}
}
