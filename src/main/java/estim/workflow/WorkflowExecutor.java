package estim.workflow;

import java.util.concurrent.TimeUnit;

import estim.device.DeviceException;
import estim.device.EStimDevice;
import estim.device.EStimDeviceState;
import estim.device.EStimDeviceStateBuilder;
import estim.device.ProgramMode;
import estim.workflow.component.AdjustOuputLevelOverTime;
import estim.workflow.component.AudioLevel;
import estim.workflow.component.SetupEstimDevice;
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
		
		final SetupEstimDevice setupEstimDevice = new SetupEstimDevice(eStimDeviceState);
		setupEstimDevice.execute(eStimDevice, eStimDeviceState);
		
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
				.withALevel((short) 10)
				.withProgramMode(ProgramMode.CONTINOUS)
		//		.withHighPowerMode(true)
				.build();

		final Workflow workflow = Workflow.create()
		//		.addNextWorkflowComponent(new AdjustOuputLevelOverTime(20, TimeUnit.SECONDS, (short) 8, (short) 0))
		//		.addNextWorkflowComponent(new SleepRandom(10, 30, TimeUnit.SECONDS))
				.addNextWorkflowComponent(new AudioLevel(15, 0, 5, 60, TimeUnit.SECONDS))
				.addNextWorkflowComponent(new AdjustOuputLevelOverTime(5, TimeUnit.SECONDS, (short) -5, (short) 0))
				.addNextWorkflowComponent(new SleepRandom(5, 15, TimeUnit.SECONDS));

		final WorkflowExecutor workflowExecutor = new WorkflowExecutor(eStimDevice, eStimDeviceState, workflow);
		workflowExecutor.setLoops(5);
		workflowExecutor.execute();
	}
}
