package estim.device;

public class EStimDeviceStateBuilder {

	protected short aLevel = 0;
	protected short bLevel = 0;
	protected short cLevel = 50;
	protected short dLevel = 50;
	protected boolean highPowerMode = false;
	protected ProgramMode programMode = ProgramMode.PULSE;
	
	private EStimDeviceStateBuilder() {
	}
	
	public static EStimDeviceStateBuilder create() {
		return new EStimDeviceStateBuilder();
	}
	
	public EStimDeviceState build() {
		return new EStimDeviceState(aLevel, bLevel, cLevel, dLevel, programMode, highPowerMode);
	}

	public EStimDeviceStateBuilder withALevel(final short aLevel) {
		this.aLevel = aLevel;
		return this;
	}
	
	public EStimDeviceStateBuilder withBLevel(final short bLevel) {
		this.bLevel = bLevel;
		return this;
	}
	
	public EStimDeviceStateBuilder withCLevel(final short cLevel) {
		this.cLevel = cLevel;
		return this;
	}
	
	public EStimDeviceStateBuilder withDLevel(final short dLevel) {
		this.dLevel = dLevel;
		return this;
	}
	
	public EStimDeviceStateBuilder withHighPowerMode(final boolean highPowerMode) {
		this.highPowerMode = highPowerMode;
		return this;
	}
	
	public EStimDeviceStateBuilder withProgramMode(final ProgramMode programMode) {
		this.programMode = programMode;
		return this;
	}
}
