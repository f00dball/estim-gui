package scripting;

public class MatchInfo {
	private final String content;
	private final boolean success;
	private final int length;

	public MatchInfo(String content, boolean success, int length) {
		this.content = content;
		this.success = success;
		this.length = length;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public int getLength() {
		return length;
	}
}
