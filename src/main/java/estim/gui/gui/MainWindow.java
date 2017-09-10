package estim.gui.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class MainWindow {
	
	public JTextField getPoti1TextField() {
		return poti1TextField;
	}

	public JTextField getPoti2TextField() {
		return poti2TextField;
	}

	public JButton getStartStopButton() {
		return startStopButton;
	}

	private JFrame mainFrame;
	private JSlider powerSlider;
	private JSlider pauseSlider;
	private JTextField pauseValue;
	private JTextField powerValue;
	private JTextField poti1TextField;
	private JTextField poti2TextField;
	private JTextField audioTextField;
	private JSlider audioFeedbackSlider;
	private JTextField audioFeedbackValue;
	private JButton startStopButton;
	
	public MainWindow(final String portname) {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("E-Stim - " + portname);
				
    	final JPanel southPanel = new JPanel();
		
    	buildSouthPanel(southPanel);
		
		mainFrame.add(setupLayout(), BorderLayout.NORTH);
		mainFrame.add(southPanel, BorderLayout.SOUTH);
	}
	
	public JTextField getAudioTextField() {
		return audioTextField;
	}

	public void show() {
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private void buildSouthPanel(final JPanel southPanel) {
		startStopButton = new JButton("Start");
		
		startStopButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if("Start".equals(startStopButton.getText())) {
					startStopButton.setText("Stop");
				} else {
					startStopButton.setText("Start");
				}
			}
		});
		
		final JButton historyButton = new JButton("Historie");
		historyButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				HistoryWindow historyWindow = new HistoryWindow();
				historyWindow.pack();
				historyWindow.setLocationRelativeTo(null);
				historyWindow.setVisible(true);
			}
		});
		
		
    	southPanel.add(startStopButton);
    	southPanel.add(historyButton);
	}
	
    public JSlider getAudioFeedbackSlider() {
		return audioFeedbackSlider;
	}

	protected JPanel setupLayout() {
    	final FormLayout formLayout = new FormLayout(
    			"right:pref, 4dlu, max(default; 40dlu), 4dlu, 150dlu:grow, 4dlu, max(default; 25dlu)",
    			"pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref");
    	
    //	PanelBuilder builder = new PanelBuilder(formLayout, new FormDebugPanel()); 
    	PanelBuilder builder = new PanelBuilder(formLayout); 
    	builder.setDefaultDialogBorder(); 
    	CellConstraints cc = new CellConstraints();
    	
    	builder.addSeparator("Control", cc.xyw(1,  1, 7)); 
    	
    	builder.addLabel("Power:", cc.xy (1,  3)); 
    	powerSlider = new JSlider();
    	powerSlider.setPaintLabels(true);
    	powerSlider.setPaintTicks(true);
    	powerSlider.setMinimum(0);
    	powerSlider.setMaximum(255);
    	powerSlider.setValue(0);
       	builder.add(powerSlider, cc.xyw (3,  3, 4)); 
    	powerValue = new JTextField("0 %");
    	builder.add(powerValue, cc.xy(7, 3));

    	powerSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				powerValue.setText(Integer.toString((int) (powerSlider.getValue() / 255.0 * 100)) + " %");
			}
		});
 
      	builder.addLabel("Frequenz:", cc.xy (1,  5)); 
    	pauseSlider = new JSlider();
    	pauseSlider.setPaintLabels(true);
    	pauseSlider.setPaintTicks(true);
    	pauseSlider.setMinimum(0);
    	pauseSlider.setMaximum(255);
    	pauseSlider.setValue(0);
    	builder.add(pauseSlider, cc.xyw (3,  5, 4)); 
    	pauseValue = new JTextField("0 %");
    	builder.add(pauseValue, cc.xy(7, 5));
    	
    	pauseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				pauseValue.setText(Integer.toString((int) (pauseSlider.getValue() / 255.0 * 100)) + " %");
			}
		});

    
    	
    	builder.addSeparator("Audio Feedback", cc.xyw(1,  7, 7)); 
      	builder.addLabel("Audio:", cc.xy (1, 9)); 
    	audioTextField = new JTextField("0");
    	builder.add(audioTextField, cc.xy(3, 9));
    	
      	builder.addLabel("Feedback:", cc.xy (1,  11)); 
    	audioFeedbackSlider = new JSlider();
    	audioFeedbackSlider.setPaintLabels(true);
    	audioFeedbackSlider.setPaintTicks(true);
    	audioFeedbackSlider.setMinimum(0);
    	audioFeedbackSlider.setMaximum(100);
    	audioFeedbackSlider.setValue(0);
    	builder.add(audioFeedbackSlider, cc.xyw (3,  11, 4)); 
    	audioFeedbackValue = new JTextField("0 %");
    	builder.add(audioFeedbackValue, cc.xy(7, 11));

    	audioFeedbackSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				audioFeedbackValue.setText(Integer.toString(audioFeedbackSlider.getValue())+ " %");
			}
		});
    	
    	builder.addSeparator("Controller", cc.xyw(1,  13, 7)); 
    	
    	builder.addLabel("Poti 1:", cc.xy (1, 15)); 
    	poti1TextField = new JTextField();
    	builder.add(poti1TextField, cc.xy(3, 15));

    	builder.addLabel("Poti 2:", cc.xy (1, 17)); 
    	poti2TextField = new JTextField();
    	builder.add(poti2TextField, cc.xy(3, 17));
    	
    	return builder.getPanel();
	}

	public JSlider getPowerSlider() {
		return powerSlider;
	}

	public JSlider getPauseSlider() {
		return pauseSlider;
	}
	
	public static void main(String[] args) {
		final MainWindow mainWindow = new MainWindow("Demo");
		mainWindow.show();
	}
}
