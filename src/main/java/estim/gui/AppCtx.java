package estim.gui;

public class AppCtx {
	
	protected static AppCtx instance;

	protected ControllerWorker controllerWorker;
	protected AudioWorker audioWorker;

	public static synchronized AppCtx getInstance() {
		if(instance == null) {
			instance = new AppCtx();
		}
		
		return instance;
	}
	
	private AppCtx() {
		// private constructor
	}
	
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Unable to clone a singleton!");
	}

	public ControllerWorker getControllerWorker() {
		return controllerWorker;
	}

	public void setControllerWorker(final ControllerWorker controllerWorker) {
		this.controllerWorker = controllerWorker;
	}
	
	public AudioWorker getAudioWorker() {
		return audioWorker;
	}

	public void setAudioWorker(AudioWorker audioWorker) {
		this.audioWorker = audioWorker;
	}
}