package estim.workflow;

import java.util.concurrent.TimeUnit;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;
import estim.device.ProgramMode;
import estim.workflow.component.AdjustOuputLevelOverTime;
import estim.workflow.component.ShutdownDevice;
import estim.workflow.component.SleepRandom;
import estim.workflow.component.WorkflowComponent;

public class WorkflowExecutor {
	
	protected int execution;
	protected EStimDevice eStimDevice;
	protected EStimDeviceState eStimDeviceState;
	protected Workflow workflow;


	public WorkflowExecutor(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState, 
			final Workflow workflow) {
		
		this.eStimDevice = eStimDevice;
		this.eStimDeviceState = eStimDeviceState;
		this.workflow = workflow;
		this.execution = 0;
	}
	
	public void execute() throws InterruptedException, DeviceException {
		for(final WorkflowComponent workflowComponent : workflow.getComponents()) {
			System.out.println("Executing " + workflowComponent.getDescription());
			eStimDeviceState = workflowComponent.execute(eStimDevice, eStimDeviceState);
		}
	}

	
	//=====================================================
	// Test * Test * Test * Test * Test * Test
	//=====================================================
	public static void main(final String[] args) throws DeviceException, InterruptedException {
		
		final String defaultPort = "/dev/tty.usbserial-FT9RF2ZO";
		final EStimDevice eStimDevice = new EStimDevice(defaultPort);
		eStimDevice.open();
		
		final EStimDeviceState eStimDeviceState = new EStimDeviceState((short) 10, (short) 10, 
				(short) 50, (short) 50, ProgramMode.CONTINOUS, false);
		
		final Workflow workflow = Workflow
				.create()
				.addNextWorkflowComponent(new AdjustOuputLevelOverTime(10, TimeUnit.SECONDS, (short) 5, (short) 0))
				.addNextWorkflowComponent(new SleepRandom(10, 40, TimeUnit.SECONDS))
				.addNextWorkflowComponent(new ShutdownDevice());
		
		final WorkflowExecutor workflowExecutor = new WorkflowExecutor(eStimDevice, eStimDeviceState, workflow);
		workflowExecutor.execute();
	}
}
