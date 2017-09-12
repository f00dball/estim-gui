package estim.scripting;

public interface Language {
	public Matcher[] getMatchers();
	
	public Matcher getErrorMatcher();
	
	public Matcher getNewLineMatcher();
}
