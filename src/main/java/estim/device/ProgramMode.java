package estim.device;

import java.util.Arrays;

public enum ProgramMode {
	
	PULSE((short) 0), 
	BOUNCE((short) 1),
	CONTINOUS((short) 2),
	A_SPLIT((short) 3),
	B_SPLIT((short) 4),
	WAVE((short) 5),
	WATERFALL((short) 6),
	SQUEEZE((short) 7),
	MILK((short) 8),
	THROB((short) 9),
	THRUST((short) 10),
	RANDOM((short) 11),
	STEP((short) 12),
	TRAINING((short) 13),
	MICROPHONE((short) 14),
	STEREO((short) 15),
	TICKLE((short) 16);
	
	
	protected short programNumber;
	
	ProgramMode(final short programNumber) {
		this.programNumber = programNumber;
	}
	
	public short getProgramNumber() {
		return programNumber;
	}
	
	public static ProgramMode getFromProgramNumber(final short programNumber) {
		return Arrays.asList(values())
				.stream()
				.filter(p -> p.getProgramNumber() == programNumber)
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException("Unknown program number: " + programNumber));
	}

}
