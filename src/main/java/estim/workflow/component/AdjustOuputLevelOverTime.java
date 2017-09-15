package estim.workflow.component;

import java.util.concurrent.TimeUnit;

import estim.device.DeviceException;
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
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState)
			throws InterruptedException, DeviceException {
		
		final long milliseconds = timeUnit.toMillis(timeValue);
		
		final double deltaA = (double) changeLevelA / (double) milliseconds;
		final double deltaB = (double) changeLevelB / (double) milliseconds;
		
		final short totalA = (short) (eStimDeviceState.getA() + changeLevelA);
		final short totalB = (short) (eStimDeviceState.getB() + changeLevelB);
		
		System.out.println(String.format("Destination output level a=%d b=%d\n", totalA, totalB));
		
		final long timeStart = System.currentTimeMillis();
		
		while(System.currentTimeMillis() < timeStart + milliseconds) {
			
			final long elapsedTime = System.currentTimeMillis() - timeStart;
			
			final short offsetA = (short) (elapsedTime * deltaA);
			final short offsetB = (short) (elapsedTime * deltaB);
			
			eStimDevice.setA((short) (eStimDeviceState.getA() + offsetA));
			eStimDevice.setB((short) (eStimDeviceState.getB() + offsetB));

			Thread.sleep(100);
		}

		eStimDevice.setA(totalA);
		eStimDevice.setB(totalB);

		return eStimDevice.getState();
	}

	@Override
	public String getDescription() {
		return String.format("Set power within the %d %s to a=%d, b=%d", timeValue, 
				timeUnit.toString(), changeLevelA, changeLevelB);
	}

}
