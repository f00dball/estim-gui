package estim.workflow.component;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public class ShutdownDevice implements WorkflowComponent {

	@Override
	public EStimDeviceState execute(EStimDevice eStimDevice, EStimDeviceState eStimDeviceState)
			throws InterruptedException, DeviceException {
		
		eStimDevice.kill();
		
		return eStimDevice.getState();
	}

	@Override
	public String getDescription() {
		return "Shutdown the device";
	}

}
