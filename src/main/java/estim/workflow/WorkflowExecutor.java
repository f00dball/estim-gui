package estim.workflow;

import java.util.concurrent.TimeUnit;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;
import estim.device.EStimDeviceStateBuilder;
import estim.device.ProgramMode;
import estim.workflow.component.AdjustOuputLevelOverTime;
import estim.workflow.component.SleepRandom;
import estim.workflow.component.WorkflowComponent;

public class WorkflowExecutor {
	
	protected int execution;
	protected EStimDevice eStimDevice;
	protected EStimDeviceState eStimDeviceState;
	protected Workflow workflow;
	protected int loops;

	public WorkflowExecutor(final EStimDevice eStimDevice, final EStimDeviceState eStimDeviceState, 
			final Workflow workflow) {
		
		this.eStimDevice = eStimDevice;
		this.eStimDeviceState = eStimDeviceState;
		this.workflow = workflow;
		this.execution = 0;
		this.loops = 1;
	}
	
	public void execute() throws InterruptedException, DeviceException {
		
		try {
			for(int loop = 0; loop < loops; loop++) {
				for(final WorkflowComponent workflowComponent : workflow.getComponents()) {
					System.out.println("Executing " + workflowComponent.getDescription());
					eStimDeviceState = workflowComponent.execute(eStimDevice, eStimDeviceState);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			eStimDevice.kill();
		}
	}
	
	public void setLoops(final int loops) {
		this.loops = loops;
	}

	public int getLoops() {
		return loops;
	}
	
	//=====================================================
	// Test * Test * Test * Test * Test * Test
	//=====================================================
	public static void main(final String[] args) throws DeviceException, InterruptedException {
		
		final String defaultPort = "/dev/tty.usbserial-FT9RF2ZO";
		final EStimDevice eStimDevice = new EStimDevice(defaultPort);
		eStimDevice.open();
		
		final EStimDeviceState eStimDeviceState = EStimDeviceStateBuilder.create()
				.withALevel((short) 15)
				.withProgramMode(ProgramMode.CONTINOUS)
				.build();

		final Workflow workflow = Workflow.create()
				.addNextWorkflowComponent(new AdjustOuputLevelOverTime(5, TimeUnit.SECONDS, (short) 20, (short) 0))
				.addNextWorkflowComponent(new SleepRandom(10, 40, TimeUnit.SECONDS))
				.addNextWorkflowComponent(new AdjustOuputLevelOverTime(-3, TimeUnit.SECONDS, (short) 10, (short) 0));
				
		final WorkflowExecutor workflowExecutor = new WorkflowExecutor(eStimDevice, eStimDeviceState, workflow);
		workflowExecutor.setLoops(5);
		workflowExecutor.execute();
	}
}
