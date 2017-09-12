package estim.workflow.component;

import java.util.concurrent.TimeUnit;

import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public class AdjustOuputLevelOverTime implements WorkflowComponent {

	private int timeValue;
	private TimeUnit timeUnit;
	private short changeLevelA;
	private short changeLevelB;

	public AdjustOuputLevelOverTime(final int timeValue, final TimeUnit timeUnit, 
			final short changeLevelA, final short changeLevelB) {
				this.timeValue = timeValue;
				this.timeUnit = timeUnit;
				this.changeLevelA = changeLevelA;
				this.changeLevelB = changeLevelB;
	}

	@Override
	public EStimDeviceState execute(EStimDevice eStimDevice, EStimDeviceState eStimDeviceState)
			throws InterruptedException {
		
		final long milliseconds = timeUnit.toMillis(timeValue);
		
		final long sleep = Math.min(milliseconds / changeLevelA, milliseconds / changeLevelB);
		
		final long timeStart = System.currentTimeMillis();
		
		while(System.currentTimeMillis() < timeStart + milliseconds) {
			
			// TODO:
			
			Thread.sleep(sleep);
		}
		
		return eStimDevice.getState();
		
	}

	@Override
	public String getDescription() {
		return String.format("Set power within the %d %s to a=%d, b=%d", timeValue, 
				timeUnit.toString(), changeLevelA, changeLevelB);
	}

}
