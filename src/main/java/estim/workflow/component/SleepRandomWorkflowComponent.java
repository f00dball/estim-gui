package estim.workflow.component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public class SleepRandomWorkflowComponent implements WorkflowComponent {
	
	private TimeUnit timeUnit;
	private int minTime;
	private int maxTime;

	public SleepRandomWorkflowComponent(final int minTime, final int maxTime, final TimeUnit timeUnit) {
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.timeUnit = timeUnit;
		
		if(minTime >= maxTime) {
			throw new IllegalArgumentException("min time has to be < as max time");
		}
	}

	@Override
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState) throws InterruptedException {
		
		final Random random = new Random();
		final int randomTime = random.nextInt(maxTime - minTime);
		
		Thread.sleep(timeUnit.toMillis(minTime + randomTime));
		
		return eStimDeviceState;
	}

	@Override
	public String getDescription() {
		return String.format("Sleep random between %d and %d %s", minTime, maxTime, timeUnit.toString());
	}

}
