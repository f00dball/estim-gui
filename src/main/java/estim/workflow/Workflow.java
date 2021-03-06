package estim.workflow;

import java.util.ArrayList;
import java.util.List;

import estim.workflow.component.WorkflowComponent;

public class Workflow {

	protected final List<WorkflowComponent> components;
	
	private Workflow() {
		this.components = new ArrayList<>();
	}
	
	public static Workflow create() {
		return new Workflow();
	}
	
	public Workflow addNextWorkflowComponent(final WorkflowComponent workflowComponent) {
		components.add(workflowComponent);
		return this;
	}

	public List<WorkflowComponent> getComponents() {
		return components;
	}
}
