package estim.workflow.component;

import java.util.concurrent.TimeUnit;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;
import estim.gui.AudioWorker;

public class AudioLevel implements WorkflowComponent {

	private final int maxA;
	private final int maxB;
	private final int timePeriodAudio;
	private final int timeState;
	private final TimeUnit timeUnit;

	public AudioLevel(final int maxA, final int maxB, final int timePeriodAudio, 
			final int timeState, final TimeUnit timeUnit) {
				this.maxA = maxA;
				this.maxB = maxB;
				this.timePeriodAudio = timePeriodAudio;
				this.timeState = timeState;
				this.timeUnit = timeUnit;
	}

	@Override
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState)
			throws InterruptedException, DeviceException {
		
		final AudioWorker audioWorker = new AudioWorker((value) -> {
			
			final short valueA = (short) ((value / 127.0) * maxA);
			final short valueB = (short) ((value / 127.0) * maxB);
					
			System.out.println("Feedback : " + value + " a=" + valueA + " b=" + valueB);

			try {
				eStimDevice.setA((short) (eStimDeviceState.getA() + valueA));
				eStimDevice.setB((short) (eStimDeviceState.getB() + valueB));
			} catch (DeviceException e) {
				System.err.println(e);
			}

		});
		
		final Thread thread = new Thread(audioWorker);
		thread.start();
		
		Thread.sleep(timeUnit.toMillis((timeState)));
		
		thread.interrupt();
		thread.join(1000);
		
		eStimDevice.setA(eStimDeviceState.getA());
		eStimDevice.setB(eStimDeviceState.getB());

		return eStimDeviceState;
	}

	@Override
	public String getDescription() {
		return "Adjust output level by audio";
	}

	
}
