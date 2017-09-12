package estim.workflow.component;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public class SetupEstimDevice implements WorkflowComponent {

	private EStimDeviceState desiredEStimDeviceState;

	public SetupEstimDevice(final EStimDeviceState eStimDeviceState) {
		this.desiredEStimDeviceState = eStimDeviceState;
	}

	@Override
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState)
			throws InterruptedException, DeviceException {
		
		eStimDevice.setPowerMode(desiredEStimDeviceState.isHighPowerMode());
		eStimDevice.setProgramMode(desiredEStimDeviceState.getProgramMode());
		eStimDevice.setA(desiredEStimDeviceState.getA());
		eStimDevice.setB(desiredEStimDeviceState.getB());
		eStimDevice.setC(desiredEStimDeviceState.getC());
		eStimDevice.setD(desiredEStimDeviceState.getD());
		
		return eStimDevice.getState();
	}

	@Override
	public String getDescription() {
		return "Set the estim device to the desired mode";
	}

}
