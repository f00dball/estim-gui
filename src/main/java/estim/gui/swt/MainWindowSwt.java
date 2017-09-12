package estim.gui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.ProgramMode;

public class MainWindowSwt {
	
	private final EStimDevice estim;
	  
	String statusMessage = "Ready";
	Label statusLabel;
	Label currentFrequencyLabel;
	Label currentPulseLabel;
	
	public MainWindowSwt(Display display) {
		final String defaultPort = "/dev/tty.usbserial-FT9RF2ZO";
		estim = new EStimDevice(defaultPort);

		connect();
		
		initUI(display);		
	}
	
	private void connect() {
		try {
			estim.open();
			estim.refreshState();
			statusMessage = estim.getState().toString();
			
		} catch (DeviceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			statusMessage = "Connection failed: " + e.getLocalizedMessage();
		}
	}
	
	private void updateChannel(EstimDeviceProperties field, short input) {
		
		// TODO check estim state
		try {
			switch (field) {
			case CHANNEL_A:
				estim.setA(input);
				break;
			case CHANNEL_B:
				estim.setB(input);
				break;
			case CHANNEL_C:
				currentFrequencyLabel.setText(input + "   ");
				estim.setC(input);				
				break;
			case CHANNEL_D:
				currentPulseLabel.setText(input + "   ");
				estim.setD(input);
				break;				
			default:
				statusMessage = "Unknown value: " + field;					
			}
		} catch (DeviceException e) {
			e.printStackTrace();
			statusMessage = "Error updating estim: " + e.getLocalizedMessage();
		}
		
		if (statusLabel != null) {
			// TODO We should not have to get to the UI element here -> what Java/SWT pattern use for this?
			statusLabel.setText(statusMessage);	 
		}
	}
	
	private void updateProgramMode(String mode) {
		ProgramMode programMode = ProgramMode.valueOf(mode);

		try {
			estim.setProgramMode(programMode);
		} catch (DeviceException e) {
			e.printStackTrace();
			statusLabel.setText("Error updating program mode: " + e.getLocalizedMessage());
		}
	}
	
	private void updatePowerLevel(String level) {
		boolean high = level.equalsIgnoreCase("HIGH");
		
		try {
			estim.setPowerMode(high);
		} catch (DeviceException e) {
			e.printStackTrace();
			statusLabel.setText("Error updating power level: " + e.getLocalizedMessage());
		}
	}
	
	private void initUI(Display display) {
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
		
		// Main layout container
		GridLayout mainGrid = new GridLayout(3, false);
		mainGrid.marginWidth = 10;
		mainGrid.marginHeight = 10;
		mainGrid.verticalSpacing = 10;
		mainGrid.horizontalSpacing = 10;
		shell.setLayout(mainGrid);
		
		// Channel Intensities
		Composite c1 = new Composite(shell, SWT.BORDER);
		c1.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		GridLayout channelsGrid = new GridLayout(2, false);
		channelsGrid.marginWidth = 10;
		channelsGrid.marginHeight = 10;
		channelsGrid.verticalSpacing = 10;
		channelsGrid.horizontalSpacing = 10;
		channelsGrid.makeColumnsEqualWidth = true;
		c1.setLayout(channelsGrid);
		
		Label channelsLabel = new Label(c1, SWT.NONE);
		channelsLabel.setText("Channels");
		channelsLabel.setAlignment(SWT.CENTER);
		GridData channelsLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		channelsLabelData.horizontalSpan = 2;
		channelsLabel.setLayoutData(channelsLabelData);
		
		Scale scaleA = new Scale (c1, SWT.VERTICAL);
		scaleA.setMaximum (100);
		scaleA.setPageIncrement (10);
		scaleA.setSelection(100);
		scaleA.addListener(SWT.Selection, event -> updateChannel(EstimDeviceProperties.CHANNEL_A, (short)scaleA.getSelection()));
		GridData scaleAData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		scaleA.setLayoutData(scaleAData);
		
		Scale scaleB = new Scale (c1, SWT.VERTICAL);
		scaleB.setMaximum (100);
		scaleB.setPageIncrement (10);
		scaleB.setSelection(100);
		scaleB.addListener(SWT.Selection, event -> updateChannel(EstimDeviceProperties.CHANNEL_B, (short)scaleB.getSelection()));
		GridData scaleBData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		scaleB.setLayoutData(scaleBData);
		
		Label channelALabel = new Label(c1, SWT.NONE);
		channelALabel.setText("A");
		channelALabel.setAlignment(SWT.CENTER);
		GridData channelLabelAData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		channelALabel.setLayoutData(channelLabelAData);
		
		Label channelBLabel = new Label(c1, SWT.NONE);
		channelBLabel.setText("B");
		channelBLabel.setAlignment(SWT.CENTER);
		GridData channelLabelBData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		channelBLabel.setLayoutData(channelLabelBData);
		
		// Channel Settings
		Composite c2 = new Composite(shell, SWT.BORDER);
		c2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		GridLayout channelSettingsGrid = new GridLayout(2, false);
		channelSettingsGrid.marginWidth = 10;
		channelSettingsGrid.marginHeight = 10;
		channelSettingsGrid.verticalSpacing = 10;
		channelSettingsGrid.horizontalSpacing = 10;
		channelSettingsGrid.makeColumnsEqualWidth = true;
		c2.setLayout(channelSettingsGrid);
		
		Label channelSettingsLabel = new Label(c2, SWT.NONE);
		channelSettingsLabel.setText("Channel Settings");
		channelSettingsLabel.setAlignment(SWT.CENTER);
		GridData channelSettingsLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		channelSettingsLabelData.minimumWidth = 400;
		channelSettingsLabelData.horizontalSpan = 2;
		channelSettingsLabel.setLayoutData(channelSettingsLabelData);
		
		Label frequencyLabel = new Label(c2, SWT.NONE);
		frequencyLabel.setText("Frequency");
		GridData frequencyLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		frequencyLabelData.verticalIndent = 32;
		frequencyLabelData.horizontalIndent = 12;
		frequencyLabel.setLayoutData(frequencyLabelData);
		
		currentFrequencyLabel = new Label(c2, SWT.PUSH);
		currentFrequencyLabel.setText("50   ");
		GridData currentFrequencyLabelData = new GridData(SWT.RIGHT, SWT.BEGINNING, true, false);
		currentFrequencyLabelData.verticalIndent = 32;
		// TODO how to achieve this??? why is indention not working for right aligned items?
		//currentFrequencyLabelData.horizontalIndent = 12;
		currentFrequencyLabel.setLayoutData(currentFrequencyLabelData);

		Scale scaleFrequency = new Scale (c2, SWT.HORIZONTAL);
		scaleFrequency.setMaximum (100);
		scaleFrequency.setPageIncrement (10);
		scaleFrequency.setSelection(50);
		scaleFrequency.addListener(SWT.Selection, event -> updateChannel(EstimDeviceProperties.CHANNEL_C, (short)scaleFrequency.getSelection()));
		GridData scaleFrequencyData = new GridData(SWT.FILL, SWT.FILL, true, false);
		scaleFrequencyData.horizontalSpan = 2;
		scaleFrequency.setLayoutData(scaleFrequencyData);

		Label pulseLabel = new Label(c2, SWT.NONE);
		pulseLabel.setText("Pulse");
		GridData pulseLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		pulseLabelData.horizontalIndent = 12;
		pulseLabel.setLayoutData(pulseLabelData);
		
		currentPulseLabel = new Label(c2, SWT.PUSH);
		currentPulseLabel.setText("50   ");
		GridData currentPulseLabelData = new GridData(SWT.RIGHT, SWT.BEGINNING, true, false);
		// TODO how to achieve this??? why is indention not working for right aligned items?
		//currentPulseLabelData.horizontalIndent = 12;
		currentPulseLabel.setLayoutData(currentPulseLabelData);
		
		Scale scalePulse = new Scale (c2, SWT.HORIZONTAL);
		scalePulse.setMaximum (100);
		scalePulse.setPageIncrement (10);
		scalePulse.setSelection(50);
		scalePulse.addListener(SWT.Selection, event -> updateChannel(EstimDeviceProperties.CHANNEL_D, (short)scalePulse.getSelection()));
		GridData scalePulseData = new GridData(SWT.FILL, SWT.FILL, true, false);
		scalePulseData.horizontalSpan = 2;
		scalePulse.setLayoutData(scalePulseData);
		
		
		
		
		
		
		// Others
		Composite c3 = new Composite(shell, SWT.BORDER);
		c3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		GridLayout otherSettingsGrid = new GridLayout(2, false);
		otherSettingsGrid.marginWidth = 10;
		otherSettingsGrid.marginHeight = 10;
		otherSettingsGrid.verticalSpacing = 10;
		otherSettingsGrid.horizontalSpacing = 10;
		otherSettingsGrid.makeColumnsEqualWidth = false;
		c3.setLayout(otherSettingsGrid);
		
		Label othersLabel = new Label(c3, SWT.NONE);
		othersLabel.setText("Others");
		othersLabel.setAlignment(SWT.CENTER);
		GridData othersLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		othersLabelData.horizontalSpan = 2;
		othersLabel.setLayoutData(othersLabelData);
		
		Label programModeLabel = new Label(c3, SWT.NONE);
		programModeLabel.setText("Program Mode");
		GridData programModeLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		programModeLabelData.horizontalSpan = 2;
		programModeLabelData.verticalIndent = 32;
		programModeLabelData.horizontalIndent = 2;
		programModeLabel.setLayoutData(programModeLabelData);
		
		Combo programCombo = new Combo(c3, SWT.READ_ONLY);
		
		String[] programModeItems = new String[ProgramMode.values().length];
		ProgramMode[] values = ProgramMode.values();

		for (int i = 0; i < values.length; i++) {
			programModeItems[i] = values[i].toString();
		}
		
		programCombo.setItems(programModeItems);
		programCombo.addListener(SWT.Selection, evetn -> updateProgramMode(programCombo.getText()));
		GridData programComboData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		programCombo.setLayoutData(programComboData);
		
		Label powerLevelLabel = new Label(c3, SWT.NONE);
		powerLevelLabel.setText("Power Level");
		GridData powerLevelLabelData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		powerLevelLabelData.horizontalSpan = 2;
		powerLevelLabelData.verticalIndent = 18;
		powerLevelLabelData.horizontalIndent = 2;
		powerLevelLabel.setLayoutData(powerLevelLabelData);
		
		Combo powerLevelCombo = new Combo(c3, SWT.READ_ONLY);
		
		String[] powerLevelItems = new String[] { "LOW", "HIGH" };
		
		powerLevelCombo.setItems(powerLevelItems);
		powerLevelCombo.addListener(SWT.Selection, event -> updatePowerLevel(powerLevelCombo.getText()));
		GridData powerLevelComboData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		powerLevelCombo.setLayoutData(powerLevelComboData);
		
		
		
		
		
		
		
				
		// Status bar
		statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setText(statusMessage);
		GridData statusLabelData = new GridData(SWT.FILL, SWT.FILL, true, true);
		statusLabelData.horizontalSpan = 3;
		statusLabelData.verticalAlignment = SWT.BOTTOM;
		statusLabel.setLayoutData(statusLabelData);
		
		// Shell Settings 
		shell.setSize(1024, 768);
		shell.setMinimumSize(1024, 768);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Display display = new Display();
		MainWindowSwt window = new MainWindowSwt(display);
		display.dispose();
	}
}
