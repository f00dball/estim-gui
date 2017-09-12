package scripting;

public interface Matcher {
	public String getName();
	
	public boolean isMatch(String input, int offset);
	
	public MatchInfo match(String input, int offset);
}
