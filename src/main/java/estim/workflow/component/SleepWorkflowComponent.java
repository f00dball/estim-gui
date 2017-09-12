package estim.workflow.component;

import java.util.concurrent.TimeUnit;

import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public class SleepWorkflowComponent implements WorkflowComponent {
	
	private int time;
	private TimeUnit timeUnit;

	public SleepWorkflowComponent(final int time, final TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
	}

	@Override
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState) throws InterruptedException {
		Thread.sleep(timeUnit.toMillis(time));
		
		return eStimDeviceState;
	}

	@Override
	public String getDescription() {
		return String.format("Sleep %d %s", time, timeUnit.toString());
	}

}
