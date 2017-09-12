package estim.workflow;

import java.util.ArrayList;
import java.util.List;

import estim.workflow.component.WorkflowComponent;

public class Workflow {

	protected final List<WorkflowComponent> components;
	
	public Workflow() {
		this.components = new ArrayList<>();
	}
	
	public Workflow addNextWorkflowComponent(final WorkflowComponent workflowComponent) {
		components.add(workflowComponent);
		return this;
	}

}
