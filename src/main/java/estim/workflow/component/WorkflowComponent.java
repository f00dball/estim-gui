package estim.workflow.component;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;

public interface WorkflowComponent {
	
	/**
	 * Execute the next workflow step
	 * @param eStimDevice
	 * @param eStimDeviceState
	 * @return
	 * @throws InterruptedException
	 * @throws DeviceException 
	 */
	public EStimDeviceState execute(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState) 
			throws InterruptedException, DeviceException;
	
	/**
	 * Get a short description of the component
	 * @return
	 */
	public String getDescription();
	
}
